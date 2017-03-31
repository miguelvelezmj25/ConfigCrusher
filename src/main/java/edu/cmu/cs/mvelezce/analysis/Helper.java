package edu.cmu.cs.mvelezce.analysis;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFactoryManager;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.editing.AdvancedNodeCreator;
import de.ovgu.featureide.fm.core.filter.AbstractFeatureFilter;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.ovgu.featureide.fm.core.job.monitor.IMonitor;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import org.apache.commons.math3.util.Combinations;
import org.prop4j.Node;
import org.prop4j.analyses.PairWiseConfigurationGenerator;
import org.prop4j.solver.SatInstance;

import java.util.*;

/**
 * Helper class for performing analyses.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class Helper {

    public void incling() throws InterruptedException {
        IFeatureModelFactory factory = DefaultFeatureModelFactory.getInstance();
        IFeatureModel featureModel = factory.createFeatureModel();
        IFeature feature = factory.createFeature(featureModel, "root");
        featureModel.addFeature(feature);
        featureModel.getStructure().setRoot(feature.getStructure());

        feature = factory.createFeature(featureModel, "A");
        featureModel.addFeature(feature);
        feature.getStructure().addChild(feature.getStructure());

        feature = factory.createFeature(featureModel, "B");
        featureModel.addFeature(feature);
        feature.getStructure().addChild(feature.getStructure());

        feature = factory.createFeature(featureModel, "C");
        featureModel.addFeature(feature);
        feature.getStructure().addChild(feature.getStructure());

        feature = factory.createFeature(featureModel, "D");
        featureModel.addFeature(feature);
        feature.getStructure().addChild(feature.getStructure());

        AdvancedNodeCreator advancedNodeCreator = new AdvancedNodeCreator(featureModel, new AbstractFeatureFilter());
        advancedNodeCreator.setCnfType(AdvancedNodeCreator.CNFType.Regular);
        advancedNodeCreator.setIncludeBooleanValues(false);

        Node createNodes = advancedNodeCreator.createNodes();
        SatInstance satInstance = new SatInstance(createNodes);
        PairWiseConfigurationGenerator generator = new PairWiseConfigurationGenerator(satInstance, 200);
//        satInstance.convertToString(generator.q.take().getModel());

        this.exec(satInstance, generator, new NullMonitor());

    }

    protected void exec(final SatInstance satInstance, final PairWiseConfigurationGenerator generator, IMonitor monitor) {
        final Thread consumer = new Thread() {
            @Override
            public void run() {
                int foundConfigurations = 0;
                while (true) {
                    try {
                        generateConfiguration(satInstance.convertToString(generator.q.take().getModel()));
                        foundConfigurations++;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                foundConfigurations += generator.q.size();
                for (org.prop4j.analyses.PairWiseConfigurationGenerator.Configuration c : generator.q) {
                    generateConfiguration(satInstance.convertToString(c.getModel()));
                }
            }

            private void generateConfiguration(List<String> solution) {
                System.out.println(solution);
            }
        };
        consumer.start();
        LongRunningWrapper.runMethod(generator, monitor);
        consumer.interrupt();
    }


    /**
     * Get all combinations of the specified parameters.
     *
     * @param parameters
     * @return
     */
    public static Set<Set<String>> getConfigurations(Set<ExpressionConfigurationConstant> parameters) {
        if(parameters == null) {
            throw new IllegalArgumentException("The parameters cannot be null");
        }

        List<String> parametersList = new ArrayList<>();

        for(ExpressionConfigurationConstant option : parameters) {
            parametersList.add(option.getName());
        }

        Set<Set<String>> configurations = new HashSet<>();

        int configurationMaxLength = parameters.size();

        for(int i = 1; i <= configurationMaxLength; i++) {
            Combinations combinations = new Combinations(configurationMaxLength, i);

            for (int[] combination : combinations) {
                Set<String> configuration = new HashSet<>();

                for(int aCombination : combination) {
                    configuration.add(parametersList.get(aCombination));
                }

                configurations.add(configuration);
            }

        }

        configurations.add(new HashSet<>());
        return configurations;
    }

//    /**
//     * Get the next configurations based on the available ones and the parameters that should be considered. First, it
//     * returns the configuration where all options are set to false. Next, it returns the next smallest configuration that
//     * has all parameters under consideration set to true. Finally, it return a random configuration.
//     *
//     * @param configurations
//     * @param considerParameters
//     * @return
//     */
//    public static Set<String> getNextConfiguration(Set<Set<String>> configurations, Set<String> considerParameters) {
//        if(configurations == null) {
//            throw new IllegalArgumentException("The configurations cannot be null");
//        }
//
//        if(considerParameters == null) {
//            throw new IllegalArgumentException("The considerParameters cannot be null");
//        }
//
//        if (configurations.isEmpty()) {
//            return null;
//        }
//
//        // TODO we always start with all configurations set to false. Should we start with all True?
//        if (configurations.contains(new HashSet<String>())) {
//            Set<String> configuration = new HashSet<>();
//            configurations.remove(configuration);
//
//            return configuration;
//        }
//
//        if(!considerParameters.isEmpty()) {// TODO check if this is needed
//            List<Set<String>> possibleConfigurations = new ArrayList<>();
//
//            for (Set<String> configuration : configurations) {
//                Set<String> possibleConsiderParametersConfiguration = new HashSet<>(considerParameters);
//                possibleConsiderParametersConfiguration.retainAll(configuration);
//
//                if(possibleConsiderParametersConfiguration.equals(considerParameters)) {
//                    possibleConfigurations.add(configuration);
//                }
//            }
//
//            if (!possibleConfigurations.isEmpty()) {
//                // We get the smallest configuration that contains all parameter under consideration
//                int minLength = Integer.MAX_VALUE;
//                int minLengthPosition = -1;
//
//                for (int i = 0; i < possibleConfigurations.size(); i++) {
//                    if (possibleConfigurations.get(i).size() < minLength) {
//                        minLength = possibleConfigurations.get(i).size();
//                        minLengthPosition = i;
//                    }
//                }
//
//                configurations.remove(possibleConfigurations.get(minLengthPosition));
//
//                return possibleConfigurations.get(minLengthPosition);
//            }
//        }
//
//        // Pick a random configuration
//        Set<String> randomConfiguration = configurations.iterator().next();
//        configurations.remove(randomConfiguration);
//
//        return randomConfiguration;
//    }

}

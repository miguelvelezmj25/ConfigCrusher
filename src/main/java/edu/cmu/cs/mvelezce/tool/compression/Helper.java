package edu.cmu.cs.mvelezce.tool.compression;

import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.editing.AdvancedNodeCreator;
import de.ovgu.featureide.fm.core.filter.AbstractFeatureFilter;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;
import de.ovgu.featureide.fm.core.job.monitor.IMonitor;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;
import org.prop4j.Node;
import org.prop4j.analyses.PairWiseConfigurationGenerator;
import org.prop4j.solver.SatInstance;

import java.util.List;

/**
 * Created by mvelezce on 4/2/17.
 */
public class Helper {

    public static void incling() throws InterruptedException {
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

        Helper.exec(satInstance, generator, new NullMonitor());

    }

    protected static void exec(final SatInstance satInstance, final PairWiseConfigurationGenerator generator, IMonitor monitor) {
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
//                System.out.println(solution);
            }
        };
        consumer.start();
        LongRunningWrapper.runMethod(generator, monitor);
        consumer.interrupt();
    }


}

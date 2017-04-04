package edu.cmu.cs.mvelezce.analysis;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionConfigurationConstant;
import org.apache.commons.math3.util.Combinations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for performing analyses.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class Helper {

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

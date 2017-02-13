package edu.cmu.cs.mvelezce.analysis;

import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import org.apache.commons.math3.util.Combinations;

import java.util.*;

/**
 * TODO
 */
public class Helper {

    /**
     * TODO
     * @param parameters
     * @return
     */
    public static Set<Set<String>> getConfigurations(Set<String> parameters) {
        List<String> parametersList= new ArrayList<>(parameters);
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

    /**
     * TODO
     * @param configurations
     * @return
     */
    public static Set<String> getNextConfiguration(Set<Set<String>> configurations, Set<String> considerParameters) {
        if (configurations.isEmpty()) {
            return null;
        }

        // TODO we always start with all configurations set to false. Should we start with all True?
        if (configurations.contains(new HashSet<String>())) {
            Set<String> configuration = new HashSet<>();
            configurations.remove(configuration);

            return configuration;
        }

        if(!considerParameters.isEmpty()) {// TODO check if this is needed
            List<Set<String>> possibleConfigurations = new ArrayList<>();

            for (Set<String> configuration : configurations) {
                Set<String> possibleConsiderParametersConfiguration = new HashSet<>(considerParameters);
                possibleConsiderParametersConfiguration.retainAll(configuration);

                if(possibleConsiderParametersConfiguration.equals(considerParameters)) {
                    possibleConfigurations.add(configuration);
                }
            }

            if (!possibleConfigurations.isEmpty()) {
                // We get the smallest configuration that contains all parameter under consideration
                int minLength = Integer.MAX_VALUE;
                int minLengthPosition = -1;

                for (int i = 0; i < possibleConfigurations.size(); i++) {
                    if (possibleConfigurations.get(i).size() < minLength) {
                        minLength = possibleConfigurations.get(i).size();
                        minLengthPosition = i;
                    }
                }

                configurations.remove(possibleConfigurations.get(minLengthPosition));

                return possibleConfigurations.get(minLengthPosition);
            }
        }

        // Pick a random configuration
        Set<String> randomConfiguration = configurations.iterator().next();
        configurations.remove(randomConfiguration);

        return randomConfiguration;
    }

}

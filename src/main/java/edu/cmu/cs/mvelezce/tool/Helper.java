package edu.cmu.cs.mvelezce.tool;

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
    public static Set<Set<String>> getConfigurations(Set<String> parameters) {
        if(parameters == null) {
            throw new IllegalArgumentException("The parameters cannot be null");
        }

        List<String> parametersList = new ArrayList<>(parameters);
        Set<Set<String>> configurations = new HashSet<>();
        int configurationMaxLength = parameters.size();

        for(int i = 1; i <= configurationMaxLength; i++) {
            Combinations combinations = new Combinations(configurationMaxLength, i);

            for (int[] combination : combinations) {
                Set<String> configuration = new HashSet<>();

                for(int element : combination) {
                    configuration.add(parametersList.get(element));
                }

                configurations.add(configuration);
            }

        }

        configurations.add(new HashSet<>());
        return configurations;
    }

}

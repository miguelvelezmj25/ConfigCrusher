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

            for(Iterator<int[]> it = combinations.iterator(); it.hasNext();) {
                Set<String> configuration = new HashSet<>();
                int[] combination = it.next();

                for(int j = 0; j < combination.length; j++) {
                    configuration.add(parametersList.get(combination[j]));
                }

                configurations.add(configuration);
            }

        }

         return configurations;
    }

}

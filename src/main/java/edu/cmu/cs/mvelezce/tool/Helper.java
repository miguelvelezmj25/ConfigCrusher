package edu.cmu.cs.mvelezce.tool;

import org.apache.commons.math3.util.Combinations;

import java.util.*;

// TODO what is this class helping for?

/**
 * Incling class for performing analyses.
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

            for(int[] combination : combinations) {
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

//    public static void removeSampledConfigurations(String name, Set<Set<String>> configurations) throws IOException, InterruptedException {
//        // arguments
//        String[] args = new String[0];
//
//        Executor executor = new BruteForceExecutor(name);
//        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
//
//        for(PerformanceEntryStatistic entry : performanceEntries) {
//            configurations.remove(entry.getConfiguration());
//        }
//    }

    public static Set<Set<String>> getRandomConfigs(List<String> options, int size) {
        Set<Set<String>> configs = new HashSet<>(size);
        int length = options.size();

        Random numOfOpts = new Random();

        while(configs.size() < size) {
            int num = numOfOpts.nextInt(length + 1);
            Set<String> config = new HashSet<>(num);
            Random opts = new Random();

            while(config.size() < num) {
                int index = opts.nextInt(length);
                String opt = options.get(index);

                if(!config.contains(opt)) {
                    config.add(opt);
                }
            }

            if(!configs.contains(config)) {
                configs.add(config);
            }
        }

        return configs;
    }

    public static Set<Set<String>> mergeConfigs(Set<Set<String>> configs1, Set<Set<String>> configs2) {
        Set<Set<String>> configs = new HashSet<>(configs1);

        for(Set<String> config : configs2) {
            if(!configs.contains(config)) {
                configs.add(config);
            }
        }

        return configs;
    }

}

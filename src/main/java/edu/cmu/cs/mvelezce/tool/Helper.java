package edu.cmu.cs.mvelezce.tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.math3.util.Combinations;

// TODO what is this class helping for?

/**
 * Incling class for performing analyses.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class Helper {

  /**
   * Get all combinations of the specified options.
   */
  public static Set<Set<String>> getConfigurations(Set<String> options) {
    if (options == null) {
      throw new IllegalArgumentException("The options passed cannot be null");
    }

    List<String> optionsList = new ArrayList<>(options);
    Set<Set<String>> configs = new HashSet<>();
    int configMaxLength = options.size();

    for (int i = 1; i <= configMaxLength; i++) {
      Combinations combos = new Combinations(configMaxLength, i);

      for (int[] combination : combos) {
        Set<String> config = new HashSet<>();

        for (int element : combination) {
          config.add(optionsList.get(element));
        }

        configs.add(config);
      }

    }

    configs.add(new HashSet<>());
    return configs;
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

  public static Set<Set<String>> getRandomConfigs(List<String> options, int size,
      Set<Set<String>> excludeConfigs) {
    Set<Set<String>> configs = new HashSet<>(size);
    int length = options.size();

    Random numOfOpts = new Random();

    while (configs.size() < size) {
      int num = numOfOpts.nextInt(length + 1);
      Set<String> config = new HashSet<>(num);
      Random opts = new Random();

      while (config.size() < num) {
        int index = opts.nextInt(length);
        String opt = options.get(index);

        if (!config.contains(opt)) {
          config.add(opt);
        }
      }

      if (!configs.contains(config) && !excludeConfigs.contains(config)) {
        configs.add(config);
      }
    }

    return configs;
  }

  public static Set<Set<String>> mergeConfigs(Set<Set<String>> configs1,
      Set<Set<String>> configs2) {
    Set<Set<String>> configs = new HashSet<>(configs1);

    for (Set<String> config : configs2) {
      if (!configs.contains(config)) {
        configs.add(config);
      }
    }

    return configs;
  }

}

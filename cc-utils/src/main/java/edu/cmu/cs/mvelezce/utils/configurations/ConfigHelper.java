package edu.cmu.cs.mvelezce.utils.configurations;

import org.apache.commons.math3.util.Combinations;

import java.util.*;

/**
 * Helping class for performing analyses.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class ConfigHelper {

  /** Get all combinations of the specified options. */
  public static Set<Set<String>> getConfigurations(Collection<String> options) {
    if (options == null) {
      throw new IllegalArgumentException("The options passed cannot be null");
    }

    Set<Set<String>> configs = new HashSet<>(ConfigHelper.getCombinations(options));
    configs.add(new HashSet<>());

    return configs;
  }

  public static Set<Set<String>> getCombinations(Collection<String> options) {
    if (options == null) {
      throw new IllegalArgumentException("The options passed cannot be null");
    }

    List<String> optionsList = new ArrayList<>(options);
    Set<Set<String>> combos = new HashSet<>();
    int comboMaxLength = options.size();

    for (int i = 1; i <= comboMaxLength; i++) {
      Combinations currentCombos = new Combinations(comboMaxLength, i);

      for (int[] currentCombo : currentCombos) {
        Set<String> combo = new HashSet<>();

        for (int element : currentCombo) {
          combo.add(optionsList.get(element));
        }

        combos.add(combo);
      }
    }

    return combos;
  }

  //    public static void removeSampledConfigurations(String name, Set<Set<String>> configurations)
  // throws IOException, InterruptedException {
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
    return getRandomConfigs(options, size, new HashSet<>());
  }

  public static Set<Set<String>> getRandomConfigs(
      List<String> options, int size, Set<Set<String>> excludeConfigs) {
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
        config.add(opt);
      }

      if (!configs.contains(config) && !excludeConfigs.contains(config)) {
        configs.add(config);
      }
    }

    return configs;
  }

  public static Set<Set<String>> mergeConfigs(
      Set<Set<String>> configs1, Set<Set<String>> configs2) {
    Set<Set<String>> configs = new HashSet<>(configs1);
    configs.addAll(configs2);

    return configs;
  }
}

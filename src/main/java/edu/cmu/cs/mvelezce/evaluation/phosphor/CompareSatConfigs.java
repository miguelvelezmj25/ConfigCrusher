package edu.cmu.cs.mvelezce.evaluation.phosphor;

import java.util.Set;

public class CompareSatConfigs {

  static boolean equalSatConfigs(Set<Set<Set<String>>> approach1Configs,
      Set<Set<Set<String>>> approach2Configs) {
    return approach1Configs.equals(approach2Configs);
  }

  static boolean containsConfigInSatConfigs(Set<Set<String>> configs, Set<Set<Set<String>>> satConfigs) {
    return satConfigs.containsAll(configs);
  }

}

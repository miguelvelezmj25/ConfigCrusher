package edu.cmu.cs.mvelezce.model.influence;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;

import java.util.*;

public class LocalPerformanceInfluenceModel extends LocalPerformanceModel<Set<String>> {

  public LocalPerformanceInfluenceModel(
      UUID region,
      LinkedHashMap<Set<String>, Double> influenceModel,
      LinkedHashMap<Set<String>, String> influenceModelHumanReadable) {
    super(
        region,
        influenceModel,
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        influenceModelHumanReadable,
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>());
  }

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    double time = 0;

    for (Map.Entry<Set<String>, Double> entry : this.getModel().entrySet()) {
      if (config.containsAll(entry.getKey())) {
        time += entry.getValue();
      }
    }

    return time;
  }
}

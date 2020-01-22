package edu.cmu.cs.mvelezce.model.influence;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

public class LocalPerformanceInfluenceModel extends LocalPerformanceModel<Set<String>> {

  public LocalPerformanceInfluenceModel(
      UUID region, LinkedHashMap<Set<String>, Double> influenceModel) {
    super(
        region,
        influenceModel,
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>());
  }
}

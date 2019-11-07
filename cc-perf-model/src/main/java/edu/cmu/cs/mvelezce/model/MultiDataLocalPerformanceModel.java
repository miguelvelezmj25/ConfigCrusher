package edu.cmu.cs.mvelezce.model;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MultiDataLocalPerformanceModel<T> {

  private final UUID region;
  private final Map<T, Set<Long>> model;

  public MultiDataLocalPerformanceModel(UUID region, Map<T, Set<Long>> model) {
    this.region = region;
    this.model = model;
  }
}

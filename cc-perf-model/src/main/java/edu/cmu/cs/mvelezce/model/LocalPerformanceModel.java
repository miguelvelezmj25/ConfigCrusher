package edu.cmu.cs.mvelezce.model;

import java.util.Map;
import java.util.UUID;

public class LocalPerformanceModel<T> {

  private final UUID region;
  private final Map<T, Long> model;

  public LocalPerformanceModel(UUID region, Map<T, Long> model) {
    this.region = region;
    this.model = model;
  }
}

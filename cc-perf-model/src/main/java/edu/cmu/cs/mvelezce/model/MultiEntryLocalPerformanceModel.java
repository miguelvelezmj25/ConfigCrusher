package edu.cmu.cs.mvelezce.model;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MultiEntryLocalPerformanceModel<T> {

  private final UUID region;
  private final Map<T, Set<Long>> model;

  public MultiEntryLocalPerformanceModel(UUID region, Map<T, Set<Long>> model) {
    this.region = region;
    this.model = model;
  }

  public UUID getRegion() {
    return region;
  }

  public Map<T, Set<Long>> getModel() {
    return model;
  }
}

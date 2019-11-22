package edu.cmu.cs.mvelezce.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerformanceModel<T> implements IPerformanceModel<T> {

  private final Set<LocalPerformanceModel<T>> localModels;

  // Dummy constructor for jackson xml
  private PerformanceModel() {
    this.localModels = new HashSet<>();
  }

  public PerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    this.localModels = localModels;
  }

  public Set<LocalPerformanceModel<T>> getLocalModels() {
    return localModels;
  }

  @Override
  public long evaluate(Set<String> config, List<String> options) {
    long time = 0;

    for (LocalPerformanceModel<T> localModel : this.localModels) {
      time += localModel.evaluate(config, options);
    }

    return time;
  }
}

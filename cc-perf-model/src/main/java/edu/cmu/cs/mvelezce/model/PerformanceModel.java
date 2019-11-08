package edu.cmu.cs.mvelezce.model;

import java.util.Set;

public class PerformanceModel<T> {

  private final Set<LocalPerformanceModel<T>> localModels;

  public PerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    this.localModels = localModels;
  }

  public Set<LocalPerformanceModel<T>> getLocalModels() {
    return localModels;
  }
}

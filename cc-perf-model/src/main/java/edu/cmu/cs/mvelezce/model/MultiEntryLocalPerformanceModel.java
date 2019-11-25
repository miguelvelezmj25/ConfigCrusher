package edu.cmu.cs.mvelezce.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MultiEntryLocalPerformanceModel<T> {

  private final UUID region;
  private final Map<T, Set<Double>> model;
  private final Map<T, Set<Double>> modelToMins;
  private final Map<T, Set<Double>> modelToMaxs;
  private final Map<T, Set<Double>> modelToDiffs;
  private final Map<T, Set<Double>> modelToSampleVariances;
  private final Map<T, Set<List<Double>>> modelToConfidenceIntervals;

  public MultiEntryLocalPerformanceModel(
      UUID region,
      Map<T, Set<Double>> model,
      Map<T, Set<Double>> modelToMins,
      Map<T, Set<Double>> modelToMaxs,
      Map<T, Set<Double>> modelToDiffs,
      Map<T, Set<Double>> modelToSampleVariances,
      Map<T, Set<List<Double>>> modelToConfidenceIntervals) {
    this.region = region;
    this.model = model;
    this.modelToMins = modelToMins;
    this.modelToMaxs = modelToMaxs;
    this.modelToDiffs = modelToDiffs;
    this.modelToSampleVariances = modelToSampleVariances;
    this.modelToConfidenceIntervals = modelToConfidenceIntervals;
  }

  public UUID getRegion() {
    return region;
  }

  public Map<T, Set<Double>> getModel() {
    return model;
  }

  public Map<T, Set<Double>> getModelToMins() {
    return modelToMins;
  }

  public Map<T, Set<Double>> getModelToMaxs() {
    return modelToMaxs;
  }

  public Map<T, Set<Double>> getModelToDiffs() {
    return modelToDiffs;
  }

  public Map<T, Set<Double>> getModelToSampleVariances() {
    return modelToSampleVariances;
  }

  public Map<T, Set<List<Double>>> getModelToConfidenceIntervals() {
    return modelToConfidenceIntervals;
  }
}

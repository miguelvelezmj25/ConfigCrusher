package edu.cmu.cs.mvelezce.eval.java.models;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.util.*;

public abstract class ModelsEvaluation<T> {

  private final String programName;

  public ModelsEvaluation(String programName) {
    this.programName = programName;
  }

  public void compare(Set<PerformanceModel<T>> models) {
    Set<Map<UUID, Map<T, Double>>> regionsToPerfModels = this.getRegionsToPerfModels(models);
    Set<UUID> regions = this.getRegions(regionsToPerfModels);

    throw new UnsupportedOperationException("implement");
  }

  private Set<UUID> getRegions(Set<Map<UUID, Map<T, Double>>> regionsToPerfModels) {
    Set<UUID> regions = new HashSet<>();

    for (Map<UUID, Map<T, Double>> entry : regionsToPerfModels) {
      regions.addAll(entry.keySet());
    }

    return regions;
  }

  private Set<Map<UUID, Map<T, Double>>> getRegionsToPerfModels(Set<PerformanceModel<T>> models) {
    Set<Map<UUID, Map<T, Double>>> regionsToPerfModels = new HashSet<>();

    for (PerformanceModel<T> model : models) {
      Set<LocalPerformanceModel<T>> localModels = model.getLocalModels();
      Map<UUID, Map<T, Double>> thisModelRegionsToPerfModels =
          this.getThisModelRegionsToPerfModels(localModels);
      regionsToPerfModels.add(thisModelRegionsToPerfModels);
    }

    return regionsToPerfModels;
  }

  private Map<UUID, Map<T, Double>> getThisModelRegionsToPerfModels(
      Set<LocalPerformanceModel<T>> localModels) {
    Map<UUID, Map<T, Double>> regionsToPerfModels = new HashMap<>();

    for (LocalPerformanceModel<T> localModel : localModels) {
      UUID region = localModel.getRegion();
      Map<T, Double> perfModel = localModel.getModel();

      regionsToPerfModels.put(region, perfModel);
    }

    return regionsToPerfModels;
  }
}

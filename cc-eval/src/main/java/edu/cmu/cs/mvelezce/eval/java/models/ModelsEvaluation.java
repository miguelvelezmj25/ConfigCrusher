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
    Map<UUID, Set<T>> allModelEntries = this.getAllModelEntries(regions, regionsToPerfModels);
    Map<UUID, Map<T, List<Double>>> comparedModels =
        this.comparePerfs(allModelEntries, regionsToPerfModels);

    throw new UnsupportedOperationException("implement");
  }

  private Map<UUID, Map<T, List<Double>>> comparePerfs(
      Map<UUID, Set<T>> allModelEntries, Set<Map<UUID, Map<T, Double>>> regionsToPerfModels) {
    Map<UUID, Map<T, List<Double>>> comparedModels = new HashMap<>();

    for (Map.Entry<UUID, Set<T>> entry : allModelEntries.entrySet()) {
      Map<T, List<Double>> comparisonMap = new HashMap<>();

      for (T row : entry.getValue()) {
        comparisonMap.put(row, new ArrayList<>());
      }

      comparedModels.put(entry.getKey(), comparisonMap);
    }

    for (Map<UUID, Map<T, Double>> oneRegionsToPerfModels : regionsToPerfModels) {
      for (Map.Entry<UUID, Map<T, Double>> entry : oneRegionsToPerfModels.entrySet()) {
        Map<T, List<Double>> comparisonMap = comparedModels.get(entry.getKey());

        for (Map.Entry<T, Double> perfEntry : entry.getValue().entrySet()) {
          comparisonMap.get(perfEntry.getKey()).add(perfEntry.getValue());
        }
      }
    }

    return comparedModels;
  }

  private Map<UUID, Set<T>> getAllModelEntries(
      Set<UUID> regions, Set<Map<UUID, Map<T, Double>>> regionsToPerfModels) {
    Map<UUID, Set<T>> entries = new HashMap<>();

    for (UUID region : regions) {
      entries.put(region, new HashSet<>());
    }

    for (Map<UUID, Map<T, Double>> oneRegionsToPerfModels : regionsToPerfModels) {

      for (Map.Entry<UUID, Map<T, Double>> entry : oneRegionsToPerfModels.entrySet()) {
        Set<T> modelEntries = entry.getValue().keySet();
        entries.get(entry.getKey()).addAll(modelEntries);
      }
    }

    return entries;
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

package edu.cmu.cs.mvelezce.eval.java.models;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public abstract class ModelsEvaluation<T> {

  protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");
  protected static final String DOT_CSV = ".csv";
  protected static final String COMPARISON_ROOT = "comparison";

  private final String programName;
  private final Collection<String> options;
  private final double diffThreshold;
  private final double perfIntensiveThreshold;

  public ModelsEvaluation(
      String programName,
      Collection<String> options,
      double diffThreshold,
      double perfIntensiveThreshold) {
    this.programName = programName;
    this.options = options;
    this.diffThreshold = diffThreshold;
    this.perfIntensiveThreshold = perfIntensiveThreshold;
  }

  public void compare(Set<PerformanceModel<T>> models) throws IOException {
    System.err.println(
        "We are always comparing 2 models. Change the parameters to take the two models");
    Set<Map<UUID, Map<T, Double>>> regionsToPerfModels = this.getRegionsToPerfModels(models);
    Set<UUID> regions = this.getRegions(regionsToPerfModels);
    Map<UUID, Set<T>> allModelEntries = this.getAllModelEntries(regions, regionsToPerfModels);
    Map<UUID, Map<T, List<Double>>> comparedModels =
        this.comparePerfs(allModelEntries, regionsToPerfModels);
    this.saveComparedModels(comparedModels);
  }

  protected abstract void saveComparedModels(Map<UUID, Map<T, List<Double>>> comparedModels)
      throws IOException;

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

  public Collection<String> getOptions() {
    return options;
  }

  public String getProgramName() {
    return programName;
  }

  public double getDiffThreshold() {
    return diffThreshold;
  }

  public double getPerfIntensiveThreshold() {
    return perfIntensiveThreshold;
  }
}

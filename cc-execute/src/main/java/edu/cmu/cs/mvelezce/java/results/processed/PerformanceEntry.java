package edu.cmu.cs.mvelezce.java.results.processed;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class PerformanceEntry {

  private final Set<String> configuration;
  private final Map<UUID, Double> regionsToPerf;
  private final Map<UUID, Double> regionsToMin;
  private final Map<UUID, Double> regionsToMax;
  private final Map<UUID, Double> regionsToDiff;
  private final Map<UUID, Double> regionsToSampleVariance;
  private final Map<UUID, Pair<Double, Double>> regionsToConfidenceInterval;
  private final Map<UUID, String> regionsToPerfHumanReadable;
  private final Map<UUID, String> regionsToMinHumanReadable;
  private final Map<UUID, String> regionsToMaxHumanReadable;
  private final Map<UUID, String> regionsToDiffHumanReadable;
  private final Map<UUID, String> regionsToSampleVarianceHumanReadable;
  private final Map<UUID, Pair<String, String>> regionsToConfidenceIntervalsHumanReadable;

  // Dummy constructor for jackson xml
  private PerformanceEntry() {
    this.configuration = new HashSet<>();
    this.regionsToPerf = new HashMap<>();
    this.regionsToMin = new HashMap<>();
    this.regionsToMax = new HashMap<>();
    this.regionsToDiff = new HashMap<>();
    this.regionsToSampleVariance = new HashMap<>();
    this.regionsToConfidenceInterval = new HashMap<>();
    this.regionsToPerfHumanReadable = new HashMap<>();
    this.regionsToMinHumanReadable = new HashMap<>();
    this.regionsToMaxHumanReadable = new HashMap<>();
    this.regionsToDiffHumanReadable = new HashMap<>();
    this.regionsToSampleVarianceHumanReadable = new HashMap<>();
    this.regionsToConfidenceIntervalsHumanReadable = new HashMap<>();
  }

  public PerformanceEntry(
      Set<String> configuration,
      Map<UUID, Double> regionsToPerf,
      Map<UUID, Double> regionsToMin,
      Map<UUID, Double> regionsToMax,
      Map<UUID, Double> regionsToDiff,
      Map<UUID, Double> regionsToSampleVariance,
      Map<UUID, Pair<Double, Double>> regionsToConfidenceInterval,
      Map<UUID, String> regionsToPerfHumanReadable,
      Map<UUID, String> regionsToMinHumanReadable,
      Map<UUID, String> regionsToMaxHumanReadable,
      Map<UUID, String> regionsToDiffHumanReadable,
      Map<UUID, String> regionsToSampleVarianceHumanReadable,
      Map<UUID, Pair<String, String>> regionsToConfidenceIntervalsHumanReadable) {
    this.configuration = configuration;
    this.regionsToPerf = regionsToPerf;
    this.regionsToMin = regionsToMin;
    this.regionsToMax = regionsToMax;
    this.regionsToDiff = regionsToDiff;
    this.regionsToSampleVariance = regionsToSampleVariance;
    this.regionsToConfidenceInterval = regionsToConfidenceInterval;
    this.regionsToPerfHumanReadable = regionsToPerfHumanReadable;
    this.regionsToMinHumanReadable = regionsToMinHumanReadable;
    this.regionsToMaxHumanReadable = regionsToMaxHumanReadable;
    this.regionsToDiffHumanReadable = regionsToDiffHumanReadable;
    this.regionsToSampleVarianceHumanReadable = regionsToSampleVarianceHumanReadable;
    this.regionsToConfidenceIntervalsHumanReadable = regionsToConfidenceIntervalsHumanReadable;
  }

  public Set<String> getConfiguration() {
    return configuration;
  }

  public Map<UUID, Double> getRegionsToPerf() {
    return regionsToPerf;
  }

  public Map<UUID, Double> getRegionsToMin() {
    return regionsToMin;
  }

  public Map<UUID, Double> getRegionsToMax() {
    return regionsToMax;
  }

  public Map<UUID, Double> getRegionsToDiff() {
    return regionsToDiff;
  }

  public Map<UUID, Double> getRegionsToSampleVariance() {
    return regionsToSampleVariance;
  }

  public Map<UUID, Pair<Double, Double>> getRegionsToConfidenceInterval() {
    return regionsToConfidenceInterval;
  }

  public Map<UUID, String> getRegionsToPerfHumanReadable() {
    return regionsToPerfHumanReadable;
  }

  public Map<UUID, String> getRegionsToMinHumanReadable() {
    return regionsToMinHumanReadable;
  }

  public Map<UUID, String> getRegionsToMaxHumanReadable() {
    return regionsToMaxHumanReadable;
  }

  public Map<UUID, String> getRegionsToDiffHumanReadable() {
    return regionsToDiffHumanReadable;
  }

  public Map<UUID, String> getRegionsToSampleVarianceHumanReadable() {
    return regionsToSampleVarianceHumanReadable;
  }

  public Map<UUID, Pair<String, String>> getRegionsToConfidenceIntervalsHumanReadable() {
    return regionsToConfidenceIntervalsHumanReadable;
  }
}

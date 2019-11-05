package edu.cmu.cs.mvelezce.java.results.processed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PerformanceEntry {

  private final Set<String> configuration;
  private final Map<String, Long> regionsToPerf;
  private final Map<String, Long> regionsToMin;
  private final Map<String, Long> regionsToMax;
  private final Map<String, Long> regionsToDiff;
  private final Map<String, String> regionsToPerfHumanReadable;
  private final Map<String, String> regionsToMinHumanReadable;
  private final Map<String, String> regionsToMaxHumanReadable;
  private final Map<String, String> regionsToDiffHumanReadable;

  // Dummy constructor for jackson xml
  public PerformanceEntry() {
    this.configuration = new HashSet<>();
    this.regionsToPerf = new HashMap<>();
    this.regionsToMin = new HashMap<>();
    this.regionsToMax = new HashMap<>();
    this.regionsToDiff = new HashMap<>();
    this.regionsToPerfHumanReadable = new HashMap<>();
    this.regionsToMinHumanReadable = new HashMap<>();
    this.regionsToMaxHumanReadable = new HashMap<>();
    this.regionsToDiffHumanReadable = new HashMap<>();
  }

  public PerformanceEntry(
      Set<String> configuration,
      Map<String, Long> regionsToPerf,
      Map<String, Long> regionsToMin,
      Map<String, Long> regionsToMax,
      Map<String, Long> regionsToDiff,
      Map<String, String> regionsToPerfHumanReadable,
      Map<String, String> regionsToMinHumanReadable,
      Map<String, String> regionsToMaxHumanReadable,
      Map<String, String> regionsToDiffHumanReadable) {
    this.configuration = configuration;
    this.regionsToPerf = regionsToPerf;
    this.regionsToMin = regionsToMin;
    this.regionsToMax = regionsToMax;
    this.regionsToDiff = regionsToDiff;
    this.regionsToPerfHumanReadable = regionsToPerfHumanReadable;
    this.regionsToMinHumanReadable = regionsToMinHumanReadable;
    this.regionsToMaxHumanReadable = regionsToMaxHumanReadable;
    this.regionsToDiffHumanReadable = regionsToDiffHumanReadable;
  }

  public Set<String> getConfiguration() {
    return configuration;
  }

  public Map<String, Long> getRegionsToPerf() {
    return regionsToPerf;
  }

  public Map<String, Long> getRegionsToMax() {
    return regionsToMax;
  }

  public Map<String, Long> getRegionsToDiff() {
    return regionsToDiff;
  }

  public Map<String, Long> getRegionsToMin() {
    return regionsToMin;
  }

  public Map<String, String> getRegionsToMinHumanReadable() {
    return regionsToMinHumanReadable;
  }

  public Map<String, String> getRegionsToMaxHumanReadable() {
    return regionsToMaxHumanReadable;
  }

  public Map<String, String> getRegionsToDiffHumanReadable() {
    return regionsToDiffHumanReadable;
  }

  public Map<String, String> getRegionsToPerfHumanReadable() {
    return regionsToPerfHumanReadable;
  }
}

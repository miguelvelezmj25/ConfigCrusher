package edu.cmu.cs.mvelezce.java.results.processed;

import java.util.*;

public class PerformanceEntry {

  private final Set<String> configuration;
  private final Map<UUID, Long> regionsToPerf;
  private final Map<UUID, Long> regionsToMin;
  private final Map<UUID, Long> regionsToMax;
  private final Map<UUID, Long> regionsToDiff;
  private final Map<UUID, String> regionsToPerfHumanReadable;
  private final Map<UUID, String> regionsToMinHumanReadable;
  private final Map<UUID, String> regionsToMaxHumanReadable;
  private final Map<UUID, String> regionsToDiffHumanReadable;

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
      Map<UUID, Long> regionsToPerf,
      Map<UUID, Long> regionsToMin,
      Map<UUID, Long> regionsToMax,
      Map<UUID, Long> regionsToDiff,
      Map<UUID, String> regionsToPerfHumanReadable,
      Map<UUID, String> regionsToMinHumanReadable,
      Map<UUID, String> regionsToMaxHumanReadable,
      Map<UUID, String> regionsToDiffHumanReadable) {
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

  public Map<UUID, Long> getRegionsToPerf() {
    return regionsToPerf;
  }

  public Map<UUID, Long> getRegionsToMax() {
    return regionsToMax;
  }

  public Map<UUID, Long> getRegionsToDiff() {
    return regionsToDiff;
  }

  public Map<UUID, Long> getRegionsToMin() {
    return regionsToMin;
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

  public Map<UUID, String> getRegionsToPerfHumanReadable() {
    return regionsToPerfHumanReadable;
  }
}

package edu.cmu.cs.mvelezce.java.results.processed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** A single execution of a configuration with perf times of each region */
public class PerfExecution {

  private final Set<String> configuration;
  private final Map<String, Long> regionsToPerf;

  // Dummy constructor for jackson xml
  private PerfExecution() {
    this.configuration = new HashSet<>();
    this.regionsToPerf = new HashMap<>();
  }

  public PerfExecution(Set<String> configuration, Map<String, Long> regionsToPerf) {
    this.configuration = configuration;
    this.regionsToPerf = regionsToPerf;
  }

  public Set<String> getConfiguration() {
    return configuration;
  }

  public Map<String, Long> getRegionsToPerf() {
    return regionsToPerf;
  }
}

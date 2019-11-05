package edu.cmu.cs.mvelezce.java.results.processed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessedPerfExecution {

  private final Set<String> configuration;
  private final Map<String, Long> regionsToPerf;

  // Dummy constructor for jackson xml
  public ProcessedPerfExecution() {
    this.configuration = new HashSet<>();
    this.regionsToPerf = new HashMap<>();
  }

  public ProcessedPerfExecution(Set<String> configuration, Map<String, Long> regionsToPerf) {
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

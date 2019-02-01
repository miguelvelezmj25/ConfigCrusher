package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.List;
import java.util.Set;

public class ConfigToTraceInfo {

  private Set<String> config;
  private List<String> trace;

  // Dummy constructor needed for jackson xml
  private ConfigToTraceInfo() {  }

  public ConfigToTraceInfo(Set<String> config, List<String> trace) {
    this.config = config;
    this.trace = trace;
  }

  public Set<String> getConfig() {
    return config;
  }

  public List<String> getTrace() {
    return trace;
  }
}

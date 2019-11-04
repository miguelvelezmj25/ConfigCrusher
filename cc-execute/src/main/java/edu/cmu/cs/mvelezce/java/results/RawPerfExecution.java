package edu.cmu.cs.mvelezce.java.results;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RawPerfExecution {

  private final Set<String> configuration;
  private final List<String> trace;

  // Dummy constructor for jackson xml
  public RawPerfExecution() {
    this.configuration = new HashSet<>();
    this.trace = new ArrayList<>();
  }

  public RawPerfExecution(Set<String> configuration, List<String> trace) {
    this.configuration = configuration;
    this.trace = trace;
  }

  public Set<String> getConfiguration() {
    return configuration;
  }

  public List<String> getTrace() {
    return trace;
  }
}

package edu.cmu.cs.mvelezce.java.results.instrumentation.raw;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RawInstrumentPerfExecution {

  private final Set<String> configuration;
  private final List<String> trace;

  // Dummy constructor for jackson xml
  private RawInstrumentPerfExecution() {
    this.configuration = new HashSet<>();
    this.trace = new ArrayList<>();
  }

  public RawInstrumentPerfExecution(Set<String> configuration, List<String> trace) {
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

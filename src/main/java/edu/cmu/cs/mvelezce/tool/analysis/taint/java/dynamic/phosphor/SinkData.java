package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SinkData {

  private final Map<VariabilityContext, Set<Set<String>>> sinkData = new HashMap<>();

  public SinkData() {
  }

  void putIfAbsent(VariabilityContext variabilityContext, Set<Set<String>> executionTaints) {
    this.sinkData.putIfAbsent(variabilityContext, executionTaints);
  }

  Set<Set<String>> getExecutionTaints(VariabilityContext variabilityContext) {
    return this.sinkData.get(variabilityContext);
  }

  public Map<VariabilityContext, Set<Set<String>>> getSinkData() {
    return sinkData;
  }
}

package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SinkData {

  private final Map<ExecVarCtx, Set<Set<String>>> sinkData = new HashMap<>();

  public SinkData() {
  }

  void putIfAbsent(ExecVarCtx execVarCtx, Set<Set<String>> executionTaints) {
    this.sinkData.putIfAbsent(execVarCtx, executionTaints);
  }

  Set<Set<String>> getExecTaints(ExecVarCtx execVarCtx) {
    return this.sinkData.get(execVarCtx);
  }

  public Map<ExecVarCtx, Set<Set<String>>> getSinkData() {
    return sinkData;
  }
}

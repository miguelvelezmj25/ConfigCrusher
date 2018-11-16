package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SinkData {

  private final Map<ExecVarCtx, Set<Set<String>>> sinkData = new HashMap<>();

  void putIfAbsent(ExecVarCtx execVarCtx, Set<Set<String>> executionTaints) {
    this.sinkData.putIfAbsent(execVarCtx, executionTaints);
  }

  Set<Set<String>> getExecTaints(ExecVarCtx execVarCtx) {
    return this.sinkData.get(execVarCtx);
  }

  public Map<ExecVarCtx, Set<Set<String>>> getSinkData() {
    return sinkData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SinkData sinkData1 = (SinkData) o;

    return sinkData.equals(sinkData1.sinkData);
  }

  @Override
  public int hashCode() {
    return sinkData.hashCode();
  }
}

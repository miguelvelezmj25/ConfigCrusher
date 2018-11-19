package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SinkData {

  private final Map<ExecVarCtx, Set<Set<String>>> data = new HashMap<>();

  void putIfAbsent(ExecVarCtx execVarCtx, Set<Set<String>> executionTaints) {
    this.data.putIfAbsent(execVarCtx, executionTaints);
  }

  Set<Set<String>> getExecTaints(ExecVarCtx execVarCtx) {
    return this.data.get(execVarCtx);
  }

  public Map<ExecVarCtx, Set<Set<String>>> getData() {
    return data;
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

    return data.equals(sinkData1.data);
  }

  @Override
  public int hashCode() {
    return data.hashCode();
  }
}

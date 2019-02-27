package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.Map;

public class SinkData {

  private final Map<ExecVarCtx, ExecConfigConstraints> data;

  SinkData(Map<ExecVarCtx, ExecConfigConstraints> data) {
    this.data = data;
  }

//  void putIfAbsent(ExecVarCtx execVarCtx, ExecTaints executionTaints) {
//    this.data.putIfAbsent(execVarCtx, executionTaints);
//  }

//  ExecTaints getExecTaints(ExecVarCtx execVarCtx) {
//    return this.data.get(execVarCtx);
//  }

  public Map<ExecVarCtx, ExecConfigConstraints> getData() {
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

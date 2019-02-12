package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SubtraceLabel {

  static final String LABEL = "LABEL ";

  // TODO should these be strings?
  private final String action;
  private final Deque<SubtraceLabel> ctx;
  private final String decisionLabel;

  private int execCount;

  public SubtraceLabel(String action, Deque<SubtraceLabel> ctx, String decisionLabel) {
    this(action, ctx, decisionLabel, -1);
  }

  public SubtraceLabel(String action, Deque<SubtraceLabel> ctx, String decisionLabel,
      int execCount) {
    this.action = action;
    this.ctx = ctx;
    this.decisionLabel = decisionLabel;
    this.execCount = execCount;
  }

  public Deque<SubtraceLabel> getCtx() {
    return ctx;
  }

  public String getDecisionLabel() {
    return decisionLabel;
  }

  public int getExecCount() {
    return execCount;
  }

  public String getAction() {
    return action;
  }

  public void setExecCount(int execCount) {
    this.execCount = execCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SubtraceLabel that = (SubtraceLabel) o;

    if (!action.equals(that.action)) {
      return false;
    }

    List<SubtraceLabel> ctxAsList = new ArrayList<>(this.ctx);
    List<SubtraceLabel> thatCtxAsList = new ArrayList<>(that.ctx);

    if (!ctxAsList.equals(thatCtxAsList)) {
      return false;
    }

    return decisionLabel.equals(that.decisionLabel);
  }

  @Override
  public int hashCode() {
    int result = action.hashCode();
    result = 31 * result + new ArrayList<>(ctx).hashCode();
    result = 31 * result + decisionLabel.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return LABEL + ctx + action + decisionLabel + "-" + execCount;
//    return LABEL + ctx.size() + " " + action + decisionLabel + "-" + execCount;
  }
}

package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

public class LoggedSubtrace {

  private static final String LABEL = "LABEL";

  // TODO should these be strings?
  private final String action;
  private final SubtraceLabel subtraceLabel;

  public LoggedSubtrace(String action, SubtraceLabel subtraceLabel) {
    this.action = action;
    this.subtraceLabel = subtraceLabel;
  }

  @Override
  public String toString() {
    return LABEL + action + subtraceLabel;
  }
}

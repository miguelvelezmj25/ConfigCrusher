package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

public class LoggedSubtrace {

//  private static final String LABEL = "LABEL";

  // TODO should these be strings?
  private final String action;
  private final DecisionLabel decisionLabel;

  public LoggedSubtrace(String action, DecisionLabel decisionLabel) {
    this.action = action;
    this.decisionLabel = decisionLabel;
  }

  @Override
  public String toString() {
    return /*LABEL + " " +*/ action + " " + decisionLabel;
  }
}

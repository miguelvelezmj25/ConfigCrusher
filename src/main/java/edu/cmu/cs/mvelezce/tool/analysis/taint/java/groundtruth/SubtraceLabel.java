package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

public class SubtraceLabel {

  static final String LABEL = "LABEL";

  // TODO should these be strings?
  private final String action;
  private final String decisionLabel;
  private final int execCount;

  public SubtraceLabel(String action, String decisionLabel, int execCount) {
    this.action = action;
    this.decisionLabel = decisionLabel;
    this.execCount = execCount;
  }

  public String getDecisionLabel() {
    return decisionLabel;
  }

  public String getAction() {
    return action;
  }

  public int getExecCount() {
    return execCount;
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

    if (execCount != that.execCount) {
      return false;
    }
    if (!action.equals(that.action)) {
      return false;
    }
    return decisionLabel.equals(that.decisionLabel);
  }

  @Override
  public int hashCode() {
    int result = action.hashCode();
    result = 31 * result + decisionLabel.hashCode();
    result = 31 * result + execCount;
    return result;
  }

  @Override
  public String toString() {
    return LABEL + action + decisionLabel + ":" + execCount;
  }
}

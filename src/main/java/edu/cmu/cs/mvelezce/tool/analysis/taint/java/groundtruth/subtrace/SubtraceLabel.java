package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

public class SubtraceLabel {

  private final String decisionLabel;
  private final int execCount;

  public SubtraceLabel(String decisionLabel, int execCount) {
    this.decisionLabel = decisionLabel;
    this.execCount = execCount;
  }

  public String getDecisionLabel() {
    return decisionLabel;
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
    return decisionLabel.equals(that.decisionLabel);

  }

  @Override
  public int hashCode() {
    int result = decisionLabel.hashCode();
    result = 31 * result + execCount;
    return result;
  }

  @Override
  public String toString() {
    return decisionLabel + ":" + execCount;
  }
}

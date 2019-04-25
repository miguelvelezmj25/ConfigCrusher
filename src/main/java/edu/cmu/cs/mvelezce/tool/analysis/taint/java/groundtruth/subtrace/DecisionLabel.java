package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

public class DecisionLabel {

  private final String decision;

  public DecisionLabel(String decision) {
    this.decision = decision;
  }

  public String getDecision() {
    return decision;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DecisionLabel that = (DecisionLabel) o;

    return decision.equals(that.decision);
  }

  @Override
  public int hashCode() {
    return decision.hashCode();
  }

  @Override
  public String toString() {
    return decision;
  }

}

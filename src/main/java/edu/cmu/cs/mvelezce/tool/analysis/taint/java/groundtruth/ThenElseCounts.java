package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.Serializable;

public class ThenElseCounts implements Serializable {

  private static final long serialVersionUID = -8677374800530611265L;

  private int thenCount = 0;
  private int elseCount = 0;

  public int getThenCount() {
    return thenCount;
  }

  public void setThenCount(int thenCount) {
    this.thenCount = thenCount;
  }

  public int getElseCount() {
    return elseCount;
  }

  public void setElseCount(int elseCount) {
    this.elseCount = elseCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ThenElseCounts that = (ThenElseCounts) o;

    if (thenCount != that.thenCount) {
      return false;
    }
    return elseCount == that.elseCount;
  }

  @Override
  public int hashCode() {
    int result = thenCount;
    result = 31 * result + elseCount;
    return result;
  }

  @Override
  public String toString() {
    return "Then=" + this.thenCount + ", Else=" + this.elseCount;
  }
}

package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class StackTraceDecision implements Serializable {

  private static final long serialVersionUID = 4363650686966128330L;

  private final List<String> stackTrace;
  private final String decision;

  public StackTraceDecision(String[] stackTrace, String decision) {
    this.stackTrace = Arrays.asList(stackTrace);
    this.decision = decision;
  }

  public List<String> getStackTrace() {
    return stackTrace;
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

    StackTraceDecision that = (StackTraceDecision) o;

    if (!stackTrace.equals(that.stackTrace)) {
      return false;
    }
    return decision.equals(that.decision);
  }

  @Override
  public int hashCode() {
    int result = stackTrace.hashCode();
    result = 31 * result + decision.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Stack trace=" + this.stackTrace + ", Decision=" + this.decision;
  }
}

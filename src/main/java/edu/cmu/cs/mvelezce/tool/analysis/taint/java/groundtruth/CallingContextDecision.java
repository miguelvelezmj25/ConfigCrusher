package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CallingContextDecision implements Serializable {

  private static final long serialVersionUID = 4363650686966128330L;

  private final List<String> callingContext;
  private final String decision;

  public CallingContextDecision(String[] callingContext, String decision) {
    this.callingContext = Arrays.asList(callingContext);
    this.decision = decision;
  }

  public List<String> getCallingContext() {
    return callingContext;
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

    CallingContextDecision that = (CallingContextDecision) o;

    if (!callingContext.equals(that.callingContext)) {
      return false;
    }
    return decision.equals(that.decision);
  }

  @Override
  public int hashCode() {
    int result = callingContext.hashCode();
    result = 31 * result + decision.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Calling context=" + this.callingContext + ", Decision=" + this.decision;
  }
}

package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashSet;
import java.util.Set;

public class BranchCoverageLogger {

  public static void main(String[] args) {
    System.out.println(getExecutedDecisions());
  }

  private static final Set<String> EXECUTED_DECISIONS = new HashSet<>();

  public static void logExecutedDecision(String methodName, int decisionCount) {
    EXECUTED_DECISIONS.add(methodName + decisionCount);
  }

  public static Set<String> getExecutedDecisions() {
    return EXECUTED_DECISIONS;
  }
}

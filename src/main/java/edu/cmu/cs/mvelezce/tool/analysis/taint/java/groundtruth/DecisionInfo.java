package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashSet;
import java.util.Set;

public class DecisionInfo {

  private final Set<Set<String>> context = new HashSet<>();
  private final DecisionBranchCountTable decisionBranchTable;

  // Dummy constructor for serializing
  private DecisionInfo() {
    this.decisionBranchTable = new DecisionBranchCountTable(new HashSet<>());
  }

  public DecisionInfo(Set<String> options) {
    this.decisionBranchTable = new DecisionBranchCountTable(options);
  }

  public Set<Set<String>> getContext() {
    return context;
  }

  public DecisionBranchCountTable getDecisionBranchTable() {
    return decisionBranchTable;
  }
}

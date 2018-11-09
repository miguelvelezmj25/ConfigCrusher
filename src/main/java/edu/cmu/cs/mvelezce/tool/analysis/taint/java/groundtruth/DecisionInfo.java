package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionInfo {

  private final Map<List<String>, Set<Set<String>>> stackTracesToContexts = new HashMap<>();
  private final Map<List<String>, DecisionBranchCountTable> stackTracesToDecisionBranchTables = new HashMap<>();

  public Map<List<String>, Set<Set<String>>> getStackTracesToContexts() {
    return stackTracesToContexts;
  }

  public Map<List<String>, DecisionBranchCountTable> getStackTracesToDecisionBranchTables() {
    return stackTracesToDecisionBranchTables;
  }
}

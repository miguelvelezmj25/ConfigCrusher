package edu.cmu.cs.mvelezce.evaluation.subtraces;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import java.util.Set;

/** Check that all subtraces are explorer with the given configurations. */
public abstract class SubtracesExecutorChecker {

  private final Set<SubtraceAnalysisInfo> subtraceAnalysisInfos;
  private final Set<Set<String>> configsToExecute;

  protected SubtracesExecutorChecker(
      Set<SubtraceAnalysisInfo> subtraceAnalysisInfos, Set<Set<String>> configsToExecute) {
    this.subtraceAnalysisInfos = subtraceAnalysisInfos;
    this.configsToExecute = configsToExecute;
  }

  public void analyze() {
    throw new UnsupportedOperationException("Implement");
  }
}

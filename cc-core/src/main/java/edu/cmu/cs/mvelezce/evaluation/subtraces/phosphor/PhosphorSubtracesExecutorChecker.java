package edu.cmu.cs.mvelezce.evaluation.subtraces.phosphor;

import edu.cmu.cs.mvelezce.evaluation.subtraces.SubtracesExecutorChecker;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import java.util.Set;

/** Check that all subtraces are explorer with the phosphor derived configurations. */
class PhosphorSubtracesExecutorChecker extends SubtracesExecutorChecker {

  PhosphorSubtracesExecutorChecker(
      Set<SubtraceAnalysisInfo> subtraceAnalysisInfos, Set<Set<String>> configsToExecute) {
    super(subtraceAnalysisInfos, configsToExecute);
  }
}

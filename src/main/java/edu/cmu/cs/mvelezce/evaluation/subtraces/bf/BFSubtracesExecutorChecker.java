package edu.cmu.cs.mvelezce.evaluation.subtraces.bf;

import edu.cmu.cs.mvelezce.evaluation.subtraces.SubtracesExecutorChecker;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import java.util.Set;

/** Check that all subtraces are explorer with the bf derived configurations. */
class BFSubtracesExecutorChecker extends SubtracesExecutorChecker {

  BFSubtracesExecutorChecker(
      Set<SubtraceAnalysisInfo> subtraceAnalysisInfos, Set<Set<String>> configsToExecute) {
    super(subtraceAnalysisInfos, configsToExecute);
  }
}

package edu.cmu.cs.mvelezce.evaluation.subtraces.bf;

import edu.cmu.cs.mvelezce.evaluation.subtraces.SubtracesExecutorChecker;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SatConfigAnalyzer;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtracesValueAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class BFSubtracesExecutorCheckerTest {

  @Test
  public void Subtraces() throws IOException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);

    SatConfigAnalyzer configAnalyzer = new SatConfigAnalyzer(programName);
    Set<Set<String>> minConfigs = configAnalyzer.analyze(args);

    SubtracesExecutorChecker explorer = new BFSubtracesExecutorChecker(subtraceAnalysisInfos, minConfigs);
    explorer.analyze();
  }
}

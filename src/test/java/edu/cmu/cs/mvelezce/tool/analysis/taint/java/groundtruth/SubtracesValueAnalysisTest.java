package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class SubtracesValueAnalysisTest {

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis = new SubtracesAnalysisExecutor(
        programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    TracesAligner tracesAligner = new TracesAligner(programName, configsToTraces);
    List<String> alignedTrace = tracesAligner.align(args);

    SubtracesValueAnalysis subtracesAnalysis = new SubtracesValueAnalysis(programName, configsToTraces,
        alignedTrace);
    subtracesAnalysis.analyze();
  }
}
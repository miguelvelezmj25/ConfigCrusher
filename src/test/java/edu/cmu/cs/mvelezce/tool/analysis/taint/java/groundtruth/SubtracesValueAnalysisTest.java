package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
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
    List<String> alignedTrace = tracesAligner.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesValueAnalysis subtracesAnalysis = new SubtracesValueAnalysis(programName,
        configsToTraces,
        alignedTrace);
    Set<ConfigLabelValueInfo> write = subtracesAnalysis.analyze(args);

    args = new String[0];

    subtracesAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigLabelValueInfo> read = subtracesAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
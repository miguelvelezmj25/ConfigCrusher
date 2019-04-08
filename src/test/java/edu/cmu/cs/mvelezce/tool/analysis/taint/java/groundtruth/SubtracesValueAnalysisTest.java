package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample.SimpleForExampleAdapter;
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

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName,
        configsToTraces,
        alignedTrace);
    Set<ConfigSubtraceValueInfo> write = subtracesValueAnalysis.analyze(args);

    args = new String[0];

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void SimpleForExample() throws IOException, InterruptedException {
    String programName = SimpleForExampleAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis = new SubtracesAnalysisExecutor(
        programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    TracesAligner tracesAligner = new TracesAligner(programName, configsToTraces);
    List<String> alignedTrace = tracesAligner.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName,
        configsToTraces,
        alignedTrace);
    Set<ConfigSubtraceValueInfo> write = subtracesValueAnalysis.analyze(args);

    args = new String[0];

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
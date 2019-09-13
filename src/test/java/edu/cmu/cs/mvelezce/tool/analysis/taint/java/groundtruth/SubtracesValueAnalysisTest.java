package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample.SimpleForExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubtracesValueAnalysisTest {

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);
    Set<SubtraceAnalysisInfo> write = subtracesValueAnalysis.analyze(args);

    args = new String[0];

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<SubtraceAnalysisInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void SimpleForExample() throws IOException, InterruptedException {
    String programName = SimpleForExampleAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);
    Set<SubtraceAnalysisInfo> write = subtracesValueAnalysis.analyze(args);

    args = new String[0];

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<SubtraceAnalysisInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<SubtraceAnalysisInfo> write = subtracesValueAnalysis.analyze(args);

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    args = new String[0];
    Set<SubtraceAnalysisInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScanAdapter() throws IOException, InterruptedException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<SubtraceAnalysisInfo> write = subtracesValueAnalysis.analyze(args);

    subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    args = new String[0];
    Set<SubtraceAnalysisInfo> read = subtracesValueAnalysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = IndexFilesAdapter.PROGRAM_NAME;
    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToLabeledTraces = subtraceLabeler.analyze(args);
    SubtracesValueAnalysis subtracesValueAnalysis =
        new SubtracesValueAnalysis(programName, configsToLabeledTraces);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtracesValueAnalysis.analyze(args);
  }
}

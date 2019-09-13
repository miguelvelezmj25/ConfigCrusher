package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.return2Example.Return2ExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample.SimpleForExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubtraceLabelerTest {

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    Map<Set<String>, List<String>> write = subtraceLabeler.analyze(args);

    args = new String[0];

    subtraceLabeler = new SubtraceLabeler(programName);
    Map<Set<String>, List<String>> read = subtraceLabeler.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void SimpleForExample() throws IOException, InterruptedException {
    String programName = SimpleForExampleAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    Map<Set<String>, List<String>> write = subtraceLabeler.analyze(args);

    args = new String[0];

    subtraceLabeler = new SubtraceLabeler(programName);
    Map<Set<String>, List<String>> read = subtraceLabeler.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Return2Example() throws IOException, InterruptedException {
    String programName = Return2ExampleAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    Map<Set<String>, List<String>> write = subtraceLabeler.analyze(args);

    args = new String[0];

    subtraceLabeler = new SubtraceLabeler(programName);
    Map<Set<String>, List<String>> read = subtraceLabeler.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Map<Set<String>, List<String>> write = subtraceLabeler.analyze(args);

    subtraceLabeler = new SubtraceLabeler(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = subtraceLabeler.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Map<Set<String>, List<String>> write = subtraceLabeler.analyze(args);

    subtraceLabeler = new SubtraceLabeler(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = subtraceLabeler.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = IndexFilesAdapter.PROGRAM_NAME;
    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);

    String[] args = new String[0];
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    subtraceLabeler.analyze(args);
  }
}

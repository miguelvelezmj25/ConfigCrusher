package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

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
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesAnalysisExecutor analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

//    args = new String[2];
//    args[0] = "-delres";
//    args[1] = "-saveres";

    SubtraceLabeler subtraceLabeler = new SubtraceLabeler(programName, configsToTraces);
    subtraceLabeler.analyze();
  }
}
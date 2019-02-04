package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class SubtracesAnalysisTest {

  @Test
  public void Subtraces() throws IOException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> configLabelValues = subtracesValueAnalysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesAnalysis analysis = new SubtracesAnalysis(programName, configLabelValues);
    Set<SubtraceAnalysisInfo> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysis(programName);
    Set<SubtraceAnalysisInfo> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
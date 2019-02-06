package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.andContext.AndContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class MinConfigAnalyzerTest {

  @Test
  public void Subtraces() throws IOException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> configLabelValues = subtracesValueAnalysis.analyze(args);

    SubtracesAnalysis subtracesAnalysis = new SubtracesAnalysis(programName, configLabelValues);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesAnalysis.analyze(args);

    Set<String> options = new HashSet<>(SubtracesAdapter.getListOfOptions());

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    MinConfigAnalyzer analysis = new MinConfigAnalyzer(programName, subtraceAnalysisInfos, options);
    Set<Set<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new MinConfigAnalyzer(programName);
    Set<Set<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void AndContext() throws IOException {
    String programName = AndContextAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> configLabelValues = subtracesValueAnalysis.analyze(args);

    SubtracesAnalysis subtracesAnalysis = new SubtracesAnalysis(programName, configLabelValues);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesAnalysis.analyze(args);

    Set<String> options = new HashSet<>(AndContextAdapter.getListOfOptions());

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    MinConfigAnalyzer analysis = new MinConfigAnalyzer(programName, subtraceAnalysisInfos, options);
    Set<Set<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new MinConfigAnalyzer(programName);
    Set<Set<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
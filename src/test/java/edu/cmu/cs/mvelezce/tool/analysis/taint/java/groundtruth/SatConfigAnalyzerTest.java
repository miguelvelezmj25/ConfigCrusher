package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.andContext.AndContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit2.Implicit2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class SatConfigAnalyzerTest {

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

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName, subtraceAnalysisInfos, options);
    Set<Set<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SatConfigAnalyzer(programName);
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

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName, subtraceAnalysisInfos, options);
    Set<Set<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SatConfigAnalyzer(programName);
    Set<Set<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces2() throws IOException {
    String programName = Subtraces2Adapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> configLabelValues = subtracesValueAnalysis.analyze(args);

    SubtracesAnalysis subtracesAnalysis = new SubtracesAnalysis(programName, configLabelValues);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesAnalysis.analyze(args);

    Set<String> options = new HashSet<>(Subtraces2Adapter.getListOfOptions());

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName, subtraceAnalysisInfos, options);
    Set<Set<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SatConfigAnalyzer(programName);
    Set<Set<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Implicit2() throws IOException {
    String programName = Implicit2Adapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<ConfigSubtraceValueInfo> configLabelValues = subtracesValueAnalysis.analyze(args);

    SubtracesAnalysis subtracesAnalysis = new SubtracesAnalysis(programName, configLabelValues);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesAnalysis.analyze(args);

    Set<String> options = new HashSet<>(Implicit2Adapter.getListOfOptions());

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName, subtraceAnalysisInfos, options);
    Set<Set<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SatConfigAnalyzer(programName);
    Set<Set<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
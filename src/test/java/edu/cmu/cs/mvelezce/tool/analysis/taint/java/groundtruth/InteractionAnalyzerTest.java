package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class InteractionAnalyzerTest {

  @Test
  public void trivial() throws IOException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);

    String[] args = new String[0];
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);

    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    InteractionAnalyzer analysis =
        new InteractionAnalyzer(programName, subtraceAnalysisInfos, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    List<FeatureExpr> write = analysis.analyze(args);

    analysis = new InteractionAnalyzer(programName);
    args = new String[0];
    List<FeatureExpr> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);

    String[] args = new String[0];
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);

    Set<String> options = new HashSet<>(MeasureDiskOrderedScanAdapter.getListOfOptions());
    InteractionAnalyzer analysis =
        new InteractionAnalyzer(programName, subtraceAnalysisInfos, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    List<FeatureExpr> write = analysis.analyze(args);

    analysis = new InteractionAnalyzer(programName);
    args = new String[0];
    List<FeatureExpr> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}

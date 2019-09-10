package edu.cmu.cs.mvelezce.evaluation.dta.interactions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtracesValueAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class SubtracesInteractionsAnalyzerTest {

  private void test(String programName, Set<String> options) throws Exception {
    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    String[] args = new String[0];
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);
    SubtracesInteractionsAnalyzer analysis =
        new SubtracesInteractionsAnalyzer(programName, subtraceAnalysisInfos, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<FeatureExpr> write = analysis.analyze(args);

    analysis = new SubtracesInteractionsAnalyzer(programName);
    args = new String[0];
    Set<FeatureExpr> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivial() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    test(programName, options);
  }

  @Test
  public void measuredDiskOrderedScan() throws Exception {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(MeasureDiskOrderedScanAdapter.getListOfOptions());
    test(programName, options);
  }
}

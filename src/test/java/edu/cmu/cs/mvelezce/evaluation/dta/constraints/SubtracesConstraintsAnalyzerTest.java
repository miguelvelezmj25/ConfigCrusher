package edu.cmu.cs.mvelezce.evaluation.dta.constraints;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtracesValueAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.nesting.NestingAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubtracesConstraintsAnalyzerTest {

  private void analyzeInteractions(String programName, Set<String> options) throws Exception {
    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    String[] args = new String[0];
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);
    SubtracesConstraintsAnalyzer analysis =
        new SubtracesConstraintsAnalyzer(programName, subtraceAnalysisInfos, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<FeatureExpr> write = analysis.analyze(args);

    analysis = new SubtracesConstraintsAnalyzer(programName);
    args = new String[0];
    Set<FeatureExpr> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivial() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }

  @Test
  public void measuredDiskOrderedScan() throws Exception {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(MeasureDiskOrderedScanAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }

  @Test
  public void indexFiles() throws Exception {
    String programName = IndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(IndexFilesAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }

  @Test
  public void nesting() throws Exception {
    String programName = NestingAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(NestingAdapter.getListOfOptions());
    analyzeInteractions(programName, options);
  }
}

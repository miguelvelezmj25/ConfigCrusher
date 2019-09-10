package edu.cmu.cs.mvelezce.evaluation.dta.interactions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint.DTAConstraintAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class PhosphorInteractionsAnalyzerTest {

  private void test(String programName, Set<String> options) throws Exception {
    DTAConstraintAnalysis constraintAnalysis = new DTAConstraintAnalysis(programName);
    String[] args = new String[0];
    Set<ConfigConstraint> constraints = constraintAnalysis.analyze(args);
    PhosphorInteractionsAnalyzer analysis =
        new PhosphorInteractionsAnalyzer(programName, constraints, options);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<FeatureExpr> write = analysis.analyze(args);

    analysis = new PhosphorInteractionsAnalyzer(programName);
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

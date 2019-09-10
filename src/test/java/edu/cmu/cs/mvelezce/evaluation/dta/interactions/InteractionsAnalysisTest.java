package edu.cmu.cs.mvelezce.evaluation.dta.interactions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.util.Set;
import org.junit.Test;

public class InteractionsAnalysisTest {

  private void analyze(String programName) throws Exception {
    String[] args = new String[0];
    PhosphorInteractionsAnalyzer phosphorInteractionsAnalyzer =
        new PhosphorInteractionsAnalyzer(programName);
    Set<FeatureExpr> phosphorInteractions = phosphorInteractionsAnalyzer.analyze(args);

    SubtracesInteractionsAnalyzer subtracesInteractionsAnalyzer =
        new SubtracesInteractionsAnalyzer(programName);
    Set<FeatureExpr> subtracesInteractions = subtracesInteractionsAnalyzer.analyze(args);

    InteractionsAnalysis analysis = new InteractionsAnalysis(programName);
    analysis.analyze(phosphorInteractions, subtracesInteractions);
  }

  @Test
  public void trivial() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    analyze(programName);
  }

  @Test
  public void measureDiskOrderedScan() throws Exception {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    analyze(programName);
  }
}

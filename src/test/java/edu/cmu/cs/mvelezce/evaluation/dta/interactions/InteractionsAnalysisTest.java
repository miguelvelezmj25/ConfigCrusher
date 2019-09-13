package edu.cmu.cs.mvelezce.evaluation.dta.interactions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Test;

import java.util.Set;

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

  @Test
  public void indexFiles() throws Exception {
    String programName = IndexFilesAdapter.PROGRAM_NAME;
    analyze(programName);
  }
}

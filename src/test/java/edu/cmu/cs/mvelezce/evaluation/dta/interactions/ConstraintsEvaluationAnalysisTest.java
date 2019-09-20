package edu.cmu.cs.mvelezce.evaluation.dta.interactions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ConstraintsEvaluationAnalysisTest {

  private void analyze(String programName, Set<String> options) throws Exception {
    String[] args = new String[0];
    PhosphorConstraintsAnalyzer phosphorConstraintsAnalyzer =
        new PhosphorConstraintsAnalyzer(programName);
    Set<FeatureExpr> phosphorInteractions = phosphorConstraintsAnalyzer.analyze(args);

    SubtracesConstraintsAnalyzer subtracesConstraintsAnalyzer =
        new SubtracesConstraintsAnalyzer(programName);
    Set<FeatureExpr> subtracesInteractions = subtracesConstraintsAnalyzer.analyze(args);

    ConstraintsEvaluationAnalysis analysis = new ConstraintsEvaluationAnalysis(programName, options);
    analysis.analyze(phosphorInteractions, subtracesInteractions);
  }

  @Test
  public void trivial() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    analyze(programName, options);
  }

  @Test
  public void measureDiskOrderedScan() throws Exception {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(MeasureDiskOrderedScanAdapter.getListOfOptions());
    analyze(programName, options);
  }

  @Test
  public void indexFiles() throws Exception {
    String programName = IndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(IndexFilesAdapter.getListOfOptions());
    analyze(programName, options);
  }
}

package edu.cmu.cs.mvelezce.evaluation.dta.constraints;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.evaluation.dta.constraints.subtraces.SubtraceOutcomeConstraint;
import edu.cmu.cs.mvelezce.evaluation.dta.constraints.subtraces.SubtracesConstraintsAnalyzer;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.nesting.NestingAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConstraintsEvaluationAnalysisTest {

  private void analyze(String programName, Set<String> options) throws Exception {
    Set<FeatureExpr> idtaConstraints = getIDTAConstraints(programName);
    Set<FeatureExpr> subtraceConstraints = getSubtraceConstraints(programName);

    ConstraintsEvaluationAnalysis analysis =
        new ConstraintsEvaluationAnalysis(programName, options);
    analysis.analyze(idtaConstraints, subtraceConstraints);
  }

  private Set<FeatureExpr> getSubtraceConstraints(String programName) throws IOException {
    SubtracesConstraintsAnalyzer subtracesConstraintsAnalyzer =
        new SubtracesConstraintsAnalyzer(programName);
    String[] args = new String[0];
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        subtracesConstraintsAnalyzer.analyze(args);

    Set<FeatureExpr> subtraceConstraints = new HashSet<>();

    FeatureExpr True = SATFeatureExprFactory.True();
    FeatureExpr False = SATFeatureExprFactory.False();

    for (SubtraceOutcomeConstraint subtraceOutcomeConstraint : subtracesOutcomeConstraint) {
      Collection<FeatureExpr> constraints =
          subtraceOutcomeConstraint.getOutcomesToConstraints().values();

      for (FeatureExpr constraint : constraints) {
        if (constraint.equals(True) || constraint.equals(False)) {
          continue;
        }

        subtraceConstraints.add(constraint);
      }
    }

    return subtraceConstraints;
  }

  private Set<FeatureExpr> getIDTAConstraints(String programName) throws IOException {
    System.err.println(
        "Might want to change how to get the IDTA constraints to a map from statements to constraints");
    idtaConstraintsAnalyzer idtaConstraintsAnalyzer = new idtaConstraintsAnalyzer(programName);
    String[] args = new String[0];

    return idtaConstraintsAnalyzer.analyze(args);
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

  @Test
  public void nesting() throws Exception {
    String programName = NestingAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(NestingAdapter.getListOfOptions());
    analyze(programName, options);
  }
}

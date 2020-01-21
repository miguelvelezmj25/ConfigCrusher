package edu.cmu.cs.mvelezce.eval.java.accuracy.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.eval.java.accuracy.AccuracyEvaluation;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.List;

public class AccuracyConstraintEvaluation extends AccuracyEvaluation<FeatureExpr> {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/constraint";

  public AccuracyConstraintEvaluation(String programName) {
    this(programName, new ArrayList<>());
  }

  public AccuracyConstraintEvaluation(String programName, List<String> options) {
    super(programName, options);
  }

  @Override
  protected String getOutputDir() {
    return OUTPUT_DIR;
  }
}

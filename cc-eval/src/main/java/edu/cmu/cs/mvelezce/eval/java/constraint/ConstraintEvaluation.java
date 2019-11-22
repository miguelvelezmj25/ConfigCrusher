package edu.cmu.cs.mvelezce.eval.java.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.List;

public class ConstraintEvaluation extends Evaluation<FeatureExpr> {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/constraint";

  public ConstraintEvaluation(String programName) {
    this(programName, new ArrayList<>());
  }

  public ConstraintEvaluation(String programName, List<String> options) {
    super(programName, options);
  }

  @Override
  protected String getOutputDir() {
    return OUTPUT_DIR;
  }
}

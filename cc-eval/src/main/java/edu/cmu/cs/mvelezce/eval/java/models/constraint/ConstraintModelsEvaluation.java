package edu.cmu.cs.mvelezce.eval.java.models.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.eval.java.models.ModelsEvaluation;
import edu.cmu.cs.mvelezce.utils.config.Options;

public class ConstraintModelsEvaluation extends ModelsEvaluation<FeatureExpr> {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/constraint";

  public ConstraintModelsEvaluation(String programName) {
    super(programName);
  }
}

package edu.cmu.cs.mvelezce.pretty.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.pretty.BasePrettyBuilder;

import java.util.Collection;

public abstract class BaseConstraintPrettyBuilder extends BasePrettyBuilder<FeatureExpr> {

  public BaseConstraintPrettyBuilder(
      String programName, Collection<String> options, PerformanceModel<FeatureExpr> model) {
    super(programName, options, model);
  }
}

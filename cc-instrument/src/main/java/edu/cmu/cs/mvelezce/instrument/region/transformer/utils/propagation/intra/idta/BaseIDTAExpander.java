package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public final class BaseIDTAExpander {

  private BaseIDTAExpander() {}

  public static String prettyPrintConstraints(
      @Nullable Set<FeatureExpr> constraints, Set<String> options) {
    Set<String> prettyConstraints = new HashSet<>();

    if (constraints == null) {
      throw new RuntimeException("The constraints cannot be null");
    }

    for (FeatureExpr constraint : constraints) {
      String prettyConstraint = ConstraintUtils.prettyPrintFeatureExpr(constraint, options);
      prettyConstraints.add(prettyConstraint);
    }

    return prettyConstraints.toString();
  }
}

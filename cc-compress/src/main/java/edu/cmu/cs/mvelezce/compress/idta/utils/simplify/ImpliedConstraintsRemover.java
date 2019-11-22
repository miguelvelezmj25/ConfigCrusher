package edu.cmu.cs.mvelezce.compress.idta.utils.simplify;

import de.fosd.typechef.featureexpr.FeatureExpr;

import java.util.HashSet;
import java.util.Set;

public final class ImpliedConstraintsRemover {

  public static void removeImpliedConstraints(Set<FeatureExpr> constraints) {
    Set<FeatureExpr> impliedConstraints = new HashSet<>();

    for (FeatureExpr currentConstraint : constraints) {
      if (impliedConstraints.contains(currentConstraint)) {
        continue;
      }

      for (FeatureExpr constraint : constraints) {
        if (currentConstraint == constraint) {
          continue;
        }

        if (!currentConstraint.implies(constraint).isTautology()) {
          continue;
        }

        impliedConstraints.add(constraint);
      }
    }

    constraints.removeAll(impliedConstraints);
  }
}

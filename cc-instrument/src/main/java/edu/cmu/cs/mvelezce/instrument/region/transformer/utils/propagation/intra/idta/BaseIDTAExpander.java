package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class BaseIDTAExpander {

  private static final FeatureExpr FALSE = SATFeatureExprFactory.False();
  private static final BaseIDTAExpander INSTANCE = new BaseIDTAExpander();
  private final Set<FeatureExpr> globalConstraints = new HashSet<>();

  private BaseIDTAExpander() {}

  public static BaseIDTAExpander getInstance() {
    return INSTANCE;
  }

  public String prettyPrintConstraints(
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

  public void init(Collection<Set<FeatureExpr>> setOfConstraints) {
    if (!this.globalConstraints.isEmpty()) {
      return;
    }

    for (Set<FeatureExpr> constraints : setOfConstraints) {
      this.globalConstraints.addAll(constraints);
    }
  }

  public boolean canExpandConstraints(Set<FeatureExpr> thisData, Set<FeatureExpr> thatData) {
    if (thisData == null) {
      throw new RuntimeException("This case should never happen");
    }

    if (thisData.isEmpty()) {
      throw new RuntimeException("How can this data be empty, but not null?");
    }

    if (thatData == null) {
      return true;
    }

    if (thatData.isEmpty()) {
      throw new RuntimeException("How can that data be empty, but not null?");
    }

    Set<FeatureExpr> newConstraints = new HashSet<>();

    for (FeatureExpr thisConstraint : thisData) {
      for (FeatureExpr downConstraint : thatData) {
        FeatureExpr newConstraint = thisConstraint.and(downConstraint);

        if (newConstraint.equals(BaseIDTAExpander.FALSE)) {
          continue;
        }

        newConstraints.add(newConstraint);
      }
    }

    return this.globalConstraints.containsAll(newConstraints);
  }

  public boolean containsAll(Set<FeatureExpr> thisData, Set<FeatureExpr> thatData) {
    if (thisData == null || thatData == null) {
      return false;
    }

    return thisData.containsAll(thatData);
  }
}

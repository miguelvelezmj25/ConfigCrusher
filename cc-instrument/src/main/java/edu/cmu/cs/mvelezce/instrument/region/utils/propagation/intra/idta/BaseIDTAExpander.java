package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta;

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

  public boolean canExpandConstraints(
      @Nullable Set<FeatureExpr> thisConstraints, @Nullable Set<FeatureExpr> thatConstraints) {
    if (thisConstraints == null) {
      throw new RuntimeException("This case should never happen");
    }

    if (thisConstraints.isEmpty()) {
      throw new RuntimeException("How can this data be empty, but not null?");
    }

    if (thatConstraints == null) {
      return true;
    }

    if (thatConstraints.isEmpty()) {
      throw new RuntimeException("How can that data be empty, but not null?");
    }

    Set<FeatureExpr> newConstraints = new HashSet<>();

    for (FeatureExpr thisConstraint : thisConstraints) {
      for (FeatureExpr thatConstraint : thatConstraints) {
        FeatureExpr newConstraint = thisConstraint.and(thatConstraint);

        if (newConstraint.equals(BaseIDTAExpander.FALSE)) {
          continue;
        }

        newConstraints.add(newConstraint);
      }
    }

    return this.globalConstraints.containsAll(newConstraints);
  }

  public boolean containsAll(Set<FeatureExpr> thisConstraints, Set<FeatureExpr> thatConstraints) {
    if (thisConstraints == null || thatConstraints == null) {
      return false;
    }

    return thisConstraints.containsAll(thatConstraints);
  }

  public Set<FeatureExpr> mergeData(
      Set<FeatureExpr> thisConstraints, Set<FeatureExpr> thatConstraints) {
    Set<FeatureExpr> newConstraints = new HashSet<>(thisConstraints);

    if (thatConstraints == null) {
      return newConstraints;
    }

    newConstraints.addAll(thatConstraints);

    return newConstraints;
  }
}

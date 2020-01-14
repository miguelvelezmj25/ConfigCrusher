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

  /** ∃ c ∈ GlobalConstraints . c ⟹ newConstraint */
  public boolean canMergeConstraints(
      Set<FeatureExpr> expandingConstraints, @Nullable Set<FeatureExpr> currentConstraints) {
    if (expandingConstraints.isEmpty()) {
      throw new RuntimeException("Expanding constraints should never be empty");
    }

    if (currentConstraints == null) {
      return true;
    }

    if (currentConstraints.isEmpty()) {
      throw new RuntimeException("How can that data be empty, but not null?");
    }

    // Shortcut
    if (currentConstraints.equals(expandingConstraints)) {
      return true;
    }

    Set<FeatureExpr> newConstraints = new HashSet<>();

    for (FeatureExpr expandingConstraint : expandingConstraints) {
      for (FeatureExpr currentConstraint : currentConstraints) {
        FeatureExpr newConstraint = expandingConstraint.and(currentConstraint);

        if (newConstraint.isContradiction()) {
          continue;
        }

        newConstraints.add(newConstraint);
      }
    }

    // Shortcut
    if (newConstraints.equals(expandingConstraints)) {
      return true;
    }

    // Logic
    for (FeatureExpr newConstraint : newConstraints) {
      boolean existsGlobalConstraint = false;

      for (FeatureExpr globalConstraint : this.globalConstraints) {
        if (globalConstraint.implies(newConstraint).isTautology()) {
          existsGlobalConstraint = true;
          break;
        }
      }

      if (!existsGlobalConstraint) {
        return false;
      }
    }

    return true;
    //    return this.impliesAll(this.globalConstraints, newConstraints);
  }

  /** ∀ dc ∈ ImpliedConstraints . ∃ gc ∈ ImplyingConstraints . gc ⟹ dc */
  public boolean impliesAll(
      @Nullable Set<FeatureExpr> implyingConstraints, Set<FeatureExpr> impliedConstraints) {
    if (implyingConstraints == null) {
      return false;
    }

    for (FeatureExpr impliedConstraint : impliedConstraints) {
      boolean exists = false;

      for (FeatureExpr implyingConstraint : implyingConstraints) {
        if (implyingConstraint.implies(impliedConstraint).isTautology()) {
          exists = true;
          break;
        }
      }

      if (!exists) {
        return false;
      }
    }

    return true;
  }

  public Set<FeatureExpr> mergeData(
      Set<FeatureExpr> thisConstraints, @Nullable Set<FeatureExpr> thatConstraints) {
    Set<FeatureExpr> newConstraints = new HashSet<>(thisConstraints);

    if (thatConstraints == null) {
      return newConstraints;
    }

    newConstraints.addAll(thatConstraints);

    return newConstraints;
  }

  //  private boolean containsAll(
  //      Set<FeatureExpr> currentConstraints, Set<FeatureExpr> expandingConstraints) {
  //    if (currentConstraints == null || expandingConstraints == null) {
  //      return false;
  //    }
  //
  //    return currentConstraints.containsAll(expandingConstraints);
  //  }
  //  /**
  //   * The disjunction of all implying constraints implies the disjunction of all implied
  // constraints
  //   */
  //  public boolean completelyImplies(
  //      Set<FeatureExpr> implyingConstraints, @Nullable Set<FeatureExpr> impliedConstraints) {
  //    if (impliedConstraints == null) {
  //      return true;
  //    }
  //
  //    boolean containsAll = this.containsAll(implyingConstraints, impliedConstraints);
  //
  //    FeatureExpr implyingConstraintsDisjunction = this.getDisjunction(implyingConstraints);
  //    FeatureExpr impliedConstraintsDisjunction = this.getDisjunction(impliedConstraints);
  //
  //    //    boolean completelyImply =
  //    //
  // impliedConstraintsDisjunction.implies(implyingConstraintsDisjunction).isTautology();
  //    //
  //    //    if (containsAll != completelyImply) {
  //    //      throw new RuntimeException("The contains all and implies all results do not match");
  //    //    }
  //    //
  //    //    return completelyImply;
  //    return containsAll;
  //  }
  //
  //  private FeatureExpr getDisjunction(Set<FeatureExpr> constraints) {
  //    FeatureExpr disjunction = FALSE;
  //
  //    for (FeatureExpr constraint : constraints) {
  //      disjunction = disjunction.or(constraint);
  //    }
  //
  //    return disjunction;
  //  }

  public Set<FeatureExpr> getGlobalConstraints() {
    return globalConstraints;
  }
}

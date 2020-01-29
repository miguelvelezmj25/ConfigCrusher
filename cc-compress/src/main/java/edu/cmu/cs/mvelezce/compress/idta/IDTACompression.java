package edu.cmu.cs.mvelezce.compress.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IDTACompression extends BaseCompression {

  private final Collection<Set<FeatureExpr>> allConstraints;

  protected IDTACompression(
      String programName, List<String> options, Collection<Set<FeatureExpr>> allConstraints) {
    super(programName, options);

    this.allConstraints = allConstraints;
  }

  protected Set<FeatureExpr> getCoveredConstraints(
      Set<String> config, Set<FeatureExpr> constraints) {
    String configStringConstraint = ConstraintUtils.parseAsConstraint(config, this.getOptions());
    FeatureExpr configConstraint =
        FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, configStringConstraint);

    Set<FeatureExpr> coveredConstraints = new HashSet<>();

    for (FeatureExpr constraint : constraints) {
      if (configConstraint.implies(constraint).isTautology()) {
        coveredConstraints.add(constraint);
      }
    }

    return coveredConstraints;
  }

  protected Set<FeatureExpr> expandAllConstraints() {
    Set<FeatureExpr> expandedConstraints = new HashSet<>();

    for (Set<FeatureExpr> constraints : this.allConstraints) {
      expandedConstraints.addAll(constraints);
    }

    return expandedConstraints;
  }
}

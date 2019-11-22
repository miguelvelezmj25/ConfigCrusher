package edu.cmu.cs.mvelezce.compress.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.utils.simplify.ImpliedConstraintsRemover;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IDTACompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compression/" + Options.DIRECTORY + "/compression/java/programs/idta";

  private final Collection<Set<FeatureExpr>> allConstraints;

  IDTACompression(
      String programName, List<String> options, Collection<Set<FeatureExpr>> allConstraints) {
    super(programName, options);

    this.allConstraints = allConstraints;
  }

  @Override
  public Set<Set<String>> analyze() {
    Set<Set<String>> configs = new HashSet<>();
    Set<FeatureExpr> constraints = this.expandAllConstraints();
    ImpliedConstraintsRemover.removeImpliedConstraints(constraints);

    Set<FeatureExpr> coveredConstraints = new HashSet<>();

    for (FeatureExpr constraint : constraints) {
      if (coveredConstraints.contains(constraint)) {
        continue;
      }

      Set<String> config = ConstraintUtils.toConfig(constraint, this.getOptions());
      configs.add(config);

      coveredConstraints.addAll(this.getCoveredConstraints(config, constraints));
    }

    return configs;
  }

  private Set<FeatureExpr> getCoveredConstraints(Set<String> config, Set<FeatureExpr> constraints) {
    String configStringConstraint = ConstraintUtils.parseAsConstraint(config, this.getOptions());
    FeatureExpr configConstraint = MinConfigsGenerator.parseAsFeatureExpr(configStringConstraint);

    Set<FeatureExpr> coveredConstraints = new HashSet<>();

    for (FeatureExpr constraint : constraints) {
      if (configConstraint.implies(constraint).isTautology()) {
        coveredConstraints.add(constraint);
      }
    }

    return coveredConstraints;
  }

  private Set<FeatureExpr> expandAllConstraints() {
    Set<FeatureExpr> expandedConstraints = new HashSet<>();

    for (Set<FeatureExpr> constraints : this.allConstraints) {
      expandedConstraints.addAll(constraints);
    }

    return expandedConstraints;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

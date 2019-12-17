package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.compress.idta.IDTACompression;
import edu.cmu.cs.mvelezce.compress.idta.utils.simplify.ImpliedConstraintsRemover;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

/** Sub-optimal algorithm */
public class IDTASuboptimalGreedyConjunctionsCompression extends IDTACompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/"
          + Options.DIRECTORY
          + "/compression/java/programs/idta/suboptimal/greedy/conjunctions";

  public IDTASuboptimalGreedyConjunctionsCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  IDTASuboptimalGreedyConjunctionsCompression(
      String programName, List<String> options, Collection<Set<FeatureExpr>> allConstraints) {
    super(programName, options, allConstraints);
  }

  @Override
  public Set<Set<String>> analyze() {
    Set<Set<String>> configs = new HashSet<>();
    Set<FeatureExpr> constraints = this.expandAllConstraints();
    ImpliedConstraintsRemover.removeImpliedConstraints(constraints);

    Set<FeatureExpr> coveredConstraints = new HashSet<>();

    while (coveredConstraints.size() != constraints.size()) {
      FeatureExpr newConstraint = SATFeatureExprFactory.True();
      Set<FeatureExpr> constraintsUsed = new HashSet<>();

      for (FeatureExpr constraint : constraints) {
        if (coveredConstraints.contains(constraint)) {
          continue;
        }

        if (newConstraint.mex(constraint).isTautology()) {
          continue;
        }

        newConstraint = newConstraint.and(constraint);
        constraintsUsed.add(constraint);
      }

      Set<String> config = ConstraintUtils.toConfig(newConstraint, this.getOptions());
      configs.add(config);

      coveredConstraints.addAll(constraintsUsed);
    }

    return configs;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}

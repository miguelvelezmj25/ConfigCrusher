package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.compress.idta.IDTACompression;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
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
    //    ImpliedConstraintsRemover.removeImpliedConstraints(constraints);

    Set<FeatureExpr> coveredConstraints = new HashSet<>();

    while (coveredConstraints.size() != constraints.size()) {
      FeatureExpr newConstraint = FeatureExprUtils.getTrue(IDTA.USE_BDD);

      for (FeatureExpr constraint : constraints) {
        if (coveredConstraints.contains(constraint)) {
          continue;
        }

        FeatureExpr andedFormula = newConstraint.and(constraint);

        if (andedFormula.isContradiction()) {
          continue;
        }

        newConstraint = andedFormula;
        coveredConstraints.add(constraint);
      }

      Set<String> config = ConstraintUtils.toConfig(newConstraint, this.getOptions());
      configs.add(config);
    }

    return configs;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}

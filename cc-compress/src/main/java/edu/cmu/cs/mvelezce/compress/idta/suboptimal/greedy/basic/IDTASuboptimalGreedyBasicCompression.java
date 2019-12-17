package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.basic;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.compress.idta.IDTACompression;
import edu.cmu.cs.mvelezce.compress.idta.utils.simplify.ImpliedConstraintsRemover;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

/** Sub-optimal algorithm */
public class IDTASuboptimalGreedyBasicCompression extends IDTACompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/"
          + Options.DIRECTORY
          + "/compression/java/programs/idta/suboptimal/greedy/basic";

  public IDTASuboptimalGreedyBasicCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  IDTASuboptimalGreedyBasicCompression(
      String programName, List<String> options, Collection<Set<FeatureExpr>> allConstraints) {
    super(programName, options, allConstraints);
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

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}

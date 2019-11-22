package edu.cmu.cs.mvelezce.compress.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.utils.simplify.ImpliedConstraintsRemover;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IDTACompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compression/" + Options.DIRECTORY + "/compression/java/programs/idta";

  private final Collection<Set<FeatureExpr>> allConstraints;

  IDTACompression(String programName, Collection<Set<FeatureExpr>> allConstraints) {
    super(programName);

    this.allConstraints = allConstraints;
  }

  @Override
  public Set<Set<String>> analyze() {
    Set<FeatureExpr> constraints = this.expandAllConstraints();
    ImpliedConstraintsRemover.removeImpliedConstraints(constraints);

    throw new UnsupportedOperationException("implement");
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

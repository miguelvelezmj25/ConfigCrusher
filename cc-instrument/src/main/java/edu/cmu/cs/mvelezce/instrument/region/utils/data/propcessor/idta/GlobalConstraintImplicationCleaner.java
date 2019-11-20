package edu.cmu.cs.mvelezce.instrument.region.utils.data.propcessor.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class will update the sets of constraints of a region. It will remove implied constraints by
 * other constraints and it will add global constraints that implied the constraints in the region.
 * The latter process potentially (which we have seen in some programs) adds missing interactions
 * not captured by the unsoundness of the idta.
 */
public final class GlobalConstraintImplicationCleaner
    extends BlockRegionAnalyzer<Set<FeatureExpr>> {

  private final BaseIDTAExpander baseIDTAExpander;

  public GlobalConstraintImplicationCleaner(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      BaseIDTAExpander baseIDTAExpander) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.baseIDTAExpander = baseIDTAExpander;
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Set<FeatureExpr> data = this.getData(region);

    if (data == null) {
      throw new RuntimeException(
          "We did not expect the constraints to be null for region " + region.getId());
    }

    this.clean(data);

    return new HashSet<>();
  }

  @Override
  protected String getAnalysisName() {
    return "cleanImpliedConstraints";
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Set<FeatureExpr> constraints = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintConstraints(constraints, options);
  }

  private void clean(Set<FeatureExpr> constraints) {
    this.removeImpliedConstraints(constraints);
    //    this.addImplyingConstraints(constraints);
    //    this.removeImpliedConstraints(constraints);
  }

  private void removeImpliedConstraints(Set<FeatureExpr> constraints) {
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

  private void addImplyingConstraints(Set<FeatureExpr> constraints) {
    for (FeatureExpr globalConstraint : this.baseIDTAExpander.getGlobalConstraints()) {
      for (FeatureExpr constraint : constraints) {
        if (!globalConstraint.implies(constraint).isTautology()) {
          continue;
        }

        constraints.add(globalConstraint);
        break;
      }
    }
  }
}

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
  private final Set<FeatureExpr> cleanedGlobalConstraints;

  public GlobalConstraintImplicationCleaner(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      BaseIDTAExpander baseIDTAExpander) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.baseIDTAExpander = baseIDTAExpander;
    this.cleanedGlobalConstraints = new HashSet<>(this.baseIDTAExpander.getGlobalConstraints());

    System.err.println("Should we simplify the global set of constraints?");
    System.err.println(
        "Should we simplify the constraints of each region? For debugging? For indicating the performance of the region?");
    //    this.removeImpliedConstraints(this.cleanedGlobalConstraints);
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
        if (impliedConstraints.contains(constraint)) {
          continue;
        }

        if (currentConstraint.equivalentTo(constraint)) {
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

  //  private void addImplyingConstraints(Set<FeatureExpr> constraints) {
  //    Map<FeatureExpr, Set<FeatureExpr>> constraintsToImplyingConstraints =
  //        this.getEmptyConstraintsToImplyingConstraints(constraints);
  //    this.calculateImplyingConstraints(constraintsToImplyingConstraints);
  //
  //    Map<FeatureExpr, Set<FeatureExpr>> constraintsToReplace =
  //        this.getConstraintsToReplace(constraintsToImplyingConstraints);
  //    this.replaceConstraints(constraints, constraintsToReplace);
  //  }
  //
  //  private void replaceConstraints(
  //      Set<FeatureExpr> constraints, Map<FeatureExpr, Set<FeatureExpr>> constraintsToReplace) {
  //    for (Map.Entry<FeatureExpr, Set<FeatureExpr>> entry : constraintsToReplace.entrySet()) {
  //      if (entry.getValue().isEmpty()) {
  //        continue;
  //      }
  //
  //      constraints.remove(entry.getKey());
  //      constraints.addAll(entry.getValue());
  //    }
  //  }
  //
  //  private Map<FeatureExpr, Set<FeatureExpr>> getConstraintsToReplace(
  //      Map<FeatureExpr, Set<FeatureExpr>> constraintsToImplyingConstraints) {
  //    Map<FeatureExpr, Set<FeatureExpr>> constraintsToReplace = new HashMap<>();
  //
  //    for (FeatureExpr constraint : constraintsToImplyingConstraints.keySet()) {
  //      constraintsToReplace.put(constraint, new HashSet<>());
  //    }
  //
  //    for (Map.Entry<FeatureExpr, Set<FeatureExpr>> entry :
  //        constraintsToImplyingConstraints.entrySet()) {
  //      FeatureExpr constraint = entry.getKey();
  //      Set<FeatureExpr> implyingConstraints = entry.getValue();
  //
  //      if (implyingConstraints.isEmpty()) {
  //        continue;
  //      }
  //
  //      if (implyingConstraints.size() == 1) {
  //        constraintsToReplace.get(constraint).addAll(implyingConstraints);
  //      } else if (this.areAllMex(implyingConstraints)) {
  //        constraintsToReplace.get(constraint).addAll(implyingConstraints);
  //      } else {
  //        Set<FeatureExpr> oneConstraint = new HashSet<>();
  //        oneConstraint.add(constraint);
  //
  //        System.err.println(
  //            "Constraint "
  //                + this.baseIDTAExpander.prettyPrintConstraints(oneConstraint, this.getOptions())
  //                + " is implied by multiple mutually inclusive global constraints "
  //                + this.baseIDTAExpander.prettyPrintConstraints(
  //                    implyingConstraints, this.getOptions())
  //                + ". Therefore, it is not clear which constraints to add in this region");
  //      }
  //    }
  //
  //    return constraintsToReplace;
  //  }
  //
  //  private boolean areAllMex(Set<FeatureExpr> implyingConstraints) {
  //    for (FeatureExpr constraint1 : implyingConstraints) {
  //      for (FeatureExpr constraint2 : implyingConstraints) {
  //        if (constraint1 == constraint2) {
  //          continue;
  //        }
  //
  //        if (!constraint1.mex(constraint2).isTautology()) {
  //          return false;
  //        }
  //      }
  //    }
  //
  //    return true;
  //  }
  //
  //  private void calculateImplyingConstraints(
  //      Map<FeatureExpr, Set<FeatureExpr>> constraintsToImplyingConstraints) {
  //    for (FeatureExpr globalConstraint : this.cleanedGlobalConstraints) {
  //      for (Map.Entry<FeatureExpr, Set<FeatureExpr>> entry :
  //          constraintsToImplyingConstraints.entrySet()) {
  //        FeatureExpr constraint = entry.getKey();
  //
  //        if (globalConstraint == constraint) {
  //          continue;
  //        }
  //
  //        if (!globalConstraint.implies(constraint).isTautology()) {
  //          continue;
  //        }
  //
  //        constraintsToImplyingConstraints.get(constraint).add(globalConstraint);
  //      }
  //    }
  //  }
  //
  //  private Map<FeatureExpr, Set<FeatureExpr>> getEmptyConstraintsToImplyingConstraints(
  //      Set<FeatureExpr> constraints) {
  //    Map<FeatureExpr, Set<FeatureExpr>> constraintsToImplyingConstraints = new HashMap<>();
  //
  //    for (FeatureExpr constraint : constraints) {
  //      constraintsToImplyingConstraints.put(constraint, new HashSet<>());
  //    }
  //
  //    return constraintsToImplyingConstraints;
  //  }
}

package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.up.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.up.BaseUpExpander;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTAUpExpander extends BaseUpExpander<Set<FeatureExpr>> {

  public IDTAUpExpander(
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData) {
    super(options, blockRegionMatcher, regionsToData);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Set<FeatureExpr> constraints = this.getData(region);
    Set<String> options = this.getOptions();

    return BaseIDTAExpander.prettyPrintConstraints(constraints, options);
  }

  @Override
  protected Set<FeatureExpr> mergeData(
      Set<FeatureExpr> thisData, @Nullable Set<FeatureExpr> thatData) {
    if (thatData != null && thatData.isEmpty()) {
      throw new RuntimeException("How can the up data be empty, but not null?");
    }

    if (thatData == null) {
      return thisData;
    }

    Set<FeatureExpr> constraints = this.mergeConstraints(thisData, thatData);
    Set<FeatureExpr> upConstraints = this.getUpConstraintsNotMerged(constraints, thatData);
    constraints.addAll(upConstraints);

    return constraints;
  }

  private Set<FeatureExpr> getUpConstraintsNotMerged(
      Set<FeatureExpr> constraints, Set<FeatureExpr> upConstraints) {
    Set<FeatureExpr> upConstraintsNotMerged = new HashSet<>();

    for (FeatureExpr upConstraint : upConstraints) {
      boolean merged = false;

      for (FeatureExpr constraint : constraints) {
        if (constraint.implies(upConstraint).isTautology()) {
          merged = true;

          break;
        }
      }

      if (merged) {
        continue;
      }

      upConstraintsNotMerged.add(upConstraint);
    }

    return upConstraintsNotMerged;
  }

  private Set<FeatureExpr> mergeConstraints(
      Set<FeatureExpr> thisConstraints, Set<FeatureExpr> upConstraints) {
    Set<FeatureExpr> constraints = new HashSet<>();

    for (FeatureExpr thisConstraint : thisConstraints) {
      for (FeatureExpr upConstraint : upConstraints) {
        if (!thisConstraint.implies(upConstraint).isTautology()) {
          continue;
        }

        constraints.add(thisConstraint);

        break;
      }
    }

    if (!constraints.equals(thisConstraints)) {
      throw new RuntimeException("Could not merge all of this region's data");
    }

    return constraints;
  }

  @Override
  protected boolean canExpandUp(
      @Nullable Set<FeatureExpr> thisData, @Nullable Set<FeatureExpr> upData) {
    if (thisData == null) {
      return false;
    }

    if (thisData.isEmpty()) {
      throw new RuntimeException("How can this data be empty, but not null?");
    }

    if (upData == null) {
      return true;
    }

    if (upData.isEmpty()) {
      throw new RuntimeException("How can that data be empty, but not null?");
    }

    for (FeatureExpr thisConstraint : thisData) {
      for (FeatureExpr upConstraint : upData) {
        if (thisConstraint.implies(upConstraint).isTautology()) {
          return true;
        }
      }
    }

    return false;
  }
}

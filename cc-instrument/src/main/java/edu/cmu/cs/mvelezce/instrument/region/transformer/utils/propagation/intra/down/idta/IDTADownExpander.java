package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.down.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.down.BaseDownExpander;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.idta.BaseIDTAExpander;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTADownExpander extends BaseDownExpander<Set<FeatureExpr>> {

  public IDTADownExpander(
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData) {
    super(options, blockRegionMatcher, regionsToData);
  }

  @Override
  protected boolean canExpandDown(
      @Nullable Set<FeatureExpr> thisData, @Nullable Set<FeatureExpr> downData) {
    if (thisData == null) {
      throw new RuntimeException("This case should never happen");
    }

    if (thisData.isEmpty()) {
      throw new RuntimeException("How can this data be empty, but not null?");
    }

    if (downData == null) {
      return true;
    }

    if (downData.isEmpty()) {
      throw new RuntimeException("How can that data be empty, but not null?");
    }

    return thisData.equals(downData);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Set<FeatureExpr> constraints = this.getData(region);
    Set<String> options = this.getOptions();

    return BaseIDTAExpander.prettyPrintConstraints(constraints, options);
  }
}

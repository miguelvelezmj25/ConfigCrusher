package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.BaseUpExpander;

import java.util.Map;
import java.util.Set;

public class IDTAUpExpander extends BaseUpExpander<Set<FeatureExpr>> {

  public IDTAUpExpander(
      BlockRegionMatcher blockRegionMatcher, Map<JavaRegion, Set<FeatureExpr>> regionsToData) {
    super(blockRegionMatcher, regionsToData);
  }

  @Override
  protected boolean canPropagateUp(Set<FeatureExpr> regionData, Set<FeatureExpr> idData) {
    System.err.println("Implement logic to check if we can propagate up");
    return false;
  }
}

package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.BaseRemoveNestedRegionsIntra;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTARemoveNestedRegionsIntra extends BaseRemoveNestedRegionsIntra<Set<FeatureExpr>> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTARemoveNestedRegionsIntra(
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
  protected boolean completelyContainsAll(
      Set<FeatureExpr> callerConstraints, @Nullable Set<FeatureExpr> calleeConstraints) {
    return this.baseIDTAExpander.completelyImplies(callerConstraints, calleeConstraints);
  }
}

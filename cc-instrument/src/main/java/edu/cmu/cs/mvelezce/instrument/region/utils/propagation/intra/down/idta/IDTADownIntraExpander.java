package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.BaseDownIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTADownIntraExpander extends BaseDownIntraExpander<Set<FeatureExpr>> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTADownIntraExpander(
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
  protected boolean containsAll(Set<FeatureExpr> downData, Set<FeatureExpr> thisConstraints) {
    return this.baseIDTAExpander.containsAll(downData, thisConstraints);
  }

  @Override
  protected Set<FeatureExpr> mergeData(
      Set<FeatureExpr> thisConstraints, @Nullable Set<FeatureExpr> downConstraints) {
    return this.baseIDTAExpander.mergeData(thisConstraints, downConstraints);
  }

  @Override
  protected boolean canExpandDown(
      @Nullable Set<FeatureExpr> thisConstraints, @Nullable Set<FeatureExpr> downConstraints) {
    return this.baseIDTAExpander.canExpandConstraints(thisConstraints, downConstraints);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Set<FeatureExpr> constraints = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintConstraints(constraints, options);
  }
}

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

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTADownExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.baseIDTAExpander = BaseIDTAExpander.getInstance();
    this.baseIDTAExpander.init(regionsToData.values());
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

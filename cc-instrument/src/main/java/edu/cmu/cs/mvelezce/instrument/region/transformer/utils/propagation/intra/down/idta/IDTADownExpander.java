package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.down.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.down.BaseDownExpander;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra.idta.BaseIDTAExpander;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTADownExpander extends BaseDownExpander<Set<FeatureExpr>> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTADownExpander(
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData) {
    super(options, blockRegionMatcher, regionsToData);

    this.baseIDTAExpander = BaseIDTAExpander.getInstance();
    this.baseIDTAExpander.init(regionsToData.values());
  }

  @Override
  protected boolean containsAll(Set<FeatureExpr> downData, Set<FeatureExpr> thisData) {
    return this.baseIDTAExpander.containsAll(downData, thisData);
  }

  @Override
  protected Set<FeatureExpr> mergeData(
      Set<FeatureExpr> thisData, @Nullable Set<FeatureExpr> downData) {
    Set<FeatureExpr> newConstraints = new HashSet<>(thisData);

    if (downData == null) {
      return newConstraints;
    }

    newConstraints.addAll(downData);

    return newConstraints;
  }

  @Override
  protected boolean canExpandDown(
      @Nullable Set<FeatureExpr> thisData, @Nullable Set<FeatureExpr> downData) {
    return this.baseIDTAExpander.canExpandConstraints(thisData, downData);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Set<FeatureExpr> constraints = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintConstraints(constraints, options);
  }
}

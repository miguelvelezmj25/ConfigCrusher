package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter.BaseInterExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import soot.jimple.toolkits.callgraph.CallGraph;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class BaseIDTAInterExpander extends BaseInterExpander<Set<FeatureExpr>> {

  private final BaseIDTAExpander baseIDTAExpander;

  public BaseIDTAInterExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      CallGraph callGraph,
      SootAsmMethodMatcher sootAsmMethodMatcher,
      BaseIDTAExpander baseIDTAExpander) {
    super(
        programName,
        debugDir,
        options,
        blockRegionMatcher,
        regionsToData,
        callGraph,
        sootAsmMethodMatcher);

    this.baseIDTAExpander = baseIDTAExpander;
  }

  @Override
  protected boolean canExpandDataUp(
      Set<FeatureExpr> firstRegionData, @Nullable Set<FeatureExpr> callerData) {
    return this.baseIDTAExpander.canMergeConstraints(firstRegionData, callerData);
  }

  @Override
  protected Set<FeatureExpr> mergeData(
      Set<FeatureExpr> firstRegionData, @Nullable Set<FeatureExpr> callerData) {
    return this.baseIDTAExpander.mergeData(firstRegionData, callerData);
  }

  @Override
  protected boolean containsAll(
      @Nullable Set<FeatureExpr> callerData, Set<FeatureExpr> firstRegionData) {
    return this.baseIDTAExpander.impliesAll(callerData, firstRegionData);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    throw new UnsupportedOperationException("Implement");
  }
}

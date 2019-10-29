package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.BaseInterAnalysisUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter.BaseRemoveNestedRegionsInter;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import soot.jimple.toolkits.callgraph.CallGraph;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTARemoveNestedRegionsInter extends BaseRemoveNestedRegionsInter<Set<FeatureExpr>> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTARemoveNestedRegionsInter(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      SootAsmMethodMatcher sootAsmMethodMatcher,
      CallGraph callGraph,
      BaseInterAnalysisUtils<Set<FeatureExpr>> baseInterAnalysisUtils,
      BaseIDTAExpander baseIDTAExpander) {
    super(
        programName,
        debugDir,
        options,
        blockRegionMatcher,
        regionsToData,
        sootAsmMethodMatcher,
        callGraph,
        baseInterAnalysisUtils);

    this.baseIDTAExpander = baseIDTAExpander;
  }

  @Override
  protected boolean coversAll(
      Set<FeatureExpr> callerConstraints, @Nullable Set<FeatureExpr> calleeConstraints) {
    if (calleeConstraints == null) {
      return true;
    }

    return this.baseIDTAExpander.impliesAll(callerConstraints, calleeConstraints);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Set<FeatureExpr> constraints = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintConstraints(constraints, options);
  }
}

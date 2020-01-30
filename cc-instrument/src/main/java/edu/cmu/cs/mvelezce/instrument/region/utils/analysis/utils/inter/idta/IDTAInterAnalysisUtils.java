package edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.BaseInterAnalysisUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import soot.jimple.toolkits.callgraph.CallGraph;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTAInterAnalysisUtils extends BaseInterAnalysisUtils<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTAInterAnalysisUtils(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Partitioning> regionsToData,
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
      Partitioning firstRegionData, @Nullable Partitioning callerData) {
    return this.baseIDTAExpander.canMergePartitionings(firstRegionData, callerData);
  }

  @Override
  protected boolean coversAll(
      Partitioning callerDataCriteriaToRemoveNestedPartitioning,
      Partitioning currentCallerPartitioning) {
    return this.baseIDTAExpander.impliesAll(
        callerDataCriteriaToRemoveNestedPartitioning, currentCallerPartitioning);
  }
}

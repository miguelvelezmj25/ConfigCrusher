package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter.idta;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.BaseInterAnalysisUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter.BaseRemoveNestedRegionsInter;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import soot.jimple.toolkits.callgraph.CallGraph;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTARemoveNestedRegionsInter extends BaseRemoveNestedRegionsInter<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTARemoveNestedRegionsInter(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Partitioning> regionsToData,
      SootAsmMethodMatcher sootAsmMethodMatcher,
      CallGraph callGraph,
      BaseInterAnalysisUtils<Partitioning> baseInterAnalysisUtils,
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
      Partitioning callerPartitioning, @Nullable Partitioning calleePartitioning) {
    if (calleePartitioning == null) {
      return true;
    }

    return this.baseIDTAExpander.impliesAll(callerPartitioning, calleePartitioning);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Partitioning partitioning = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintPartitions(partitioning, options);
  }
}

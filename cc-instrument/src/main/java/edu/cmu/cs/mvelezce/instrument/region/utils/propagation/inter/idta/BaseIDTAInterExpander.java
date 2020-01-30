package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.BaseInterAnalysisUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter.BaseInterExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class BaseIDTAInterExpander extends BaseInterExpander<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public BaseIDTAInterExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Partitioning> regionsToData,
      BaseInterAnalysisUtils<Partitioning> baseInterAnalysisUtils,
      SootAsmMethodMatcher sootAsmMethodMatcher,
      BaseIDTAExpander baseIDTAExpander) {
    super(
        programName,
        debugDir,
        options,
        blockRegionMatcher,
        regionsToData,
        baseInterAnalysisUtils,
        sootAsmMethodMatcher);

    this.baseIDTAExpander = baseIDTAExpander;
  }

  @Override
  protected Partitioning mergeData(
      Partitioning firstRegionData, @Nullable Partitioning callerData) {
    return this.baseIDTAExpander.mergeData(firstRegionData, callerData);
  }
}

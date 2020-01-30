package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.BaseRemoveNestedRegionsIntra;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTARemoveNestedRegionsIntra extends BaseRemoveNestedRegionsIntra<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTARemoveNestedRegionsIntra(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Partitioning> regionsToData,
      BaseIDTAExpander baseIDTAExpander) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.baseIDTAExpander = baseIDTAExpander;
  }

  @Override
  protected boolean coversAll(
      Partitioning callerPartitioning, @Nullable Partitioning calleePartitioning) {
    return this.baseIDTAExpander.impliesAll(callerPartitioning, calleePartitioning);
  }
}

package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.down.BaseDownIntraExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTADownIntraExpander extends BaseDownIntraExpander<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTADownIntraExpander(
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
  protected Partitioning mergeData(
      Partitioning regionPartitioning, @Nullable Partitioning downPartitioning) {
    return this.baseIDTAExpander.mergeData(regionPartitioning, downPartitioning);
  }

  @Override
  protected boolean canExpandDataDown(
      Partitioning expandingPartitioning, @Nullable Partitioning downPartitioning) {
    return this.baseIDTAExpander.canMergePartitionings(expandingPartitioning, downPartitioning);
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Partitioning partitioning = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintPartitions(partitioning, options);
  }
}

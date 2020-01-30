package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up.BaseUpIntraExpander;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTAUpIntraExpander extends BaseUpIntraExpander<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTAUpIntraExpander(
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
  protected String getPrettyData(@Nullable JavaRegion region) {
    Partitioning partitioning = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintPartitions(partitioning, options);
  }

  @Override
  protected boolean canExpandDataUp(
      Partitioning expandingPartitioning, @Nullable Partitioning upPartitioning) {
    return this.baseIDTAExpander.canMergePartitionings(expandingPartitioning, upPartitioning);
  }

  @Override
  protected Partitioning mergeData(
      Partitioning expandingPartitioning, @Nullable Partitioning upPartitioning) {
    return this.baseIDTAExpander.mergeData(expandingPartitioning, upPartitioning);
  }
}

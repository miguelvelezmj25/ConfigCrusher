package edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra.BaseRemoveNestedRegionsIntra;
import edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter.BaseStartEndRegionBlocksSetter;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IDTAStartEndRegionBlocksSetter extends BaseStartEndRegionBlocksSetter<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;

  public IDTAStartEndRegionBlocksSetter(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Partitioning> regionsToData,
      BaseRemoveNestedRegionsIntra<Partitioning> baseRemoveNestedRegionsIntra,
      BaseIDTAExpander baseIDTAExpander) {
    super(
        programName,
        debugDir,
        options,
        blockRegionMatcher,
        regionsToData,
        baseRemoveNestedRegionsIntra);

    this.baseIDTAExpander = baseIDTAExpander;
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Partitioning partitioning = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintPartitions(partitioning, options);
  }
}

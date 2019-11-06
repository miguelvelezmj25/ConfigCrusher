package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.intra;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseRemoveNestedRegionsIntra<T> extends BlockRegionAnalyzer<T> {

  public BaseRemoveNestedRegionsIntra(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);
  }

  protected abstract boolean coversAll(T coveringData, @Nullable T regionData);

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  protected String debugFileName(String methodName) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  public void removeCoveredNestedRegions(
      MethodBlock start,
      Set<MethodBlock> endBlocks,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      T regionData) {
    Set<MethodBlock> reachables = new HashSet<>();

    for (MethodBlock endBlock : endBlocks) {
      reachables.addAll(graph.getReachableBlocks(start, endBlock));
    }

    reachables.removeAll(endBlocks);

    // If the ends are connected to the exit block, we want to analyze them
    MethodBlock exitBlock = graph.getExitBlock();
    for (MethodBlock endBlock : endBlocks) {
      Set<MethodBlock> endBlockSuccs = endBlock.getSuccessors();

      if (endBlockSuccs.size() == 1 && endBlockSuccs.iterator().next().equals(exitBlock)) {
        reachables.add(endBlock);
      }
    }

    reachables.remove(start);
    this.removeCoveredRegionsIntra(reachables, blocksToRegions, regionData);
  }

  private void removeCoveredRegionsIntra(
      Set<MethodBlock> reachables,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      T regionData) {
    for (MethodBlock reachable : reachables) {
      JavaRegion reachableRegion = blocksToRegions.get(reachable);

      if (reachableRegion == null) {
        continue;
      }

      T reachableData = this.getData(reachableRegion);

      boolean equals = regionData.equals(reachableData);
      boolean coversAll = this.coversAll(regionData, reachableData);

      if (equals != coversAll) {
        throw new RuntimeException("The equals and covers all checks do not match");
      }

      if (!coversAll) {
        continue;
      }

      blocksToRegions.put(reachable, null);
      this.removeRegionToData(reachableRegion);
    }
  }
}

package edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseStartEndRegionBlocksSetter<T> extends BlockRegionAnalyzer<T> {

  public BaseStartEndRegionBlocksSetter(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);
  }

  @Override
  protected String debugFileName(String methodName) {
    return "setStartEndRegion/" + methodName;
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    //    System.out.println(graph.toDotString("test"));
    region.setStartMethodBlock(block);
    this.setRegionEndBlocks(block, region, graph, blocksToRegions);

    return new HashSet<>();
  }

  private void setRegionEndBlocks(
      MethodBlock startBlock,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Set<MethodBlock> succBlocks = startBlock.getSuccessors();
    MethodBlock exit = graph.getExitBlock();

    //    if (succBlocks.size() == 1 && succBlocks.iterator().next().equals(exit)) {
    //      throw new UnsupportedOperationException("Implement");
    //    }

    MethodBlock ipd = this.getIPD(startBlock, region, graph, blocksToRegions);
    Set<MethodBlock> ends = this.getEndBlocks(startBlock, ipd, graph);
    region.setEndMethodBlocks(ends);

    this.removeCoveredNestedRegions(startBlock, ends, graph, blocksToRegions, this.getData(region));
  }

  private MethodBlock getIPD(
      MethodBlock startBlock,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    MethodBlock exit = graph.getExitBlock();
    MethodBlock ipd = graph.getImmediatePostDominator(startBlock);
    JavaRegion ipdRegion = blocksToRegions.get(ipd);
    T ipdData = this.getData(ipdRegion);
    T regionData = this.getData(region);

    if (regionData == null) {
      throw new RuntimeException("The data at this region cannot be null");
    }

    while (!ipd.equals(exit) && regionData.equals(ipdData)) {
      ipd = graph.getImmediatePostDominator(ipd);
      ipdRegion = blocksToRegions.get(ipd);
      ipdData = this.getData(ipdRegion);
    }

    return ipd;
  }

  private void removeCoveredNestedRegions(
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
    reachables.add(start);

    // If the ends are connected to the exit block, we want to analyze them
    MethodBlock exitBlock = graph.getExitBlock();
    for (MethodBlock endBlock : endBlocks) {
      Set<MethodBlock> endBlockSuccs = endBlock.getSuccessors();

      if (endBlockSuccs.size() == 1 && endBlockSuccs.iterator().next().equals(exitBlock)) {
        reachables.add(endBlock);
      }
    }

    //    this.removeCoveredRegionsInter(reachables);
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

      if (!regionData.equals(reachableData)) {
        continue;
      }

      blocksToRegions.put(reachable, null);
      this.removeRegionToData(reachableRegion);
    }
  }

  private Set<MethodBlock> getEndBlocks(MethodBlock start, MethodBlock ipd, MethodGraph graph) {
    Set<MethodBlock> ends = new HashSet<>();

    if (start == ipd) {
      throw new RuntimeException("Start and end equal");
    }

    if (graph.getExitBlock().equals(ipd)) {
      //      this.getEndRegionBlocksWithReturn().addAll(ipd.getPredecessors());
      ends.addAll(ipd.getPredecessors());
    } else if (start.getSuccessors().size() == 1
        && start.getSuccessors().iterator().next().equals(ipd)) {
      //      ends.add(start);
      //      if (graph.getExitBlock() == ipd) {
      //        this.getEndRegionBlocksWithReturn().add(start);
      //      }
      throw new RuntimeException("Handle case");
    } else {
      ends.add(ipd);
    }

    return ends;
  }
}

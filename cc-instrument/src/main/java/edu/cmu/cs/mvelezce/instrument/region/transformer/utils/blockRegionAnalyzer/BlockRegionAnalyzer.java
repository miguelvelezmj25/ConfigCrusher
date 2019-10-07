package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionAnalyzer;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BlockRegionAnalyzer<T> {

  private final Map<JavaRegion, T> regionsToData;
  private final BlockRegionMatcher blockRegionMatcher;

  public BlockRegionAnalyzer(
      BlockRegionMatcher blockRegionMatcher, Map<JavaRegion, T> regionsToData) {
    this.blockRegionMatcher = blockRegionMatcher;
    this.regionsToData = regionsToData;
  }

  public void processBlocks(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.blockRegionMatcher.getMethodNodesToRegionsInBlocks().get(methodNode);

    for (Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
      MethodBlock block = blockToRegion.getKey();

      if (graph.getEntryBlock().equals(block)) {
        continue;
      }

      JavaRegion region = blockToRegion.getValue();

      if (region == null) {
        continue;
      }

      this.processBlock(block, region, graph, blocksToRegions);
    }
  }

  protected T getData(JavaRegion region) {
    return this.regionsToData.get(region);
  }

  protected abstract void processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions);
}

package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionAnalyzer;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nullable;
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

    //    this.debug()
  }

  //    Set<MethodBlock> blocks = graph.getBlocks();
  //
  //    //        if(methodNode.name.equals("init")) {
  //    //            System.out.println();
  //    //        }
  //    //
  //    //        if(methodNode.name.equals("<init>")) {
  //    //            System.out.println();
  //    //        }
  //
  //    StringBuilder dotString = new StringBuilder("digraph " + methodNode.name + " {\n");
  //    dotString.append("node [shape=record];\n");
  //
  //    for (MethodBlock block : blocks) {
  //      dotString.append(block.getID());
  //      dotString.append(" [label=\"");
  //      dotString.append(block.getID());
  //      dotString.append(" - ");
  //
  //      JavaRegion region = blocksToRegions.get(block);
  //
  //      if (region == null) {
  //        dotString.append("[]");
  //      } else {
  //        T decision = this.getData(region);
  //        dotString.append(decision);
  //      }
  //
  //      dotString.append("\"];\n");
  //    }
  //
  //    dotString.append(graph.getEntryBlock().getID());
  //    dotString.append(";\n");
  //    dotString.append(graph.getExitBlock().getID());
  //    dotString.append(";\n");
  //
  //    for (MethodBlock methodBlock : graph.getBlocks()) {
  //      for (MethodBlock successor : methodBlock.getSuccessors()) {
  //        dotString.append(methodBlock.getID());
  //        dotString.append(" -> ");
  //        dotString.append(successor.getID());
  //        dotString.append(";\n");
  //      }
  //    }
  //
  //    dotString.append("}");
  //
  //    System.out.println(dotString);
  //    System.out.println();

  @Nullable
  protected T getData(@Nullable JavaRegion region) {
    return this.regionsToData.get(region);
  }

  protected void addRegionToData(JavaRegion region, @Nullable T data) {
    this.regionsToData.put(region, data);
  }

  protected abstract void processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions);
}

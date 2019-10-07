package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionAnalyzer;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BlockRegionAnalyzer<T> {

  private final Set<String> options;
  private final Map<JavaRegion, T> regionsToData;
  private final BlockRegionMatcher blockRegionMatcher;

  public BlockRegionAnalyzer(
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData) {
    this.options = options;
    this.blockRegionMatcher = blockRegionMatcher;
    this.regionsToData = regionsToData;
  }

  public void processBlocks(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.blockRegionMatcher.getMethodNodesToRegionsInBlocks().get(methodNode);
    Queue<MethodBlock> worklist = new ArrayDeque<>(blocksToRegions.keySet());

    while (!worklist.isEmpty()) {
      MethodBlock block = worklist.remove();

      if (graph.getEntryBlock().equals(block) || graph.getExitBlock().equals(block)) {
        continue;
      }

      JavaRegion region = blocksToRegions.get(block);

      if (region == null) {
        continue;
      }

      Set<MethodBlock> updatedBlocks = this.processBlock(block, region, graph, blocksToRegions);
      worklist.addAll(updatedBlocks);
    }

    this.debugBlockData(methodNode, graph, blocksToRegions);
  }

  private void debugBlockData(
      MethodNode methodNode,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Set<MethodBlock> blocks = graph.getBlocks();

    StringBuilder dotString = new StringBuilder("digraph " + methodNode.name + " {\n");
    dotString.append("node [shape=record];\n");

    for (MethodBlock block : blocks) {
      dotString.append(block.getID());
      dotString.append(" [label=\"");
      dotString.append(block.getID());
      dotString.append(" - ");

      JavaRegion region = blocksToRegions.get(block);

      if (region == null) {
        dotString.append("[]");
      } else {
        String prettyData = this.getPrettyData(region);
        dotString.append(prettyData);
      }

      dotString.append("\"];\n");
    }

    dotString.append(graph.getEntryBlock().getID());
    dotString.append(";\n");
    dotString.append(graph.getExitBlock().getID());
    dotString.append(";\n");

    for (MethodBlock methodBlock : graph.getBlocks()) {
      for (MethodBlock successor : methodBlock.getSuccessors()) {
        dotString.append(methodBlock.getID());
        dotString.append(" -> ");
        dotString.append(successor.getID());
        dotString.append(";\n");
      }
    }

    dotString.append("}");

    System.out.println(dotString);
    System.out.println();
  }

  protected Set<String> getOptions() {
    return options;
  }

  protected abstract String getPrettyData(@Nullable JavaRegion region);

  @Nullable
  protected T getData(@Nullable JavaRegion region) {
    return this.regionsToData.get(region);
  }

  protected void addRegionToData(JavaRegion region, @Nullable T data) {
    this.regionsToData.put(region, data);
  }

  protected abstract Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions);
}

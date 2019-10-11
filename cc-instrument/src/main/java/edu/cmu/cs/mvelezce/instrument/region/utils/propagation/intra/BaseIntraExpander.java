package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseIntraExpander<T> extends BlockRegionAnalyzer<T> {

  public BaseIntraExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);
  }

  @Override
  protected String debugFileName(String methodName) {
    return "expandData/" + methodName;
  }

  public void validateAllBlocksHaveRegions(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);
    LinkedHashMap<MethodBlock, JavaRegion> regionsInBlocks =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(methodNode);

    for (Map.Entry<MethodBlock, JavaRegion> entry : regionsInBlocks.entrySet()) {
      MethodBlock block = entry.getKey();

      if (block.getSuccessors().isEmpty() && block.getPredecessors().isEmpty()) {
        continue;
      }

      if (block.equals(graph.getEntryBlock()) || block.equals(graph.getExitBlock())) {
        continue;
      }

      if (entry.getValue() == null) {
        throw new RuntimeException(
            "The block "
                + block.getID()
                + " in "
                + classNode.name
                + " - "
                + methodNode.name
                + " does not have a region");
      }
    }
  }
}

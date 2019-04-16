package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.InvalidGraphException;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm.CFGBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class BlockRegionMatcher {

  private final InstructionRegionMatcher instructionRegionMatcher;

  BlockRegionMatcher(InstructionRegionMatcher instructionRegionMatcher) {
    this.instructionRegionMatcher = instructionRegionMatcher;
  }

  LinkedHashMap<MethodBlock, JavaRegion> matchBlocksToRegion(MethodNode methodNode,
      ClassNode classNode, List<JavaRegion> regionsInMethod) {
    MethodGraph graph;

    try {
      graph = CFGBuilder.getCfg(methodNode, classNode);
    }
    catch (InvalidGraphException ige) {
      return new LinkedHashMap<>();
    }

    InsnList instructions = methodNode.instructions;
    List<MethodBlock> blocks = getSortedMethodBlocks(instructions, graph);

    Map<AbstractInsnNode, JavaRegion> instructionsToRegions = this.instructionRegionMatcher
        .matchInstructionToRegion(methodNode, regionsInMethod);

    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = getMethodBlocksToNullRegions(blocks);
    matchRegionsToBlocks(blocksToRegions, instructionsToRegions, graph);

    return blocksToRegions;
  }

  private void matchRegionsToBlocks(LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      Map<AbstractInsnNode, JavaRegion> instructionsToRegions, MethodGraph graph) {
    for (Map.Entry<AbstractInsnNode, JavaRegion> instructionToRegion : instructionsToRegions
        .entrySet()) {
      AbstractInsnNode instruction = instructionToRegion.getKey();

      for (MethodBlock block : blocksToRegions.keySet()) {
        List<AbstractInsnNode> instructions = block.getInstructions();

        if (!instructions.contains(instruction)) {
          continue;
        }

        if (blocksToRegions.get(block) != null) {
          System.err.println(graph.toDotString("Error"));
          System.err.println(block.getID());
          throw new RuntimeException("There cannot be multiple regions in the same block");
        }

        blocksToRegions.put(block, instructionToRegion.getValue());
      }
    }
  }

  private static LinkedHashMap<MethodBlock, JavaRegion> getMethodBlocksToNullRegions(
      List<MethodBlock> blocks) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = new LinkedHashMap<>();

    for (MethodBlock block : blocks) {
      blocksToRegions.put(block, null);
    }

    return blocksToRegions;
  }

  private static List<MethodBlock> getSortedMethodBlocks(
      InsnList instructions, MethodGraph graph) {
    List<MethodBlock> methodBlocks = getMethodBlocks(graph);

    methodBlocks.sort((o1, o2) -> {
      int o1Index = instructions.indexOf(o1.getInstructions().get(0));
      int o2Index = instructions.indexOf(o2.getInstructions().get(0));

      return Integer.compare(o1Index, o2Index);
    });

    methodBlocks.add(0, graph.getEntryBlock());
    methodBlocks.add(graph.getExitBlock());

    return methodBlocks;
  }

  private static List<MethodBlock> getMethodBlocks(MethodGraph graph) {
    List<MethodBlock> methodBlocks = new ArrayList<>(graph.getBlocks());
    methodBlocks.remove(graph.getEntryBlock());
    methodBlocks.remove(graph.getExitBlock());

    return methodBlocks;
  }

}

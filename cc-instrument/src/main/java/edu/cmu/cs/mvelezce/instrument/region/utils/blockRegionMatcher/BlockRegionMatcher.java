package edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.InstructionRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.instrumenter.graph.exception.InvalidGraphException;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class BlockRegionMatcher {

  private final Set<JavaRegion> regions;
  private final InstructionRegionMatcher instructionRegionMatcher;
  private final Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>>
      methodNodesToRegionsInBlocks = new HashMap<>();

  public BlockRegionMatcher(
      InstructionRegionMatcher instructionRegionMatcher, Set<JavaRegion> regions) {
    this.instructionRegionMatcher = instructionRegionMatcher;
    this.regions = regions;
  }

  public void matchBlocksToRegions(MethodNode methodNode, ClassNode classNode) {
    Set<JavaRegion> regionsInMethod =
        InstrumenterUtils.getRegionsInMethod(methodNode, classNode, this.regions);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = new LinkedHashMap<>();

    try {
      MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);
      blocksToRegions =
          this.matchBlocksToRegions(methodNode, graph, new ArrayList<>(regionsInMethod));
    } catch (InvalidGraphException ignored) {
      System.err.println(
          "Is there a better way to implement this logic without ignoring the exception?");
    }

    this.methodNodesToRegionsInBlocks.put(methodNode, blocksToRegions);
  }

  public Map<MethodNode, LinkedHashMap<MethodBlock, JavaRegion>> getMethodNodesToRegionsInBlocks() {
    return methodNodesToRegionsInBlocks;
  }

  private LinkedHashMap<MethodBlock, JavaRegion> matchBlocksToRegions(
      MethodNode methodNode, MethodGraph graph, List<JavaRegion> regionsInMethod) {
    InsnList instructions = methodNode.instructions;
    List<MethodBlock> blocks = getSortedMethodBlocks(instructions, graph);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = getMethodBlocksToNullRegions(blocks);

    Map<AbstractInsnNode, JavaRegion> instructionsToRegions =
        this.instructionRegionMatcher.matchInstructionToRegion(methodNode, regionsInMethod);
    matchRegionsToBlocks(blocksToRegions, instructionsToRegions, graph);

    return blocksToRegions;
  }

  private LinkedHashMap<MethodBlock, JavaRegion> getMethodBlocksToNullRegions(
      List<MethodBlock> blocks) {
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = new LinkedHashMap<>();

    for (MethodBlock block : blocks) {
      blocksToRegions.put(block, null);
    }

    return blocksToRegions;
  }

  private List<MethodBlock> getSortedMethodBlocks(InsnList instructions, MethodGraph graph) {
    List<MethodBlock> methodBlocks = getMethodBlocks(graph);

    methodBlocks.sort(
        (o1, o2) -> {
          int o1Index = instructions.indexOf(o1.getInstructions().get(0));
          int o2Index = instructions.indexOf(o2.getInstructions().get(0));

          return Integer.compare(o1Index, o2Index);
        });

    methodBlocks.add(0, graph.getEntryBlock());
    methodBlocks.add(graph.getExitBlock());

    return methodBlocks;
  }

  private List<MethodBlock> getMethodBlocks(MethodGraph graph) {
    List<MethodBlock> methodBlocks = new ArrayList<>(graph.getBlocks());
    methodBlocks.remove(graph.getEntryBlock());
    methodBlocks.remove(graph.getExitBlock());

    return methodBlocks;
  }

  private void matchRegionsToBlocks(
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      Map<AbstractInsnNode, JavaRegion> instructionsToRegions,
      MethodGraph graph) {
    for (Map.Entry<AbstractInsnNode, JavaRegion> instructionToRegion :
        instructionsToRegions.entrySet()) {
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
}

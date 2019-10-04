package edu.cmu.cs.mvelezce.instrument.region.transformer;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.instructionRegionMatcher.InstructionRegionMatcher;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.BaseMethodTransformer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class RegionTransformer<T> extends BaseMethodTransformer {

  private final Map<JavaRegion, T> regionsToData;
  private final BlockRegionMatcher blockRegionMatcher;

  public RegionTransformer(
      String programName,
      ClassTransformer classTransformer,
      String mainClass,
      boolean debug,
      Map<JavaRegion, T> regionsToData,
      InstructionRegionMatcher instructionRegionMatcher) {
    super(programName, classTransformer, mainClass, debug);

    this.regionsToData = regionsToData;
    this.blockRegionMatcher =
        new BlockRegionMatcher(instructionRegionMatcher, regionsToData.keySet());
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();
    Set<JavaRegion> regions = this.regionsToData.keySet();

    if (InstrumenterUtils.getRegionsInClass(classNode, regions).isEmpty()) {
      return methodsToInstrument;
    }

    for (MethodNode methodNode : classNode.methods) {
      if (!InstrumenterUtils.getRegionsInMethod(methodNode, classNode, regions).isEmpty()) {
        methodsToInstrument.add(methodNode);
      }
    }

    return methodsToInstrument;
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    this.blockRegionMatcher.matchBlocksToRegions(methodNode, classNode);
    //    this.setStartAndEndRegionBlocks(methodNode, classNode);

    System.out.println();
  }

  //  private void setStartAndEndRegionBlocks(MethodNode methodNode, ClassNode classNode) {
  //    MethodGraph graph = this.getMethodGraph(methodNode, classNode);
  //    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
  //        this.methodNodesToRegionsInBlocks.get(methodNode);
  //
  //    for (Map.Entry<MethodBlock, JavaRegion> blockToRegion : blocksToRegions.entrySet()) {
  //      MethodBlock methodBlock = blockToRegion.getKey();
  //
  //      if (graph.getEntryBlock().equals(methodBlock)) {
  //        continue;
  //      }
  //
  //      JavaRegion region = blockToRegion.getValue();
  //
  //      if (region == null) {
  //        continue;
  //      }
  //
  //      System.out.println();
  //    }
  //  }

  //  private void matchBlocksToRegions(MethodNode methodNode, ClassNode classNode) {
  //    Set<JavaRegion> regionsInMethod =
  //        InstrumenterUtils.getRegionsInMethod(methodNode, classNode,
  // this.regionsToData.keySet());
  //    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions = new LinkedHashMap<>();
  //
  //    try {
  //      MethodGraph graph = this.getMethodGraph(methodNode, classNode);
  //      blocksToRegions =
  //          this.blockRegionMatcher.matchBlocksToRegions(
  //              methodNode, graph, new ArrayList<>(regionsInMethod));
  //    } catch (InvalidGraphException ignored) {
  //      System.err.println(
  //          "Is there a better way to implement this logic without ignoring the exception?");
  //    }
  //
  //    this.methodNodesToRegionsInBlocks.put(methodNode, blocksToRegions);
  //  }
}

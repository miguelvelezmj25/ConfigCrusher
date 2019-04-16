package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.RegionTransformer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public abstract class DynamicRegionTransformer extends RegionTransformer<InfluencingTaints> {

  // TODO delete programName
  private DynamicRegionTransformer(String programName, String entryPoint,
      ClassTransformer classTransformer,
      Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints) {
    super(programName, entryPoint, classTransformer, regionsToInfluencingTaints, true,
        new DynamicInstructionRegionMatcher());
  }

  DynamicRegionTransformer(String programName, String entryPoint, String directory,
      Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints)
      throws NoSuchMethodException, MalformedURLException, IllegalAccessException, InvocationTargetException {
    this(programName, entryPoint, new DefaultClassTransformer(directory),
        regionsToInfluencingTaints);
  }

  @Override
  public void transformMethods(Set<ClassNode> classNodes) {
    this.setBlocksToDecisions(classNodes);

    // TODO propgate in methods, propagate up, repeat
    this.expandRegionsInMethods(classNodes);
  }

  private void expandRegionsInMethods(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      Set<MethodNode> methodsToInstrument = this.getMethodsToInstrument(classNode);

      if (methodsToInstrument.isEmpty()) {
        continue;
      }

      for (MethodNode methodNode : methodsToInstrument) {
        this.expandUpRegionsInMethod(methodNode, classNode,
            this.getMethodsToDecisionsInBlocks().get(methodNode));
//      this.expandDownRegionsInMethods();
      }

    }

  }

  // TODO might be able to abstract this method and most callees to region transformer
  private void expandUpRegionsInMethod(MethodNode methodNode, ClassNode classNode,
      Map<MethodBlock, JavaRegion> blocksToRegions) {
    List<MethodBlock> worklist = new ArrayList<>(blocksToRegions.keySet());

    while (!worklist.isEmpty()) {
      MethodBlock block = worklist.remove(0);

      // Optimization
      if (blocksToRegions.get(block) == null) {
        continue;
      }

      List<MethodBlock> updatedBlocks = this
          .expandUpRegionInMethod(methodNode, classNode, block, blocksToRegions);

      if (updatedBlocks.isEmpty()) {
        continue;
      }

      // Optimization
      worklist.addAll(0, updatedBlocks);
    }

  }

  private List<MethodBlock> expandUpRegionInMethod(MethodNode methodNode, ClassNode classNode,
      MethodBlock block, Map<MethodBlock, JavaRegion> blocksToRegions) {
    List<MethodBlock> updatedBlocks = new ArrayList<>();
    MethodGraph graph = this.getMethodGraph(methodNode, classNode);
    MethodBlock id = graph.getImmediateDominator(block);

    if (id == null || id == graph.getEntryBlock()) {
      return updatedBlocks;
    }

    JavaRegion blockRegion = blocksToRegions.get(block);
    InfluencingTaints blockInfluencingTaints = this.getDecision(blockRegion);

    JavaRegion idRegion = blocksToRegions.get(id);
    InfluencingTaints idInfluencingTaints = this.getDecision(idRegion);

    if (!this.canExpandUpToId(blockInfluencingTaints, idInfluencingTaints)) {
      return updatedBlocks;
    }

    return this.expandUpInfluencingTaintsInMethod(block, blockInfluencingTaints, blocksToRegions);
  }

  private List<MethodBlock> expandUpInfluencingTaintsInMethod(MethodBlock block,
      InfluencingTaints blockInfluencingTaints, Map<MethodBlock, JavaRegion> blocksToRegions) {
    List<MethodBlock> updatedBlocks = new ArrayList<>();
    Set<MethodBlock> blockPreds = block.getPredecessors();

    if (blockPreds.isEmpty()) {
      throw new RuntimeException("The predecessors cannot be empty " + block.getID());
    }

    for (MethodBlock pred : blockPreds) {
      // A block might jump to itself
      if (block == pred) {
        continue;
      }

      JavaRegion predRegion = blocksToRegions.get(pred);
      InfluencingTaints predInfluencingTaints = this.getDecision(predRegion);

      if (!canExpandUpToPred(blockInfluencingTaints, predInfluencingTaints)) {
//        if (pred.isCatchWithImplicitThrow()) {
//          continue;
//        }
//
        throw new RuntimeException(
            "Cannot push up decisions from " + block.getID() + " to " + pred.getID());
      }

      JavaRegion predNewRegion = this
          .addNewRegionToMappingOfBlocksToRegions(block, pred, predRegion, blocksToRegions);
      this.addNewRegionToMappingOfRegionsToData(predNewRegion, blockInfluencingTaints);

      updatedBlocks.add(0, pred);
    }

    return updatedBlocks;
  }

  private void addNewRegionToMappingOfRegionsToData(JavaRegion predNewRegion,
      InfluencingTaints blockInfluencingTaints) {
    InfluencingTaints predInfluencingTaints = new InfluencingTaints(
        blockInfluencingTaints.getContext(), blockInfluencingTaints.getCondition());
    this.getRegionsToData().put(predNewRegion, predInfluencingTaints);
  }

  private JavaRegion addNewRegionToMappingOfBlocksToRegions(MethodBlock block, MethodBlock pred,
      JavaRegion predRegion, Map<MethodBlock, JavaRegion> blocksToRegions) {
    int index;

    if (predRegion == null) {
      index = -1;
    }
    else {
      index = predRegion.getStartRegionIndex();
      this.getRegionsToData().remove(predRegion);
    }

    JavaRegion blockRegion = blocksToRegions.get(block);
    JavaRegion newRegion = new JavaRegion.Builder(blockRegion.getRegionPackage(),
        blockRegion.getRegionClass(), blockRegion.getRegionMethod()).startBytecodeIndex(index)
        .build();

    blocksToRegions.put(pred, newRegion);

    return newRegion;
  }

  private boolean canExpandUpToPred(InfluencingTaints thisInfluencingTaints,
      InfluencingTaints predInfluencingTaints) {
    if (thisInfluencingTaints.equals(predInfluencingTaints)) {
      return false;
    }

    Set<String> thisContextTaints = thisInfluencingTaints.getContext();
    Set<String> predContextTaints = predInfluencingTaints.getContext();

    if (!thisContextTaints.equals(predContextTaints)) {
      throw new RuntimeException("The thisContextTaints do not equal the predContextTaints");
    }

    return thisInfluencingTaints.getCondition().containsAll(predInfluencingTaints.getCondition());
  }

  private boolean canExpandUpToId(InfluencingTaints thisInfluencingTaints,
      InfluencingTaints idInfluencingTaints) {
    if (thisInfluencingTaints.equals(idInfluencingTaints)) {
      return false;
    }

    Set<String> thisContextTaints = thisInfluencingTaints.getContext();
    Set<String> idContextTaints = idInfluencingTaints.getContext();

    if (!thisContextTaints.equals(idContextTaints)) {
      throw new RuntimeException("The blockContextTaints do not equal the idContextTaints");
    }

    return thisInfluencingTaints.getCondition().containsAll(idInfluencingTaints.getCondition());
  }

  private InfluencingTaints getDecision(JavaRegion region) {
    if (region == null) {
      return new InfluencingTaints(new HashSet<>(), new HashSet<>());
    }

    return this.getRegionsToData().get(region);
  }
}

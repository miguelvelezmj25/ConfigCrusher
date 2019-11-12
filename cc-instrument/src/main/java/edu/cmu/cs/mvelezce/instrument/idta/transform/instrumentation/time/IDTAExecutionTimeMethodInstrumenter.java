package edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.time;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.IDTAMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.region.RegionsManager;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.*;

public class IDTAExecutionTimeMethodInstrumenter implements IDTAMethodInstrumenter {

  private static MethodBlock getEndBlock(JavaRegion region) {
    Set<MethodBlock> endBlocks = region.getEndMethodBlocks();

    if (endBlocks.size() != 1) {
      throw new RuntimeException("Expected to only have 1 end block (i.e., the ipd)");
    }

    return endBlocks.iterator().next();
  }

  @Override
  public void instrument(
      MethodNode methodNode,
      ClassNode classNode,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    List<JavaRegion> orderedRegionsInMethod = this.getOrderedRegionsInMethod(blocksToRegions);
    orderedRegionsInMethod.removeIf(Objects::isNull);

    if (orderedRegionsInMethod.isEmpty()) {
      throw new RuntimeException(
          classNode.name + " - " + methodNode.name + " does not have any regions");
    }

    //        if (orderedRegionsInMethod.size() == 1) {
    //    this.instrumentEntireMethod(methodNode, orderedRegionsInMethod.iterator().next());
    //        } else {
    this.instrumentMethod(classNode, methodNode, orderedRegionsInMethod);
    //        }

    methodNode.visitMaxs(200, 200);
  }

  private List<JavaRegion> getOrderedRegionsInMethod(
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    List<JavaRegion> orderedRegions = new ArrayList<>();

    for (Map.Entry<MethodBlock, JavaRegion> entry : blocksToRegions.entrySet()) {
      orderedRegions.add(entry.getValue());
    }

    return orderedRegions;
  }

  //  private void instrumentEntireMethod(MethodNode methodNode, JavaRegion region) {
  //    this.instrumentEntireMethodStart(methodNode, region);
  //    this.instrumentEntireMethodEnd(methodNode, region);
  //  }
  //
  //  private void instrumentEntireMethodEnd(MethodNode methodNode, JavaRegion region) {
  //    Set<MethodBlock> endBlocks = getEndBlock(region).getPredecessors();
  //
  //    for (MethodBlock endBlock : endBlocks) {
  //      this.instrumentEndBlockWithReturn(methodNode, region, endBlock);
  //    }
  //  }

  private boolean isExitMethodInsn(int opcodeLastInsn) {
    return (opcodeLastInsn >= Opcodes.IRETURN && opcodeLastInsn <= Opcodes.RETURN)
        || opcodeLastInsn == Opcodes.RET
        || opcodeLastInsn == Opcodes.ATHROW;
  }

  private InsnList getEndRegionInsnList(JavaRegion region) {
    InsnList instructionsEndRegion = new InsnList();
    instructionsEndRegion.add(new LdcInsnNode(region.getId().toString()));
    instructionsEndRegion.add(
        new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            RegionsManager.INTERNAL_NAME,
            RegionsManager.EXIT_REGION,
            RegionsManager.REGION_DESCRIPTOR,
            false));

    return instructionsEndRegion;
  }

  //  private void instrumentEntireMethodStart(MethodNode methodNode, JavaRegion region) {
  //    InsnList startRegionInsnList = this.getStartRegionInsnList(region);
  //
  //    InsnList insnList = methodNode.instructions;
  //    AbstractInsnNode firstInsn = insnList.getFirst();
  //    insnList.insert(firstInsn, startRegionInsnList);
  //  }

  private InsnList getStartRegionInsnList(JavaRegion region) {
    InsnList instructionsStartRegion = new InsnList();
    instructionsStartRegion.add(new LdcInsnNode(region.getId().toString()));
    instructionsStartRegion.add(
        new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            RegionsManager.INTERNAL_NAME,
            RegionsManager.ENTER_REGION,
            RegionsManager.REGION_DESCRIPTOR,
            false));

    return instructionsStartRegion;
  }

  private void instrumentMethod(
      ClassNode classNode, MethodNode methodNode, List<JavaRegion> orderedRegionsInMethod) {
    this.instrumentSameBlock(classNode, methodNode, orderedRegionsInMethod);
    this.instrumentNormal(classNode, methodNode, orderedRegionsInMethod);
  }

  private void instrumentNormal(
      ClassNode classNode, MethodNode methodNode, List<JavaRegion> orderedRegionsInMethod) {
    List<JavaRegion> reversedOrderedRegions = new ArrayList<>(orderedRegionsInMethod);
    Collections.reverse(reversedOrderedRegions);

    for (JavaRegion region : reversedOrderedRegions) {
      if (this.startAndEndInSameBlock(classNode, methodNode, region)) {
        continue;
      }

      this.instrumentNormalStart(methodNode, region);
      methodNode.visitMaxs(200, 200);
    }

    for (JavaRegion region : orderedRegionsInMethod) {
      if (this.startAndEndInSameBlock(classNode, methodNode, region)) {
        continue;
      }

      this.instrumentNormalEnd(classNode, methodNode, region);
      methodNode.visitMaxs(200, 200);
    }
  }

  private void instrumentNormalEnd(ClassNode classNode, MethodNode methodNode, JavaRegion region) {
    MethodBlock startBlock = region.getStartMethodBlock();
    MethodBlock ipd = getEndBlock(region);
    boolean startIsPred = false;

    for (MethodBlock endBlock : ipd.getPredecessors()) {
      if (endBlock.equals(startBlock)) {
        startIsPred = true;

        break;
      }
    }

    if (startIsPred) {
      this.instrumentEndIPD(methodNode, region, ipd);
    } else {
      this.instrumentEndNormal(classNode, methodNode, region, ipd);
    }
  }

  private void instrumentEndNormal(
      ClassNode classNode, MethodNode methodNode, JavaRegion region, MethodBlock ipd) {
    MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);
    Set<MethodBlock> reachables = graph.getReachableBlocks(region.getStartMethodBlock(), ipd);

    for (MethodBlock pred : ipd.getPredecessors()) {
      if (!reachables.contains(pred)) {
        continue;
      }

      if (pred.isWithReturn() || pred.isWithExplicitThrow() || pred.isWithLastInstruction()) {
        this.instrumentEndBlockWithReturn(methodNode, region, pred);
      } else {
        this.instrumentEndRegularBlock(methodNode, region, pred);
      }
    }
  }

  private void instrumentEndIPD(MethodNode methodNode, JavaRegion region, MethodBlock ipd) {
    if (ipd.isSpecial()) {
      System.out.println();
    }
    AbstractInsnNode endBlockInsn = ipd.getInstructions().get(0);
    InsnList endRegionInsnList = this.getEndRegionInsnList(region);

    InsnList insnList = methodNode.instructions;
    insnList.insert(endBlockInsn, endRegionInsnList);
  }

  private void instrumentEndRegularBlock(
      MethodNode methodNode, JavaRegion region, MethodBlock endBlock) {
    List<AbstractInsnNode> endBlockInsnList = endBlock.getInstructions();
    AbstractInsnNode lastInsn = endBlockInsnList.get(endBlockInsnList.size() - 1);

    if (lastInsn instanceof JumpInsnNode
        || lastInsn instanceof LookupSwitchInsnNode
        || lastInsn instanceof TableSwitchInsnNode) {
      lastInsn = endBlockInsnList.get(endBlockInsnList.size() - 2);
    }

    InsnList insnList = methodNode.instructions;
    InsnList endRegionInsnList = this.getEndRegionInsnList(region);
    insnList.insert(lastInsn, endRegionInsnList);
  }

  private void instrumentEndBlockWithReturn(
      MethodNode methodNode, JavaRegion region, MethodBlock endBlock) {
    InsnList insnList = methodNode.instructions;
    List<AbstractInsnNode> blockInstructions = endBlock.getInstructions();
    AbstractInsnNode lastInsn = blockInstructions.get(blockInstructions.size() - 1);
    int opcodeLastInsn = lastInsn.getOpcode();

    if (this.isExitMethodInsn(opcodeLastInsn)) {
      InsnList endInstructions = this.getEndRegionInsnList(region);
      insnList.insertBefore(lastInsn, endInstructions);
    } else {
      // Some blocks might have a label node at the end of the block
      lastInsn = blockInstructions.get(blockInstructions.size() - 2);
      opcodeLastInsn = lastInsn.getOpcode();

      if (this.isExitMethodInsn(opcodeLastInsn)) {
        InsnList endInstructions = this.getEndRegionInsnList(region);
        insnList.insertBefore(lastInsn, endInstructions);
      } else {
        //          if(!this.getMethodsToGraphs().get(methodNode).isWithWhileTrue()) {
        //            throw new RuntimeException("The last instruction in a method with return is
        // not
        // a return instruction");
        //          }
        throw new RuntimeException("Handle this case");
      }
    }
  }

  private void instrumentNormalStart(MethodNode methodNode, JavaRegion region) {
    MethodBlock startBlock = region.getStartMethodBlock();
    AbstractInsnNode firstStartInsn = startBlock.getInstructions().get(0);
    InsnList insnList = methodNode.instructions;

    if (!insnList.contains(firstStartInsn)) {
      throw new RuntimeException(
          "The first instruction of this block is not in the method node instructions");
    }

    InsnList startRegionInsnList = this.getStartRegionInsnList(region);
    insnList.insert(firstStartInsn, startRegionInsnList);
  }

  private void instrumentSameBlock(
      ClassNode classNode, MethodNode methodNode, List<JavaRegion> orderedRegionsInMethod) {
    for (JavaRegion region : orderedRegionsInMethod) {
      if (!this.startAndEndInSameBlock(classNode, methodNode, region)) {
        continue;
      }

      this.instrumentFirstInsn(methodNode, region, region.getStartMethodBlock());
      this.instrumentLastInsn(methodNode, region, region.getStartMethodBlock());
      methodNode.visitMaxs(200, 200);
    }
  }

  private void instrumentFirstInsn(MethodNode methodNode, JavaRegion region, MethodBlock block) {
    InsnList startRegionInsnList = this.getStartRegionInsnList(region);

    InsnList insnList = methodNode.instructions;
    AbstractInsnNode firstInsn = block.getInstructions().get(0);
    insnList.insert(firstInsn, startRegionInsnList);
  }

  private void instrumentLastInsn(MethodNode methodNode, JavaRegion region, MethodBlock block) {
    InsnList startRegionInsnList = this.getEndRegionInsnList(region);

    AbstractInsnNode lastInstruction =
        block.getInstructions().get(block.getInstructions().size() - 1);

    if (block.isWithReturn() || block.isWithExplicitThrow() || block.isWithLastInstruction()) {
      lastInstruction = lastInstruction.getPrevious();
    }

    InsnList insnList = methodNode.instructions;
    insnList.insertBefore(lastInstruction, startRegionInsnList);
  }

  private boolean startAndEndInSameBlock(
      ClassNode classNode, MethodNode methodNode, JavaRegion region) {
    MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);
    MethodBlock start = region.getStartMethodBlock();
    MethodBlock end = getEndBlock(region);

    Set<MethodBlock> reachables = graph.getReachableBlocks(start, end);

    return reachables.size() == 2;
  }
}

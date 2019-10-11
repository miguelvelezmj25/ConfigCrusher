package edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.time;

import edu.cmu.cs.mvelezce.analysis.region.RegionsManager;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.IDTAMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.*;

public class IDTAExecutionTimeMethodInstrumenter implements IDTAMethodInstrumenter {

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

    if (orderedRegionsInMethod.size() == 1) {
      this.instrumentEntireMethod(methodNode, orderedRegionsInMethod.iterator().next());
    } else {
      this.instrumentMethod(methodNode, orderedRegionsInMethod);
    }

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

  private void instrumentEntireMethod(MethodNode methodNode, JavaRegion region) {
    this.instrumentEntireMethodStart(methodNode, region);
    this.instrumentEntireMethodEnd(methodNode, region);
  }

  private void instrumentEntireMethodEnd(MethodNode methodNode, JavaRegion region) {
    InsnList insnList = methodNode.instructions;
    Set<MethodBlock> endBlocks = region.getEndMethodBlocks();

    for (MethodBlock endBlock : endBlocks) {
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
  }

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

  private void instrumentEntireMethodStart(MethodNode methodNode, JavaRegion region) {
    InsnList startRegionInsnList = this.getStartRegionInsnList(region);

    InsnList insnList = methodNode.instructions;
    AbstractInsnNode firstInsn = insnList.getFirst();
    insnList.insert(firstInsn, startRegionInsnList);
  }

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

  private void instrumentMethod(MethodNode methodNode, List<JavaRegion> orderedRegionsInMethod) {
    this.instrumentSameBlock(methodNode, orderedRegionsInMethod);
    this.instrumentNormal(methodNode, orderedRegionsInMethod);
  }

  private void instrumentNormal(MethodNode methodNode, List<JavaRegion> orderedRegionsInMethod) {
    List<JavaRegion> reversedOrderedRegions = new ArrayList<>(orderedRegionsInMethod);
    Collections.reverse(reversedOrderedRegions);

    for (JavaRegion region : reversedOrderedRegions) {
      this.instrumentNormalStart(methodNode, region);
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

  private void instrumentSameBlock(MethodNode methodNode, List<JavaRegion> orderedRegionsInMethod) {
    for (JavaRegion region : orderedRegionsInMethod) {
      if (this.startAndEndInSameBlock(region)) {
        throw new UnsupportedOperationException("Handle this case");
      }
    }
  }

  private boolean startAndEndInSameBlock(JavaRegion region) {
    Set<MethodBlock> endBlocks = region.getEndMethodBlocks();

    if (endBlocks.size() != 1) {
      return false;
    }

    MethodBlock startBlock = region.getStartMethodBlock();

    return region.getEndMethodBlocks().iterator().next().equals(startBlock);
  }
}

package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.asm.CFGBuilder;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.DynamicBaseRegionInstrumenter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class DynamicConfigCrusherTimerTransformer extends DynamicConfigCrusherRegionTransformer {

  public DynamicConfigCrusherTimerTransformer(String programName, String entryPoint,
      String directory, Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints)
      throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
    super(programName, entryPoint, directory, regionsToInfluencingTaints);
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    List<JavaRegion> regionsInMethod = this.getRegionsInMethod(methodNode, classNode);

    this.instrumentRegion(methodNode, classNode, regionsInMethod);
  }

  private void instrumentRegion(MethodNode methodNode, ClassNode classNode,
      List<JavaRegion> regionsInMethod) {
    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();

    int decisionCount = 0;
    methodNode.visitMaxs(200, 200);

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();

      if (!this.isCFD(insnNode.getOpcode())) {
        continue;
      }

      decisionCount++;
      JavaRegion startRegionToInstrument = getStartRegionToInstrument(decisionCount,
          regionsInMethod);

      if (startRegionToInstrument == null) {
        continue;
      }

      this.instrumentRegionStart(insnNode, startRegionToInstrument, methodNode);
      this.instrumentRegionEnd(insnNode, startRegionToInstrument, methodNode, classNode);
    }
  }

  private void instrumentRegionStart(AbstractInsnNode insnNode, JavaRegion startRegionToInstrument,
      MethodNode methodNode) {
    InsnList loggingInsnList = this.getInstructionsStartRegion(startRegionToInstrument);
    methodNode.instructions.insertBefore(insnNode, loggingInsnList);
  }

  private void instrumentRegionEnd(AbstractInsnNode insnNode, JavaRegion startRegion,
      MethodNode methodNode, ClassNode classNode) {
    InsnList insnList = methodNode.instructions;
    // TODO do not create a new graph, but rather reuse the existing graph
    MethodGraph cfg = CFGBuilder.getCfg(methodNode, classNode);

    MethodBlock methodBlockWithJumpInsn = this.getMethodBlockWithJumpInsn(insnNode, cfg);
    Set<MethodBlock> succs = methodBlockWithJumpInsn.getSuccessors();

    if (succs.size() < 2) {
      throw new UnsupportedOperationException(
          "In " + methodNode.name
              + ", the method block with the jump instruction does not have at least 2 successors. "
              + "Possibly, the control-flow decision has an empty body");
    }

    MethodBlock exitBlock = cfg.getExitBlock();

    if (succs.contains(exitBlock)) {
      throw new UnsupportedOperationException("How can the successor be the exit node?");
    }

    MethodBlock ipd = cfg.getImmediatePostDominator(methodBlockWithJumpInsn);

    if (ipd.equals(exitBlock)) {
      this.instrumentIPDExitNode(cfg, startRegion, insnList);
    }
    else {
      this.instrumentNormalIPD(methodBlockWithJumpInsn, ipd, cfg, startRegion, insnList);
    }
  }

  @Nullable
  private JavaRegion getStartRegionToInstrument(int decisionCount,
      List<JavaRegion> regionsInMethod) {
    // TODO probably optimize with a map from index to region?
    for (JavaRegion region : regionsInMethod) {
      if (region.getStartRegionIndex() == decisionCount) {
        return region;
      }
    }

    return null;
  }

  private boolean isCFD(int opcode) {
    // TODO add table switch, lookup switch
    return (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE) || opcode == Opcodes.IFNULL
        || opcode == Opcodes.IFNONNULL;
  }

  private void instrumentIPDExitNode(MethodGraph cfg, JavaRegion javaRegion, InsnList insnList) {
    Set<MethodBlock> exitPreds = cfg.getExitBlock().getPredecessors();

    for (MethodBlock pred : exitPreds) {
      List<AbstractInsnNode> instructions = pred.getInstructions();
      AbstractInsnNode returnInsnNode = this.getReturnInsnNode(instructions);
      InsnList loggingInsnList = this.getInstructionsEndRegion(javaRegion);
      insnList.insertBefore(returnInsnNode, loggingInsnList);
    }
  }

  private AbstractInsnNode getReturnInsnNode(List<AbstractInsnNode> instructions) {
    for (int i = (instructions.size() - 1); i >= 0; i--) {
      AbstractInsnNode insnNode = instructions.get(i);
      int opcode = insnNode.getOpcode();

      if (opcode == Opcodes.RET || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
        return insnNode;
      }
    }

    throw new RuntimeException(
        "The predecessor of the exit node did not have a return instruction");
  }

  private void instrumentNormalIPD(MethodBlock methodBlockWithJumpInsn, MethodBlock ipd,
      MethodGraph cfg, JavaRegion javaRegion, InsnList insnList) {
    AbstractInsnNode ipdLabelInsn = ipd.getInstructions().get(0);
    Set<MethodBlock> reachables = cfg.getReachableBlocks(methodBlockWithJumpInsn, ipd);
    reachables.remove(ipd);

    LabelNode newIPDLabelNode = this.getLabelNode();
    InsnList newIPDLoggingInsnList = this.getNewIPDLoggingInsnList(newIPDLabelNode, javaRegion);
    insnList.insertBefore(ipd.getInstructions().get(0), newIPDLoggingInsnList);

    for (MethodBlock reachable : reachables) {
      List<AbstractInsnNode> reachableInstructions = reachable.getInstructions();
      AbstractInsnNode reachableLastInstruction = reachableInstructions
          .get(reachableInstructions.size() - 1);

      if (!(reachableLastInstruction instanceof JumpInsnNode)) {
        continue;
      }

      if (!((JumpInsnNode) reachableLastInstruction).label.equals(ipdLabelInsn)) {
        continue;
      }

      JumpInsnNode newJumpInstruction = new JumpInsnNode(reachableLastInstruction.getOpcode(),
          newIPDLabelNode);
      insnList.insertBefore(reachableLastInstruction, newJumpInstruction);
      insnList.remove(reachableLastInstruction);
    }
  }

  private LabelNode getLabelNode() {
    Label label = new Label();
    return new LabelNode(label);
  }

  private InsnList getNewIPDLoggingInsnList(LabelNode labelNode, JavaRegion javaRegion) {
    InsnList loggingInsnList = new InsnList();
    loggingInsnList.add(labelNode);
    loggingInsnList.add(this.getInstructionsEndRegion(javaRegion));

    return loggingInsnList;
  }

  private MethodBlock getMethodBlockWithJumpInsn(AbstractInsnNode insnNode, MethodGraph cfg) {
    MethodBlock entryBlock = cfg.getEntryBlock();
    MethodBlock exitBlock = cfg.getExitBlock();
    Set<MethodBlock> methodBlocks = cfg.getBlocks();

    for (MethodBlock methodBlock : methodBlocks) {
      if (methodBlock.equals(entryBlock) || methodBlock.equals(exitBlock)) {
        continue;
      }

      List<AbstractInsnNode> instructions = methodBlock.getInstructions();
      AbstractInsnNode instruction = instructions.get(instructions.size() - 1);

      if (insnNode.equals(instruction)) {
        return methodBlock;
      }
    }

    throw new RuntimeException(
        "Could not fine the jump instruction as the last instruction of a method block. Possibly, "
            + "the instructions in not in the last position of the method block, which might be "
            + "that the control-flow decision does not have a body.");
  }

  @Override
  protected String getDebugDir() {
    return DynamicBaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName();
  }

}

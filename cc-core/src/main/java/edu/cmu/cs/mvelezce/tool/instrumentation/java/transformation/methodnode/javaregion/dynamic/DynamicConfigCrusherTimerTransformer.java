package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.DynamicBaseRegionInstrumenter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class DynamicConfigCrusherTimerTransformer extends DynamicConfigCrusherRegionTransformer {

  public DynamicConfigCrusherTimerTransformer(String programName, String entryPoint,
      String rootPackage, String directory,
      Map<JavaRegion, Set<Set<String>>> regionsToInfluencingTaints)
      throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
    super(programName, entryPoint, rootPackage, directory, regionsToInfluencingTaints);
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    List<JavaRegion> regionsInMethod = this.getRegionsInMethodNode(methodNode, classNode);

    this.instrumentRegion(methodNode, classNode, regionsInMethod);
  }

  private void instrumentRegion(MethodNode methodNode, ClassNode classNode,
      List<JavaRegion> regionsInMethod) {
    Set<MethodBlock> blocks = this.getMethodGraph(methodNode, classNode).getBlocks();
    InsnList insnList = methodNode.instructions;

    for (MethodBlock block : blocks) {
      if (this.getEndRegionBlocksWithReturn().contains(block)) {
        this.instrumentIPDExitNode(block, regionsInMethod, insnList);
      }
      else {
        this.instrumentEnd(methodNode, block, regionsInMethod);
        this.instrumentStart(methodNode, block, regionsInMethod);
      }
    }

    methodNode.visitMaxs(200, 200);
  }

  private void instrumentEnd(MethodNode methodNode, MethodBlock methodBlock,
      List<JavaRegion> regionsInMethod) {
    for (JavaRegion region : regionsInMethod) {
      if (!region.getEndMethodBlocks().contains(methodBlock)) {
        continue;
      }

      this.instrumentRegionEnd(methodBlock.getInstructions().get(1), region, methodNode);
    }
  }

  private void instrumentStart(MethodNode methodNode, MethodBlock methodBlock,
      List<JavaRegion> regionsInMethod) {
    for (JavaRegion region : regionsInMethod) {
      if (region.getStartMethodBlock() == null) {
        System.out.println();
      }

      if (!region.getStartMethodBlock().equals(methodBlock)) {
        continue;
      }

      this.instrumentRegionStart(methodBlock.getInstructions().get(1), region, methodNode);
    }
  }

  private void instrumentRegionStart(AbstractInsnNode insnNode, JavaRegion startRegionToInstrument,
      MethodNode methodNode) {
    InsnList loggingInsnList = this.getInstructionsStartRegion(startRegionToInstrument);
    methodNode.instructions.insertBefore(insnNode, loggingInsnList);
  }

  private void instrumentRegionEnd(AbstractInsnNode insnNode, JavaRegion region,
      MethodNode methodNode) {
    InsnList loggingInsnList = this.getInstructionsEndRegion(region);
    methodNode.instructions.insertBefore(insnNode, loggingInsnList);
  }

  private void instrumentIPDExitNode(MethodBlock exitBlock, List<JavaRegion> regionsInMethod,
      InsnList insnList) {
    for (JavaRegion region : regionsInMethod) {
      if (!region.getEndMethodBlocks().contains(exitBlock)) {
        continue;
      }

      List<AbstractInsnNode> instructions = exitBlock.getInstructions();
      AbstractInsnNode returnInsnNode = this.getReturnInsnNode(instructions);
      InsnList loggingInsnList = this.getInstructionsEndRegion(region);
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

  @Override
  protected String getDebugDir() {
    return DynamicBaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName();
  }

}

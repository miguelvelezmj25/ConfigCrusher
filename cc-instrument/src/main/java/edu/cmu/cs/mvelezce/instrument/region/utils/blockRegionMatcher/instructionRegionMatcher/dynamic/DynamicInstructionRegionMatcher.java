package edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.dynamic;

import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.InstructionRegionMatcher;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class DynamicInstructionRegionMatcher implements InstructionRegionMatcher {

  static {
    System.err.println("Add table switch, lookup switch");
  }

  public Map<AbstractInsnNode, JavaRegion> matchInstructionToRegion(
      MethodNode methodNode, List<JavaRegion> regionsInMethod) {
    Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

    if (regionsInMethod.isEmpty()) {
      return instructionsToRegion;
    }

    InsnList insnList = methodNode.instructions;
    ListIterator<AbstractInsnNode> insnListIter = insnList.iterator();
    int decisionCount = 0;

    while (insnListIter.hasNext()) {
      AbstractInsnNode insnNode = insnListIter.next();

      if (!this.isCFD(insnNode.getOpcode())) {
        continue;
      }

      decisionCount++;

      for (JavaRegion region : regionsInMethod) {
        if (region.getStartIndex() == decisionCount) {
          instructionsToRegion.put(insnNode, region);
          break;
        }
      }
    }

    if (!instructionsToRegion.values().containsAll(regionsInMethod)) {
      throw new RuntimeException("Not all regions were matched to an instruction");
    }

    return instructionsToRegion;
  }

  private boolean isCFD(int opcode) {
    switch (opcode) {
      case Opcodes.IFEQ:
      case Opcodes.IFNE:
      case Opcodes.IFLT:
      case Opcodes.IFGE:
      case Opcodes.IFGT:
      case Opcodes.IFLE:
      case Opcodes.IFNULL:
      case Opcodes.IFNONNULL:
      case Opcodes.IF_ICMPEQ:
      case Opcodes.IF_ICMPNE:
      case Opcodes.IF_ICMPLT:
      case Opcodes.IF_ICMPGE:
      case Opcodes.IF_ICMPGT:
      case Opcodes.IF_ICMPLE:
      case Opcodes.IF_ACMPNE:
      case Opcodes.IF_ACMPEQ:
      case Opcodes.TABLESWITCH:
      case Opcodes.LOOKUPSWITCH:
        return true;
    }

    return false;
  }
}

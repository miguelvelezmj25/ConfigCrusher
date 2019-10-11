package edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.dynamic;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.InstructionRegionMatcher;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

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
    return (opcode >= Opcodes.IFEQ && opcode <= Opcodes.IF_ACMPNE)
        || opcode == Opcodes.IFNULL
        || opcode == Opcodes.IFNONNULL;
  }
}

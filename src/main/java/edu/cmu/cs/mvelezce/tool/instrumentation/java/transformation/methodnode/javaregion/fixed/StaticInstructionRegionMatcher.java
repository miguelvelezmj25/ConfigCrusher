package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.fixed;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion.InstructionRegionMatcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class StaticInstructionRegionMatcher implements InstructionRegionMatcher {

  public Map<AbstractInsnNode, JavaRegion> matchInstructionToRegion(MethodNode methodNode,
      List<JavaRegion> regionsInMethod) {
    InsnList instructions = methodNode.instructions;
    Map<AbstractInsnNode, JavaRegion> instructionsToRegion = new HashMap<>();

    for (JavaRegion region : regionsInMethod) {
      instructionsToRegion.put(instructions.get(region.getStartRegionIndex()), region);
    }

    return instructionsToRegion;
  }

}

package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public interface InstructionRegionMatcher {

  Map<AbstractInsnNode, JavaRegion> matchInstructionToRegion(MethodNode methodNode,
      List<JavaRegion> regionsInMethod);

}

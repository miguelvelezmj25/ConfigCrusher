package edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Map;

public interface InstructionRegionMatcher {

  Map<AbstractInsnNode, JavaRegion> matchInstructionToRegion(
      MethodNode methodNode, List<JavaRegion> regionsInMethod);
}

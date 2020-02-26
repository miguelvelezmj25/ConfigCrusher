package edu.cmu.cs.mvelezce.instrument.region.transformer;

import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.instructionRegionMatcher.InstructionRegionMatcher;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class RegionTransformer<T> extends BaseMethodTransformer {

  private final Map<JavaRegion, T> regionsToData;
  private final BlockRegionMatcher blockRegionMatcher;

  public RegionTransformer(
      String programName,
      ClassTransformer classTransformer,
      String mainClass,
      boolean debug,
      Map<JavaRegion, T> regionsToData,
      InstructionRegionMatcher instructionRegionMatcher) {
    super(programName, classTransformer, mainClass, debug);

    this.regionsToData = regionsToData;
    this.blockRegionMatcher =
        new BlockRegionMatcher(instructionRegionMatcher, regionsToData.keySet());
  }

  protected Map<JavaRegion, T> getRegionsToData() {
    return regionsToData;
  }

  protected BlockRegionMatcher getBlockRegionMatcher() {
    return blockRegionMatcher;
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    Set<MethodNode> methodsToInstrument = new HashSet<>();
    Set<JavaRegion> regions = this.regionsToData.keySet();

    if (InstrumenterUtils.getRegionsInClass(classNode, regions).isEmpty()) {
      return methodsToInstrument;
    }

    for (MethodNode methodNode : classNode.methods) {
      if (!InstrumenterUtils.getRegionsInMethod(methodNode, classNode, regions).isEmpty()) {
        methodsToInstrument.add(methodNode);
      }
    }

    return methodsToInstrument;
  }

  @Override
  public void transformMethods(Set<ClassNode> classNodes) throws IOException {
    for (ClassNode classNode : classNodes) {
      for (MethodNode methodNode : classNode.methods) {
        this.blockRegionMatcher.matchBlocksToRegions(methodNode, classNode);
        this.blockRegionMatcher.matchBlocksToMethodNodes(methodNode, classNode);
      }
    }

    this.transformRegions(classNodes);
    super.transformMethods(classNodes);
  }

  protected abstract void transformRegions(Set<ClassNode> classNodes);
}

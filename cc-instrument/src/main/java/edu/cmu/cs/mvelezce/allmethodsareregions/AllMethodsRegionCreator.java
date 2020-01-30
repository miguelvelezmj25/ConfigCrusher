package edu.cmu.cs.mvelezce.allmethodsareregions;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.instrumenter.transform.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.instrumenter.transform.methodnode.BaseMethodTransformer;
import edu.cmu.cs.mvelezce.utils.asm.ASMUtils;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.*;

public class AllMethodsRegionCreator extends BaseMethodTransformer {

  public AllMethodsRegionCreator(
      String programName, ClassTransformer classTransformer, String mainClass, boolean debug) {
    super(programName, classTransformer, mainClass, debug);
  }

  Map<JavaRegion, Partitioning> createRegions(Partitioning partitioning) throws IOException {
    Map<JavaRegion, Partitioning> regionsToPartitions = new HashMap<>();
    Set<ClassNode> classes = this.getClassTransformer().readClasses();

    for (ClassNode classNode : classes) {
      List<MethodNode> methods = classNode.methods;

      for (MethodNode methodNode : methods) {
        JavaRegion region =
            new JavaRegion.Builder(
                    UUID.randomUUID(),
                    ASMUtils.getClassPackage(classNode),
                    ASMUtils.getClassName(classNode),
                    ASMUtils.getMethodSignature(methodNode))
                .startMethodBlock(new MethodBlock.Builder(UUID.randomUUID().toString()).build())
                .endMethodBlocks(new HashSet<>())
                .build();

        regionsToPartitions.put(region, partitioning);
      }
    }

    return regionsToPartitions;
  }

  @Override
  protected String getDebugDir() {
    throw new UnsupportedOperationException(
        "Method should not be called since we only want to make each method in the program a region");
  }

  @Override
  public Set<MethodNode> getMethodsToInstrument(ClassNode classNode) {
    throw new UnsupportedOperationException(
        "Method should not be called since we only want to make each method in the program a region");
  }

  @Override
  public void transformMethod(MethodNode methodNode, ClassNode classNode) {
    throw new UnsupportedOperationException(
        "Method should not be called since we only want to make each method in the program a region");
  }
}

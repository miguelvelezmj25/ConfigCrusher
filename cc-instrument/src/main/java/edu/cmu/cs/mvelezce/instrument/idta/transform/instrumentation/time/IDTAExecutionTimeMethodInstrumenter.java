package edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.time;

import edu.cmu.cs.mvelezce.analysis.region.RegionsManager;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.IDTAMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;

public class IDTAExecutionTimeMethodInstrumenter implements IDTAMethodInstrumenter {

  @Override
  public void instrument(
      MethodNode methodNode,
      ClassNode classNode,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Collection<JavaRegion> regionsInMethod = blocksToRegions.values();
    regionsInMethod.removeIf(Objects::isNull);

    if (regionsInMethod.isEmpty()) {
      throw new RuntimeException(
          classNode.name + " - " + methodNode.name + " does not have any regions");
    }

    if (regionsInMethod.size() == 1) {
      this.instrumentEntireMethod(methodNode, regionsInMethod.iterator().next());
    } else {
      this.instrumentMethod(methodNode, regionsInMethod);
    }

    methodNode.visitMaxs(200, 200);
  }

  private void instrumentEntireMethod(MethodNode methodNode, JavaRegion region) {
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

  private void instrumentMethod(MethodNode methodNode, Collection<JavaRegion> regionsInMethod) {
    throw new UnsupportedOperationException("implement");
  }
}

package edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation;

import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.LinkedHashMap;

public interface IDTAMethodInstrumenter {

  Void errorMessage = errorMessage();

  static Void errorMessage() {
    System.err.println(
        "This interface is not specific to IDTA. Hence, we can probably move it up to a 'MethodInstrumenter'");

    return null;
  }

  void instrument(
      MethodNode methodNode,
      ClassNode classNode,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions);
}

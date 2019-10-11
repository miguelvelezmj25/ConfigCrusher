package edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.LinkedHashMap;

public interface IDTAMethodInstrumenter {

  void instrument(
      MethodNode methodNode,
      ClassNode classNode,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions);
}

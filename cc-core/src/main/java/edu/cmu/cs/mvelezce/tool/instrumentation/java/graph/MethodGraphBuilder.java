package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import jdk.internal.org.objectweb.asm.tree.MethodNode;

public interface MethodGraphBuilder {

  MethodGraph build(MethodNode methodNode);

  void addEdges(MethodGraph graph, MethodNode methodNode);

  void addBlocks(MethodGraph graph, MethodNode methodNode);

  void addInstructions(MethodGraph graph, MethodNode methodNode);

  void connectEntryNode(MethodGraph graph, MethodNode methodNode);

  void connectExitNode(MethodGraph graph, MethodNode methodNode);

}

package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

public interface MethodGraphBuilder {

  MethodGraph build();

  void addEdges();

  void getBlocks();

  void addInstructions();

  void connectEntryNode();

  void connectExitNode();

}

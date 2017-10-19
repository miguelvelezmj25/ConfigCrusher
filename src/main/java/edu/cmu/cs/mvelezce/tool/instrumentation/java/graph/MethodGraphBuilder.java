package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

public interface MethodGraphBuilder {

    public MethodGraph build();

    public void addEdges();

    public void getBlocks();

    public void addInstructions();

    public void connectEntryNode();

    public void connectExitNode();

}

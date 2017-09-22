package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

public interface MethodGraphBuilder {

    public MethodGraph build();

    public void addEdges(MethodGraph graph);

    public void getBlocks(MethodGraph graph);

    public void addInstructions(MethodGraph graph);

    public void connectEntryNode(MethodGraph graph);

    public void connectExitNode(MethodGraph graph);

}

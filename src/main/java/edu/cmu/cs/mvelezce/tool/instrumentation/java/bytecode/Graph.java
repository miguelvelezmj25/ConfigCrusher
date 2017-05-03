package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 5/3/17.
 */
public class Graph {

    private DirectedGraph<Block, DefaultEdge> graph;

    public Graph() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    public void addVertex(Block block) {
        this.graph.addVertex(block);
    }

    public void addEdge(Block from, Block to) {
        // TODO check that the from does not already have two outgoing edges
        Set<Block> successors = this.getSuccessors(from);

        if(successors.size() >= 2) {
            throw new IllegalArgumentException("A block cannot have more than two successors");
        }

        this.graph.addEdge(from, to);
    }

    public Set<Block> getSuccessors(Block block) {
        Set<DefaultEdge> outgoingEdges = this.graph.outgoingEdgesOf(block);
        Set<Block> successors = new HashSet<>();

        for(DefaultEdge edge : outgoingEdges) {
            successors.add(this.graph.getEdgeTarget(edge));
        }

        return successors;
    }

    public Set<Block> getPredecessors(Block block) {
        Set<DefaultEdge> outgoingEdges = this.graph.incomingEdgesOf(block);
        Set<Block> predecessors = new HashSet<>();

        for(DefaultEdge edge : outgoingEdges) {
            predecessors.add(this.graph.getEdgeSource(edge));
        }

        return predecessors;
    }

    public Set<Block> getAllVertices() {
        return this.graph.vertexSet();
    }

    @Override
    public String toString() {
        return "Graph{" +
                "graph=" + graph +
                '}';
    }
}

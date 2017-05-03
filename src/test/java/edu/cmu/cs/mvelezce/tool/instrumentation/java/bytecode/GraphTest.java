package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 5/3/17.
 */
public class GraphTest {

    @Test
    public void testAddVertex() {
        // Build graph
        Graph graph = new Graph();

        // Build block
        Block block = new Block(new Label(), new InsnList());

        // Add vertex
        graph.addVertex(block);

        // Assert
        Assert.assertEquals(1, graph.getAllVertices().size());
    }

    @Test
    public void testAddEdge1() {
        // Build graph
        Graph graph = new Graph();

        // Build block
        Block source = new Block(new Label(), new InsnList());
        Block target = new Block(new Label(), new InsnList());

        // Add vertices
        graph.addVertex(source);
        graph.addVertex(target);

        // Add edge
        graph.addEdge(source, target);

        // Assert
        Assert.assertEquals(target, graph.getSuccessors(source).iterator().next());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddEdge2() {
        // Build graph
        Graph graph = new Graph();

        // Build block
        Block source = new Block(new Label(), new InsnList());
        Block target1 = new Block(new Label(), new InsnList());
        Block target2 = new Block(new Label(), new InsnList());
        Block target3 = new Block(new Label(), new InsnList());

        // Add vertices
        graph.addVertex(source);
        graph.addVertex(target1);
        graph.addVertex(target2);

        // Add edges
        graph.addEdge(source, target1);
        graph.addEdge(source, target2);

        // Assert exception thrown
        graph.addEdge(source, target3);
    }

    @Test
    public void testGetSuccessors() {
        // Build graph
        Graph graph = new Graph();

        // Build block
        Block one = new Block(new Label(), new InsnList());
        Block two = new Block(new Label(), new InsnList());
        Block three = new Block(new Label(), new InsnList());

        // Add vertices
        graph.addVertex(one);
        graph.addVertex(two);
        graph.addVertex(three);

        // Add edge
        graph.addEdge(one, two);
        graph.addEdge(one, three);

        // Expected
        Set<Block> successorBlocks = new HashSet<>();
        successorBlocks.add(two);
        successorBlocks.add(three);

        // Assert
        Assert.assertEquals(successorBlocks, graph.getSuccessors(one));
    }

    @Test
    public void testGetPredecessors() {
        // Build graph
        Graph graph = new Graph();

        // Build block
        Block one = new Block(new Label(), new InsnList());
        Block two = new Block(new Label(), new InsnList());
        Block three = new Block(new Label(), new InsnList());

        // Add vertices
        graph.addVertex(one);
        graph.addVertex(two);
        graph.addVertex(three);

        // Add edge
        graph.addEdge(two, one);
        graph.addEdge(three, one);

        // Expected
        Set<Block> predecessorBlocks = new HashSet<>();
        predecessorBlocks.add(two);
        predecessorBlocks.add(three);

        // Assert
        Assert.assertEquals(predecessorBlocks, graph.getPredecessors(one));
    }

}
package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphTest {

    @Test
    public void testAddMethodBlock() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build methodBlock
        MethodBlock methodBlock = new MethodBlock(new Label(), new InsnList());

        // Add block
        methodGraph.addMethodBlock(methodBlock);

        // Assert
        Assert.assertEquals(1, methodGraph.getBlockCount());
    }

    @Test
    public void testAddEdge1() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock source = new MethodBlock(new Label(), new InsnList());
        MethodBlock target = new MethodBlock(new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(source);
        methodGraph.addMethodBlock(target);

        // Add edge
        methodGraph.addEdge(source, target);

        // Assert
        Assert.assertEquals(target, source.getSuccessors().iterator().next());
        Assert.assertEquals(source, target.getPredecessors().iterator().next());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddEdge2() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock source = new MethodBlock(new Label(), new InsnList());
        MethodBlock target1 = new MethodBlock(new Label(), new InsnList());
        MethodBlock target2 = new MethodBlock(new Label(), new InsnList());
        MethodBlock target3 = new MethodBlock(new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(source);
        methodGraph.addMethodBlock(target1);
        methodGraph.addMethodBlock(target2);

        // Add edges
        methodGraph.addEdge(source, target1);
        methodGraph.addEdge(source, target2);

        // Assert exception thrown
        methodGraph.addEdge(source, target3);
    }

}
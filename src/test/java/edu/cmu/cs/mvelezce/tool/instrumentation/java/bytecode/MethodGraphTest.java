package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphTest {

    @Test
    public void getNextCommonSuccessor1() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label(), new InsnList());
        MethodBlock b = new MethodBlock("B", new Label(), new InsnList());
        MethodBlock end = new MethodBlock("D", new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(end);

        // Add edges
        methodGraph.addEdge(a, end);
        methodGraph.addEdge(b, end);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(end, methodGraph.getNextCommonSuccessor(a, b));
    }

    @Test
    public void getNextCommonSuccessor2() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label(), new InsnList());
        MethodBlock b = new MethodBlock("B", new Label(), new InsnList());
        MethodBlock c = new MethodBlock("C", new Label(), new InsnList());
        MethodBlock d = new MethodBlock("D", new Label(), new InsnList());
        MethodBlock e = new MethodBlock("E", new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(e, methodGraph.getNextCommonSuccessor(a, c));
    }

    @Test
    public void getNextCommonSuccessor3() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label(), new InsnList());
        MethodBlock b = new MethodBlock("B", new Label(), new InsnList());
        MethodBlock c = new MethodBlock("C", new Label(), new InsnList());
        MethodBlock d = new MethodBlock("D", new Label(), new InsnList());
        MethodBlock e = new MethodBlock("E", new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(c, a);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(a, methodGraph.getNextCommonSuccessor(a, c));
    }

    @Test
    public void getNextCommonSuccessor4() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label(), new InsnList());
        MethodBlock b = new MethodBlock("B", new Label(), new InsnList());
        MethodBlock c = new MethodBlock("C", new Label(), new InsnList());
        MethodBlock d = new MethodBlock("D", new Label(), new InsnList());
        MethodBlock e = new MethodBlock("E", new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(c, b);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(b, methodGraph.getNextCommonSuccessor(a, c));
    }

    @Test
    public void getNextCommonSuccessor5() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label(), new InsnList());
        MethodBlock b = new MethodBlock("B", new Label(), new InsnList());
        MethodBlock c = new MethodBlock("C", new Label(), new InsnList());
        MethodBlock d = new MethodBlock("D", new Label(), new InsnList());
        MethodBlock e = new MethodBlock("E", new Label(), new InsnList());
        MethodBlock f = new MethodBlock("F", new Label(), new InsnList());
        MethodBlock g = new MethodBlock("G", new Label(), new InsnList());
        MethodBlock h = new MethodBlock("H", new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);
        methodGraph.addMethodBlock(h);

        // Add edges
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(g, e);
        methodGraph.addEdge(h, d);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(d, methodGraph.getNextCommonSuccessor(a, b));
    }

    @Test
    public void getNextCommonSuccessor6() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label(), new InsnList());
        MethodBlock b = new MethodBlock("B", new Label(), new InsnList());
        MethodBlock c = new MethodBlock("C", new Label(), new InsnList());
        MethodBlock d = new MethodBlock("D", new Label(), new InsnList());
        MethodBlock e = new MethodBlock("E", new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(c, e);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(c, methodGraph.getNextCommonSuccessor(b, c));
    }

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
        MethodBlock end = new MethodBlock(new Label(), new InsnList());

        // Add vertices
        methodGraph.addMethodBlock(source);
        methodGraph.addMethodBlock(end);

        // Add edge
        methodGraph.addEdge(source, end);

        // Assert
        Assert.assertEquals(end, source.getSuccessors().iterator().next());
        Assert.assertEquals(source, end.getPredecessors().iterator().next());
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
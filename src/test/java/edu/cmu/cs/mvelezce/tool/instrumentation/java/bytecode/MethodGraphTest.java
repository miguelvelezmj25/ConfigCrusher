package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphTest {

    @Test
    public void testGetWhereBranchesConverge1() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(d, methodGraph.getWhereBranchesConverge(a));
    }

    @Test
    public void testGetWhereBranchesConverge2() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());
        MethodBlock g = new MethodBlock("G", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(f, a);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, g);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(e, methodGraph.getWhereBranchesConverge(f));
    }

    @Test
    public void testGetWhereBranchesConverge3() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());
        MethodBlock g = new MethodBlock("G", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(f, a);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(e, g);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(e, methodGraph.getWhereBranchesConverge(f));
    }

    @Test
    public void testGetWhereBranchesConverge4() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(f, a);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(c, b);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(b, methodGraph.getWhereBranchesConverge(f));
    }

    @Test
    public void testGetWhereBranchesConverge5() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());
        MethodBlock g = new MethodBlock("G", new Label());
        MethodBlock h = new MethodBlock("H", new Label());
        MethodBlock i = new MethodBlock("I", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);
        methodGraph.addMethodBlock(h);
        methodGraph.addMethodBlock(i);

        // Add edges
        methodGraph.addEdge(i, a);
        methodGraph.addEdge(i, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(g, e);
        methodGraph.addEdge(h, d);
        methodGraph.addEdge(a, h);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(d, methodGraph.getWhereBranchesConverge(i));
    }

    @Test
    public void testGetWhereBranchesConverge6() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());

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

        // TODO this might be the case where we need to
        // Assert
        Assert.assertEquals(c, methodGraph.getWhereBranchesConverge(a));
    }

    @Test
    public void testGetWhereBranchesConverge7() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(d, f);
        methodGraph.addEdge(c, f);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, f);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(f, methodGraph.getWhereBranchesConverge(a));
    }

    @Test
    public void testGetWhereBranchesConverge8() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(d, f);
        methodGraph.addEdge(c, f);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(c, e);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(f, methodGraph.getWhereBranchesConverge(a));
    }

    @Test
    public void testGetWhereBranchesConverge9() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, b);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(e, methodGraph.getWhereBranchesConverge(b));
    }

    @Test
    public void testGetWhereBranchesConverge10() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(c, f);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(f, methodGraph.getWhereBranchesConverge(a));
    }

    @Test
    public void testGetWhereBranchesConverge11() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());
        MethodBlock f = new MethodBlock("F", new Label());
        MethodBlock g = new MethodBlock("G", new Label());
        MethodBlock h = new MethodBlock("H", new Label());

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
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(e, h);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, h);

        System.out.println(methodGraph.toDotString());

        // Assert
        Assert.assertEquals(c, methodGraph.getWhereBranchesConverge(a));
    }

    @Test
    public void testAddMethodBlock() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build methodBlock
        MethodBlock methodBlock = new MethodBlock(new Label());

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
        MethodBlock source = new MethodBlock(new Label());
        MethodBlock end = new MethodBlock(new Label());

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
        MethodBlock source = new MethodBlock(new Label());
        MethodBlock target1 = new MethodBlock(new Label());
        MethodBlock target2 = new MethodBlock(new Label());
        MethodBlock target3 = new MethodBlock(new Label());

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
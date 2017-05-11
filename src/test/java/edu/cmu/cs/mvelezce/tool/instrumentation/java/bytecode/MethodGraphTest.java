package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.Label;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphTest {

    @Test
    public void testGetWhereToEndInstrumenting1() {
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
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, f);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(f, c);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(c, MethodGraph.getWhereToEndInstrumenting(methodGraph, a));
    }

    @Test
    public void testGetStronglyConnectedComponents1() {
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
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(d, e);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(3, MethodGraph.getStronglyConnectedComponents(methodGraph, a).size());
    }

    @Test
    public void testGetStronglyConnectedComponents2() {
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

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(5, MethodGraph.getStronglyConnectedComponents(methodGraph, a).size());
    }

    @Test
    public void testGetStronglyConnectedComponents3() {
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
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(f, a);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(c, b);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(5, MethodGraph.getStronglyConnectedComponents(methodGraph, f).size());
    }

    @Test
    public void testGetStronglyConnectedComponents4() {
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
        methodGraph.addEdge(a,b);
        methodGraph.addEdge(b,e);
        methodGraph.addEdge(e,f);
        methodGraph.addEdge(b,c);
        methodGraph.addEdge(c,d);
        methodGraph.addEdge(d,b);

        Assert.assertEquals(4, MethodGraph.getStronglyConnectedComponents(methodGraph, a).size());
    }

    @Test
    public void testGetStronglyConnectedComponents5() {
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
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, h);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, d);

        Assert.assertEquals(5, MethodGraph.getStronglyConnectedComponents(methodGraph, a).size());
    }

    @Test
    public void testReverseGraph1() {
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

        System.out.println(methodGraph.toDotString("test"));
        MethodGraph reversedGraph = methodGraph.reverseGraph();
        MethodGraph normalGraph = MethodGraph.reverseGraph(reversedGraph);
        System.out.println(normalGraph.toDotString("normal"));

        Assert.assertEquals(methodGraph.toDotString("a"), normalGraph.toDotString("a"));
    }

    @Test
    public void testReverseGraph2() {
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

        System.out.println(methodGraph.toDotString("test"));
        MethodGraph reversedGraph = methodGraph.reverseGraph();
        MethodGraph normalGraph = MethodGraph.reverseGraph(reversedGraph);
        System.out.println(normalGraph.toDotString("normal"));

        Assert.assertEquals(methodGraph.toDotString("a"), normalGraph.toDotString("a"));
    }

    @Test
    public void testGetDominators1() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Expected
        Map<MethodBlock, Set<MethodBlock>> expected = new HashMap<>();
        Set<MethodBlock> dominators = new HashSet<>();
        dominators.add(a);
        expected.put(a, dominators);

        dominators = new HashSet<>();
        dominators.add(a);
        dominators.add(b);
        expected.put(b, dominators);

        dominators = new HashSet<>();
        dominators.add(a);
        dominators.add(c);
        expected.put(c, dominators);

        dominators = new HashSet<>();
        dominators.add(a);
        dominators.add(d);
        expected.put(d, dominators);

        dominators = new HashSet<>();
        dominators.add(a);
        dominators.add(d);
        dominators.add(e);
        expected.put(e, dominators);

        Map<MethodBlock, Set<MethodBlock>> result = methodGraph.getDominators();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetDominators2() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());

        // Add vertices
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(a);

        // Add edges
        methodGraph.addEdge(e, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(b, a);

        System.out.println(methodGraph.toDotString("test"));

        // Expected
        Map<MethodBlock, Set<MethodBlock>> expected = new HashMap<>();
        Set<MethodBlock> dominators = new HashSet<>();
        dominators.add(e);
        expected.put(e, dominators);

        dominators = new HashSet<>();
        dominators.add(e);
        dominators.add(d);
        expected.put(d, dominators);

        dominators = new HashSet<>();
        dominators.add(e);
        dominators.add(d);
        dominators.add(c);
        expected.put(c, dominators);

        dominators = new HashSet<>();
        dominators.add(e);
        dominators.add(d);
        dominators.add(b);
        expected.put(b, dominators);

        dominators = new HashSet<>();
        dominators.add(e);
        dominators.add(d);
        dominators.add(a);
        expected.put(a, dominators);

        Map<MethodBlock, Set<MethodBlock>> result = methodGraph.getDominators();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetDominators3() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());

        // Add vertices
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);

        // Add edges
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);

        System.out.println(methodGraph.toDotString("test"));

        // Expected
        Map<MethodBlock, Set<MethodBlock>> expected = new HashMap<>();
        Set<MethodBlock> dominators = new HashSet<>();
        dominators.add(b);
        expected.put(b, dominators);

        dominators = new HashSet<>();
        dominators.add(c);
        dominators.add(b);
        expected.put(c, dominators);

        dominators = new HashSet<>();
        dominators.add(b);
        dominators.add(c);
        dominators.add(d);
        expected.put(d, dominators);

        Map<MethodBlock, Set<MethodBlock>> result = methodGraph.getDominators();

        for(Map.Entry<MethodBlock, Set<MethodBlock>> blockToDominators : result.entrySet()) {
            System.out.println(blockToDominators.getKey() + " - " + blockToDominators.getValue());
            Assert.assertEquals(expected.get(blockToDominators.getKey()), blockToDominators.getValue());
        }

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetImmediateDominator1() {
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

        System.out.println(methodGraph.toDotString("test"));
        MethodBlock result = methodGraph.getImmediateDominator(d);

        Assert.assertEquals(a, result);
    }

    @Test
    public void testGetImmediateDominator2() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A", new Label());
        MethodBlock b = new MethodBlock("B", new Label());
        MethodBlock c = new MethodBlock("C", new Label());
        MethodBlock d = new MethodBlock("D", new Label());
        MethodBlock e = new MethodBlock("E", new Label());

        // Add vertices
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(a);

        // Add edges
        methodGraph.addEdge(e, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(b, a);

        System.out.println(methodGraph.toDotString("test"));
        MethodBlock result = methodGraph.getImmediateDominator(a);

        Assert.assertEquals(d, result);
    }

    @Test
    public void testGetImmediatePostDominator1() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(d.getID(), methodGraph.getImmediatePostDominator(a).getID());
    }

    @Test
    public void testGetImmediatePostDominator2() {
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
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(f, a);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, g);

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(e, methodGraph.getImmediatePostDominator(f));
    }

    @Test
    public void testGetImmediatePostDominator3() {
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
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(e, methodGraph.getImmediatePostDominator(f));
    }

    @Test
    public void testGetImmediatePostDominator4() {
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
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(f, a);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(c, b);

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(b, methodGraph.getImmediatePostDominator(f));
    }

    @Test
    public void testGetImmediatePostDominator5() {
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
        methodGraph.addMethodBlock(i);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);
        methodGraph.addMethodBlock(h);
        methodGraph.addMethodBlock(d);

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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(d, methodGraph.getImmediatePostDominator(i));
    }

    @Test
    public void testGetImmediatePostDominator6() {
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

        System.out.println(methodGraph.toDotString("test"));

        // TODO this might be the case where we need to
        // Assert
        Assert.assertEquals(c, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator7() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(f, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator8() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(f, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator9() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(e, methodGraph.getImmediatePostDominator(b));
    }

    @Test
    public void testGetImmediatePostDominator10() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(f, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator11() {
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

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(c, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator12() {
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
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(a, d);
        methodGraph.addEdge(d, a);
        methodGraph.addEdge(c, f);

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(f, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator13() {
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
        methodGraph.addEdge(a, d);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(d, methodGraph.getImmediatePostDominator(a));
    }

    @Test
    public void testGetImmediatePostDominator14() {
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
        methodGraph.addEdge(a, d);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(d, methodGraph.getImmediatePostDominator(b));
    }

    @Test
    public void testGetImmediatePostDominator15() {
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
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, h);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, d);

        System.out.println(methodGraph.toDotString("test"));

        // Assert
        Assert.assertEquals(e, methodGraph.getImmediatePostDominator(b));
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
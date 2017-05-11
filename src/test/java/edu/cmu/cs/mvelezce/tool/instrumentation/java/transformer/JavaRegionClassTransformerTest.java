package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.MethodGraph;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mvelezce on 5/11/17.
 */
public class JavaRegionClassTransformerTest {

    @Test
    public void testGetWhereToStartInstrumenting1() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock f = new MethodBlock("F");
        MethodBlock x = new MethodBlock("X");

        // Add vertices
        methodGraph.addMethodBlock(x);
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
        methodGraph.addEdge(x, a);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(a, JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
    }

    @Test
    public void testGetWhereToStartInstrumenting2() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock x = new MethodBlock("X");

        // Add vertices
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, d);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(b, e);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(a, JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
    }

    @Test
    public void testGetWhereToStartInstrumenting3() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock x = new MethodBlock("X");
        MethodBlock y = new MethodBlock("Y");

        // Add vertices
        methodGraph.addMethodBlock(y);
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(y, x);
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, d);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, x);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(b, e);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(x, JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
    }

    @Test
    public void testGetWhereToStartInstrumenting4() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock x = new MethodBlock("X");
        MethodBlock y = new MethodBlock("Y");

        // Add vertices
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(y);
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(d, x);
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(x, y);
        methodGraph.addEdge(y, c);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, e);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(x, JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(methodGraph, x));
    }

    @Test
    public void testGetWhereToStartInstrumenting5() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock x = new MethodBlock("X");

        // Add vertices
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(a, JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
    }

    @Test
    public void testGetWhereToStartInstrumenting6() {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock f = new MethodBlock("F");
        MethodBlock g = new MethodBlock("G");
        MethodBlock x = new MethodBlock("X");
        MethodBlock y = new MethodBlock("Y");

        // Add vertices
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(y);
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(f, d);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, x);
        methodGraph.addEdge(d, x);
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(x, y);
        methodGraph.addEdge(y, c);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, e);

        System.out.println(methodGraph.toDotString("test"));
        Assert.assertEquals(x, JavaRegionClassTransformer.getBlockToStartInstrumentingBeforeIt(methodGraph, x));
    }

}
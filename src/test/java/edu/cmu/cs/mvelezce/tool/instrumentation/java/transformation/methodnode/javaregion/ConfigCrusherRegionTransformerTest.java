package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodGraph;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode.MethodTransformer;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

public class ConfigCrusherRegionTransformerTest {

    private static ConfigCrusherRegionTransformer transformer;

    @Before
    public void setUp() {
        try {
            transformer = new ConfigCrusherRegionTransformer(null, null, "", null) {
                @Override
                public InsnList getInstructionsStartRegion(JavaRegion javaRegion) {
                    return null;
                }

                @Override
                public InsnList getInstructionsEndRegion(JavaRegion javaRegion) {
                    return null;
                }

                @Override
                public void transformMethod(MethodNode methodNode) {

                }
            };
        } catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException | MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetWhereToStartInstrumenting1() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(c, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToEndInstrumenting1() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(c, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToStartInstrumenting2() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToEndInstrumenting2() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToStartInstrumenting3() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToEndInstrumenting3() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToStartInstrumenting4() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(x, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToEndInstrumenting4() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToStartInstrumenting5() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
    }

    @Test
    public void testGetWhereToEndInstrumenting5() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(d, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
    }

    @Test
    public void testGetWhereToStartInstrumenting6() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(f, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, f));
        Assert.assertEquals(x, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToEndInstrumenting6() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(x, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, f));
        Assert.assertEquals(a, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
    }

    @Test
    public void testGetWhereToStartInstrumenting7() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");

        // Add vertices
        methodGraph.addMethodBlock(h);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(h, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, f);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(c, e);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(c, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToEndInstrumenting7() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");

        // Add vertices
        methodGraph.addMethodBlock(h);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(e);

        // Add edges
        methodGraph.addEdge(h, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, f);
        methodGraph.addEdge(f, c);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, b);
        methodGraph.addEdge(c, e);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(c, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToStartInstrumenting8() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");
        MethodBlock i = new MethodBlock("I");

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
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(h, f);
        methodGraph.addEdge(g, i);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(g, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, g));
    }

    @Test
    public void testGetWhereToEndInstrumenting8() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");
        MethodBlock i = new MethodBlock("I");

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
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(h, f);
        methodGraph.addEdge(g, i);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(i, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, g));
    }

    @Test
    public void testGetWhereToStartInstrumenting9() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(b, f);
        methodGraph.addEdge(f, d);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(d, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, d));
    }

    @Test
    public void testGetWhereToEndInstrumenting9() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(b, f);
        methodGraph.addEdge(f, d);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(d, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(g, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, d));
    }

    @Test
    public void testGetWhereToStartInstrumenting10() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(b, f);
        methodGraph.addEdge(f, d);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(d, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, d));
    }

    @Test
    public void testGetWhereToEndInstrumenting10() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(b, f);
        methodGraph.addEdge(f, d);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(d, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(g, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, d));
    }

    @Test
    public void testGetWhereToStartInstrumenting11() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock f = new MethodBlock("F");

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(c, f);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(c, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToEndInstrumenting11() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
        // Build methodGraph
        MethodGraph methodGraph = new MethodGraph();

        // Build block
        MethodBlock a = new MethodBlock("A");
        MethodBlock b = new MethodBlock("B");
        MethodBlock c = new MethodBlock("C");
        MethodBlock d = new MethodBlock("D");
        MethodBlock e = new MethodBlock("E");
        MethodBlock f = new MethodBlock("F");

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, c);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(c, f);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(c, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(f, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToStartInstrumenting12() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        methodGraph.addEdge(c, e);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(x, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(c, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToEndInstrumenting12() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        methodGraph.addEdge(c, e);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, c));
    }

    @Test
    public void testGetWhereToStartInstrumenting13() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock y = new MethodBlock("Y");

        // Add vertices
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(y);
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(d, x);
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(x, y);
        methodGraph.addEdge(y, c);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(e, f);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(x, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(e, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, e));
    }

    @Test
    public void testGetWhereToEndInstrumenting13() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock y = new MethodBlock("Y");

        // Add vertices
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(y);
        methodGraph.addMethodBlock(x);
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(d, x);
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(x, y);
        methodGraph.addEdge(y, c);
        methodGraph.addEdge(c, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, e);
        methodGraph.addEdge(e, c);
        methodGraph.addEdge(e, f);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, x));
        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(f, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, e));
    }

    @Test
    public void testGetWhereToStartInstrumenting14() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");
        MethodBlock i = new MethodBlock("I");
        MethodBlock j = new MethodBlock("J");
        MethodBlock k = new MethodBlock("K");
        MethodBlock l = new MethodBlock("L");

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
        methodGraph.addMethodBlock(j);
        methodGraph.addMethodBlock(k);
        methodGraph.addMethodBlock(l);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, d);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(g, k);
        methodGraph.addEdge(h, i);
        methodGraph.addEdge(i, j);
        methodGraph.addEdge(j, h);
        methodGraph.addEdge(k, j);
        methodGraph.addEdge(k, l);
        methodGraph.addEdge(j, l);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(d, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, d));
        Assert.assertEquals(g, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, g));
        Assert.assertEquals(k, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, k));
        Assert.assertEquals(j, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, j));
    }

    @Test
    public void testGetWhereToEndInstrumenting14() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");
        MethodBlock i = new MethodBlock("I");
        MethodBlock j = new MethodBlock("J");
        MethodBlock k = new MethodBlock("K");
        MethodBlock l = new MethodBlock("L");

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
        methodGraph.addMethodBlock(j);
        methodGraph.addMethodBlock(k);
        methodGraph.addMethodBlock(l);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, d);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(g, k);
        methodGraph.addEdge(h, i);
        methodGraph.addEdge(i, j);
        methodGraph.addEdge(j, h);
        methodGraph.addEdge(k, j);
        methodGraph.addEdge(k, l);
        methodGraph.addEdge(j, l);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(d, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(g, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, d));
        Assert.assertEquals(l, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, g));
        Assert.assertEquals(l, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, k));
        Assert.assertEquals(l, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, j));
    }

    @Test
    public void testGetWhereToStartInstrumenting15() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");
        MethodBlock i = new MethodBlock("I");
        MethodBlock j = new MethodBlock("J");
        MethodBlock k = new MethodBlock("K");
        MethodBlock l = new MethodBlock("L");

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
        methodGraph.addMethodBlock(j);
        methodGraph.addMethodBlock(k);
        methodGraph.addMethodBlock(l);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, d);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(g, k);
        methodGraph.addEdge(h, i);
        methodGraph.addEdge(i, j);
        methodGraph.addEdge(j, h);
        methodGraph.addEdge(k, j);
        methodGraph.addEdge(j, l);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(a, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(d, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, d));
        Assert.assertEquals(g, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, g));
        Assert.assertEquals(j, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, j));
    }

    @Test
    public void testGetWhereToEndInstrumenting15() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        MethodBlock h = new MethodBlock("H");
        MethodBlock i = new MethodBlock("I");
        MethodBlock j = new MethodBlock("J");
        MethodBlock k = new MethodBlock("K");
        MethodBlock l = new MethodBlock("L");

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
        methodGraph.addMethodBlock(j);
        methodGraph.addMethodBlock(k);
        methodGraph.addMethodBlock(l);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(a, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(f, d);
        methodGraph.addEdge(d, g);
        methodGraph.addEdge(g, h);
        methodGraph.addEdge(g, k);
        methodGraph.addEdge(h, i);
        methodGraph.addEdge(i, j);
        methodGraph.addEdge(j, h);
        methodGraph.addEdge(k, j);
        methodGraph.addEdge(j, l);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(d, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, a));
        Assert.assertEquals(g, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, d));
        Assert.assertEquals(j, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, g));
        Assert.assertEquals(l, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, j));
    }

    @Test
    public void testGetWhereToStartInstrumenting16() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(c);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(d, f);
        methodGraph.addEdge(e, g);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, b);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(b, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(d, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, d));
    }

    @Test
    public void testGetWhereToEndInstrumenting16() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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

        // Add vertices
        methodGraph.addMethodBlock(a);
        methodGraph.addMethodBlock(b);
        methodGraph.addMethodBlock(d);
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);
        methodGraph.addMethodBlock(g);
        methodGraph.addMethodBlock(c);

        // Add edges
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(b, d);
        methodGraph.addEdge(d, e);
        methodGraph.addEdge(d, f);
        methodGraph.addEdge(e, g);
        methodGraph.addEdge(f, g);
        methodGraph.addEdge(g, b);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(c, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, b));
        Assert.assertEquals(g, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, d));
    }

    @Test
    public void testGetWhereToStartInstrumenting17() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, a);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(e, d);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(c, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, c));
        Assert.assertEquals(e, transformer.getBlockToStartInstrumentingBeforeIt(methodGraph, e));
    }

    @Test
    public void testGetWhereToEndInstrumenting17() throws InvocationTargetException, NoSuchMethodException, MalformedURLException, IllegalAccessException {
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
        methodGraph.addMethodBlock(e);
        methodGraph.addMethodBlock(f);

        // Add edges
        methodGraph.addEdge(x, a);
        methodGraph.addEdge(a, b);
        methodGraph.addEdge(b, c);
        methodGraph.addEdge(c, d);
        methodGraph.addEdge(d, a);
        methodGraph.addEdge(c, e);
        methodGraph.addEdge(e, f);
        methodGraph.addEdge(e, d);

        System.out.println(methodGraph.toDotString("test"));

        for(MethodBlock block : methodGraph.getBlocks()) {
            if(block.getSuccessors().size() > 1) {
                System.out.println(block.getID());
            }
        }

        Assert.assertEquals(e, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, c));
        Assert.assertEquals(f, transformer.getBlockToEndInstrumentingBeforeIt(methodGraph, e));
    }

    @Test
    public void testRunningExample() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "running-example";
        String rootPackage = "edu";
        String classDirectory = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        MethodTransformer methodTransformer = new ConfigCrusherTimerTransformer(programName, rootPackage, classDirectory, decisionsToOptions);
        methodTransformer.transformMethods();
    }

}
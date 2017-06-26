package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import edu.cmu.cs.mvelezce.*;
import edu.cmu.cs.mvelezce.gpl.CycleWorkSpace;
import edu.cmu.cs.mvelezce.gpl.Graph;
import edu.cmu.cs.mvelezce.gpl.Vertex;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.ClassTransformerReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilderTest {

    @Test
    public void testVertex() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Vertex.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("bftNodeSearch")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testCycleWorkSpace() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(CycleWorkSpace.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("<init>")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

            if(method.name.equals("checkNeighborAction")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testGraph() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Graph.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("findsVertex")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testDummy6() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Dummy6.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("one")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

            if(method.name.equals("two")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

            if(method.name.equals("three")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }

            if(method.name.equals("four")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testDummy() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Dummy.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(9, methodGraph.getBlockCount());
                Assert.assertEquals(9, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testDummy5() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Dummy5.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(9, methodGraph.getBlockCount());
                Assert.assertEquals(9, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testSleep15() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep15.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(9, methodGraph.getBlockCount());
                Assert.assertEquals(9, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testSleep20() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep20.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph1() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep1.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(9, methodGraph.getBlockCount());
                Assert.assertEquals(9, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph13() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep13.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(8, methodGraph.getBlockCount());
                Assert.assertEquals(8, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph2() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep2.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(12, methodGraph.getBlockCount());
                Assert.assertEquals(12, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph3() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep3.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(14, methodGraph.getBlockCount());
                Assert.assertEquals(15, methodGraph.getEdgeCount());
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph4() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep4.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(9, methodGraph.getBlockCount());
                Assert.assertEquals(9, methodGraph.getEdgeCount());
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph5() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep5.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(11, methodGraph.getBlockCount());
                Assert.assertEquals(11, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph6() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep6.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(11, methodGraph.getBlockCount());
                Assert.assertEquals(11, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph7() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep7.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(14, methodGraph.getBlockCount());
                Assert.assertEquals(15, methodGraph.getEdgeCount());
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph8() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep8.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(14, methodGraph.getBlockCount());
                Assert.assertEquals(15, methodGraph.getEdgeCount());
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph9() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep9.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(14, methodGraph.getBlockCount());
                Assert.assertEquals(15, methodGraph.getEdgeCount());
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph10() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Sleep10.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(14, methodGraph.getBlockCount());
                Assert.assertEquals(15, methodGraph.getEdgeCount());
                System.out.println(methodGraph.toDotString(method.name));
            }
        }
    }

    @Test
    public void testBuildMethodGraph11() throws IOException {
        ClassTransformerReader reader = new ClassTransformerReader();
        ClassNode classNode = reader.readClass(Dummy3.class.getCanonicalName());

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                System.out.println(methodGraph.toDotString(method.name));
                Assert.assertEquals(14, methodGraph.getBlockCount());
                Assert.assertEquals(15, methodGraph.getEdgeCount());
            }
        }
    }

}
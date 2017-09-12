package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import edu.cmu.cs.mvelezce.*;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilderTest {

    @Test
    public void graph0() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph0.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(6, graph.getBlocks().size());
            Assert.assertEquals(6, graph.getEdgeCount());
        }
    }

    @Test
    public void graph1() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph1.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(8, graph.getBlocks().size());
            Assert.assertEquals(9, graph.getEdgeCount());
        }
    }

    @Test
    public void graph2() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph2.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(5, graph.getBlocks().size());
            Assert.assertEquals(5, graph.getEdgeCount());
        }
    }

    @Test
    public void graph3() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph3.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(6, graph.getBlocks().size());
            Assert.assertEquals(6, graph.getEdgeCount());
        }
    }

    @Test
    public void graph4() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph4.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(6, graph.getBlocks().size());
            Assert.assertEquals(6, graph.getEdgeCount());
        }
    }

    @Test
    public void graph5() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph5.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(8, graph.getBlocks().size());
            Assert.assertEquals(9, graph.getEdgeCount());
        }
    }

    @Test
    public void graph6() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ClassTransformer reader = new DefaultBaseClassTransformer(path);
        ClassNode classNode = reader.readClass(Graph6.class.getCanonicalName());

        for(MethodNode methodNode : classNode.methods) {
            if(!methodNode.name.equals("main")) {
                continue;
            }

            MethodGraphBuilder builder = new MethodGraphBuilder(methodNode);
            MethodGraph graph = builder.build();
            System.out.println(graph.toDotString("main"));
            Assert.assertEquals(9, graph.getBlocks().size());
            Assert.assertEquals(11, graph.getEdgeCount());
        }
    }

//    @Test
//    public void testDummy6() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy6.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("one")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//
//            if(method.name.equals("two")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//
//            if(method.name.equals("three")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//
//            if(method.name.equals("four")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testDummy() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testDummy5() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy5.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testSleep15() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep15.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testSleep20() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep20.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
////            if(method.name.equals("main")) {
////                MethodGraph methodGraph = MethodGraphBuilder.build(method);
////                System.out.println(methodGraph.toDotString(method.name));
////            }
//
//            if(method.name.equals("method")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph1() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep1.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph13() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep13.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(8, methodGraph.getBlockCount());
//                Assert.assertEquals(8, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph2() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep2.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(12, methodGraph.getBlockCount());
//                Assert.assertEquals(12, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph3() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep3.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph4() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep4.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph5() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep5.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(11, methodGraph.getBlockCount());
//                Assert.assertEquals(11, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph6() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep6.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(11, methodGraph.getBlockCount());
//                Assert.assertEquals(11, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph7() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep7.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph8() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep8.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph9() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep9.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph10() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep10.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph11() throws IOException {
//        ClassTransformer reader = new DefaultBaseClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy3.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = MethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//            }
//        }
//    }

}
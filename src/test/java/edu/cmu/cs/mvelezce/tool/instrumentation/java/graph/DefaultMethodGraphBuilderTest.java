package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import edu.cmu.cs.mvelezce.Example;
import edu.cmu.cs.mvelezce.Graph0;
import edu.cmu.cs.mvelezce.Graph1;
import edu.cmu.cs.mvelezce.Graph10;
import edu.cmu.cs.mvelezce.Graph11;
import edu.cmu.cs.mvelezce.Graph12;
import edu.cmu.cs.mvelezce.Graph13;
import edu.cmu.cs.mvelezce.Graph14;
import edu.cmu.cs.mvelezce.Graph15;
import edu.cmu.cs.mvelezce.Graph16;
import edu.cmu.cs.mvelezce.Graph2;
import edu.cmu.cs.mvelezce.Graph3;
import edu.cmu.cs.mvelezce.Graph4;
import edu.cmu.cs.mvelezce.Graph5;
import edu.cmu.cs.mvelezce.Graph6;
import edu.cmu.cs.mvelezce.Graph7;
import edu.cmu.cs.mvelezce.Graph8;
import edu.cmu.cs.mvelezce.Graph9;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultClassTransformer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import kanzi.io.CompressedOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.prevayler.implementation.publishing.POBox;

/**
 * Created by mvelezce on 5/3/17.
 */
public class DefaultMethodGraphBuilderTest {

  @Test
  public void graph0()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph0.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(6, graph.getBlocks().size());
      Assert.assertEquals(6, graph.getEdgeCount());
    }
  }

  @Test
  public void graph1()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph1.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(8, graph.getBlocks().size());
      Assert.assertEquals(9, graph.getEdgeCount());
    }
  }

  @Test
  public void graph2()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph2.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      Assert.assertEquals(5, graph.getBlocks().size());
      Assert.assertEquals(5, graph.getEdgeCount());
    }
  }

  @Test
  public void graph3()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph3.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(6, graph.getBlocks().size());
      Assert.assertEquals(6, graph.getEdgeCount());
    }
  }

  @Test
  public void graph4()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph4.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(6, graph.getBlocks().size());
      Assert.assertEquals(6, graph.getEdgeCount());
    }
  }

  @Test
  public void graph5()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph5.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(8, graph.getBlocks().size());
      Assert.assertEquals(9, graph.getEdgeCount());
    }
  }

  @Test
  public void graph6()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph6.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(9, graph.getBlocks().size());
      Assert.assertEquals(11, graph.getEdgeCount());
    }
  }

  @Test
  public void graph7()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph7.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(5, graph.getBlocks().size());
      Assert.assertEquals(5, graph.getEdgeCount());
    }
  }

  @Test
  public void graph8()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph8.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(7, graph.getBlocks().size());
      Assert.assertEquals(8, graph.getEdgeCount());
    }
  }

  @Test
  public void graph9()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph9.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(12, graph.getBlocks().size());
      Assert.assertEquals(18, graph.getEdgeCount());
    }
  }

  @Test
  public void graph10()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph10.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(11, graph.getBlocks().size());
      Assert.assertEquals(17, graph.getEdgeCount());
    }
  }

  @Test
  public void graph11()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph11.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(10, graph.getBlocks().size());
      Assert.assertEquals(14, graph.getEdgeCount());
    }
  }

  @Test
  public void graph12()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph12.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(9, graph.getBlocks().size());
      Assert.assertEquals(13, graph.getEdgeCount());
    }
  }

  @Test
  public void graph13()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph13.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(5, graph.getBlocks().size());
      Assert.assertEquals(4, graph.getEdgeCount());
    }
  }

  @Test
  public void graph14()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph14.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(5, graph.getBlocks().size());
      Assert.assertEquals(4, graph.getEdgeCount());
    }
  }

  @Test
  public void graph15()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph15.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      Assert.assertEquals(4, graph.getBlocks().size());
      Assert.assertEquals(3, graph.getEdgeCount());
    }
  }

  @Test
  public void graph16()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Graph16.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("main")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("main"));
      Assert.assertEquals(4, graph.getBlocks().size());
      Assert.assertEquals(3, graph.getEdgeCount());
    }
  }

  @Test
  public void runningExample()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(Example.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("moo")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      System.out.println(graph.toDotString("moo"));
      Assert.assertEquals(8, graph.getBlocks().size());
      Assert.assertEquals(10, graph.getEdgeCount());
    }
  }

  @Test
  public void prevayler()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/prevayler/target/classes";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(POBox.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("run")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
//            System.out.println(graph.toDotString("recoverPendingTransactions"));
      Assert.assertEquals(8, graph.getBlocks().size());
      Assert.assertEquals(10, graph.getEdgeCount());
    }
  }

  @Test
  public void kanzi()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
    String path = BaseAdapter.USER_HOME
        + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/kanzi/target/classes";
    ClassTransformer reader = new DefaultClassTransformer(path);
    ClassNode classNode = reader.readClass(CompressedOutputStream.class.getCanonicalName());

    for (MethodNode methodNode : classNode.methods) {
      if (!methodNode.name.equals("write") || !methodNode.desc.equals("(I)V")) {
        continue;
      }

      DefaultMethodGraphBuilder builder = new DefaultMethodGraphBuilder();
      MethodGraph graph = builder.build(methodNode);
      graph.getBlocks();
    }
  }

//    @Test
//    public void testDummy6() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy6.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("one")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//
//            if(method.name.equals("two")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//
//            if(method.name.equals("three")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//
//            if(method.name.equals("four")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testDummy() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testDummy5() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy5.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testSleep15() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep15.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testSleep20() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep20.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
////            if(method.name.equals("main")) {
////                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
////                System.out.println(methodGraph.toDotString(method.name));
////            }
//
//            if(method.name.equals("method")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph1() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep1.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph13() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep13.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(8, methodGraph.getBlockCount());
//                Assert.assertEquals(8, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph2() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep2.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(12, methodGraph.getBlockCount());
//                Assert.assertEquals(12, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph3() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep3.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph4() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep4.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                Assert.assertEquals(9, methodGraph.getBlockCount());
//                Assert.assertEquals(9, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph5() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep5.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(11, methodGraph.getBlockCount());
//                Assert.assertEquals(11, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph6() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep6.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(11, methodGraph.getBlockCount());
//                Assert.assertEquals(11, methodGraph.getEdgeCount());
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph7() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep7.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph8() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep8.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph9() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep9.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph10() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Sleep10.FILENAME);
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//                System.out.println(methodGraph.toDotString(method.name));
//            }
//        }
//    }
//
//    @Test
//    public void testBuildMethodGraph11() throws IOException {
//        ClassTransformer reader = new DefaultClassTransformer();
//        ClassNode classNode = reader.readClass(Dummy3.class.getCanonicalName());
//
//        List<MethodNode> methods = classNode.methods;
//
//        for(MethodNode method : methods) {
//            if(method.name.equals("main")) {
//                MethodGraph methodGraph = DefaultMethodGraphBuilder.build(method);
//                System.out.println(methodGraph.toDotString(method.name));
//                Assert.assertEquals(14, methodGraph.getBlockCount());
//                Assert.assertEquals(15, methodGraph.getEdgeCount());
//            }
//        }
//    }

}
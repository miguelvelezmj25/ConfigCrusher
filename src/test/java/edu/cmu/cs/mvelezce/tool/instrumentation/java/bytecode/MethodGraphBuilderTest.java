package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.java.programs.Sleep3;
import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.ClassTransformerBase;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 5/3/17.
 */
public class MethodGraphBuilderTest {

    @Test
    public void testBuildMethodGraph1() throws IOException {
        ClassTransformerBase base = new ClassTransformerBase() {
            @Override
            public Set<ClassNode> transformClasses() throws IOException {
                return null;
            }

            @Override
            public void transform(ClassNode classNode) {

            }
        };
        ClassNode classNode = base.readClass(Sleep1.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(8, methodGraph.getBlockCount());
                Assert.assertEquals(7, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph2() throws IOException {
        ClassTransformerBase base = new ClassTransformerBase() {
            @Override
            public Set<ClassNode> transformClasses() throws IOException {
                return null;
            }

            @Override
            public void transform(ClassNode classNode) {

            }
        };
        ClassNode classNode = base.readClass(Sleep2.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(9, methodGraph.getBlockCount());
                Assert.assertEquals(8, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph3() throws IOException {
        ClassTransformerBase base = new ClassTransformerBase() {
            @Override
            public Set<ClassNode> transformClasses() throws IOException {
                return null;
            }

            @Override
            public void transform(ClassNode classNode) {

            }
        };
        ClassNode classNode = base.readClass(Sleep3.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(13, methodGraph.getBlockCount());
                Assert.assertEquals(13, methodGraph.getEdgeCount());
            }
        }
    }

    @Test
    public void testBuildMethodGraph4() throws IOException {
        ClassTransformerBase base = new ClassTransformerBase() {
            @Override
            public Set<ClassNode> transformClasses() throws IOException {
                return null;
            }

            @Override
            public void transform(ClassNode classNode) {

            }
        };
        ClassNode classNode = base.readClass(Sleep4.FILENAME);

        List<MethodNode> methods = classNode.methods;

        for(MethodNode method : methods) {
            if(method.name.equals("main")) {
                MethodGraph methodGraph = MethodGraphBuilder.buildMethodGraph(method);
                Assert.assertEquals(8, methodGraph.getBlockCount());
                Assert.assertEquals(7, methodGraph.getEdgeCount());
            }
        }
    }

}
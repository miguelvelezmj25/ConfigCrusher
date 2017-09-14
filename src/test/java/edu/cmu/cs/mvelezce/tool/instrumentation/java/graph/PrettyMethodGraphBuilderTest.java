package edu.cmu.cs.mvelezce.tool.instrumentation.java.graph;

import edu.cmu.cs.mvelezce.Example;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.BytecodePrettyPrinter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode.TraceMethodGetter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.ClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.classnode.DefaultBaseClassTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

public class PrettyMethodGraphBuilderTest {

    @Test
    public void runningExample() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String path = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";
        String className = Example.class.getCanonicalName();
        String methodName = "moo";
        String methodSignature = "moo(Z)V";
        ClassTransformer transformer = new DefaultBaseClassTransformer(path);
        ClassNode classNode = transformer.readClass(className);

        MethodGraph graph = null;
        MethodNode methodNode = null;

        for(MethodNode method : classNode.methods) {
            if(!method.name.equals(methodName)) {
                continue;
            }

            methodNode = method;
            MethodGraphBuilder builder = new MethodGraphBuilder(method);
            graph = builder.build();
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
        TraceMethodGetter traceMethodGetter = new TraceMethodGetter(Opcodes.ASM5, traceClassVisitor);
        ClassReader classReader = new ClassReader(className);
        classReader.accept(traceMethodGetter, 0);

        Printer printer = traceMethodGetter.getPrinterForMethodSignature(methodSignature);

        PrettyMethodGraphBuilder prettyBuilder = new PrettyMethodGraphBuilder(methodNode, graph, printer);
        PrettyMethodGraph prettyGraph = prettyBuilder.build();

        System.out.println(prettyGraph.toDotStringVerbsoe(methodName));
    }

}
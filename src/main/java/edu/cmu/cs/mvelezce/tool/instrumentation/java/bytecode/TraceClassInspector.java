package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TraceClassInspector {

    private String className;

    public TraceClassInspector(String className) {
        this.className = className;
    }

    public MethodTracer visitClass() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
        MethodTracer methodTracer = new MethodTracer(Opcodes.ASM5, traceClassVisitor);
        ClassReader classReader = new ClassReader(this.className);

        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        classReader.accept(methodTracer, 0);
        return methodTracer;
    }
}

package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.Textifier;
import jdk.internal.org.objectweb.asm.util.TraceMethodVisitor;

import java.util.HashMap;
import java.util.Map;

public class TraceMethodGetter extends ClassVisitor {

    private Map<String, Printer> methodToPrinter = new HashMap<>();

    public TraceMethodGetter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        String methodSignature = name + desc;
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        Printer p = new Textifier();
        this.methodToPrinter.put(methodSignature, p);

        return new TraceMethodVisitor(mv, p);
    }

    public Map<String, Printer> getMethodToPrinter() {
        return methodToPrinter;
    }
}

package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.Textifier;
import jdk.internal.org.objectweb.asm.util.TraceMethodVisitor;

public class MethodTracer extends ClassVisitor {

  private Map<String, Printer> methodToPrinter = new HashMap<>();

  public MethodTracer(int api, ClassVisitor classVisitor) {
    super(api, classVisitor);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature,
      String[] exceptions) {
    String methodSignature = name + desc;
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    Printer printer = new Textifier();
    this.methodToPrinter.put(methodSignature, printer);

    return new TraceMethodVisitor(mv, printer);
  }

//  public Map<String, Printer> getMethodToPrinter() {
//    return methodToPrinter;
//  }

  public Printer getPrinterForMethodSignature(String signature) {
    return this.methodToPrinter.get(signature);
  }
}

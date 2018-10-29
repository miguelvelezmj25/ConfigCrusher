package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode;

import java.io.IOException;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public interface MethodTransformer {

  Set<MethodNode> getMethodsToInstrument(ClassNode classNode);

  void transformMethod(MethodNode methodNode, ClassNode classNode);

  void transformMethods() throws IOException;

  void transformMethods(Set<ClassNode> classNodes) throws IOException;

}

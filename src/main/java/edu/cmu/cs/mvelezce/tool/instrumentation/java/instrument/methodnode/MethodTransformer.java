package edu.cmu.cs.mvelezce.tool.instrumentation.java.instrument.methodnode;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.Set;

public interface MethodTransformer {

    public Set<MethodNode> getMethodsToInstrument(ClassNode classNode);

    public void transformMethod(MethodNode methodNode);

    public void transformMethods() throws IOException;

    void transformMethods(Set<ClassNode> classNodes) throws IOException;
}

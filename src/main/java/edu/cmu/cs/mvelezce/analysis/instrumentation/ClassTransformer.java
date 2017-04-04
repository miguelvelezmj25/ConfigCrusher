package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

/**
 * Created by mvelezce on 4/3/17.
 */
public interface ClassTransformer {

    public void transform(ClassNode classNode);

    public ClassNode readClass() throws IOException;

    public void writeClass(ClassNode classNode, String fileName) throws IOException;

}

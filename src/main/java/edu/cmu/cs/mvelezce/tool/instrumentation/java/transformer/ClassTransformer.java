package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mvelezce on 4/3/17.
 */
public interface ClassTransformer {

    public Set<ClassNode> transformClasses() throws IOException;

    public void transform(ClassNode classNode);

    public ClassNode readClass(String fileName) throws IOException;

    public void writeClass(ClassNode classNode, String fileName) throws IOException;

}

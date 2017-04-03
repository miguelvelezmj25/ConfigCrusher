package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * Created by mvelezce on 4/3/17.
 */
public interface ClassTransformer {

    public void transform(ClassNode classNode);

}

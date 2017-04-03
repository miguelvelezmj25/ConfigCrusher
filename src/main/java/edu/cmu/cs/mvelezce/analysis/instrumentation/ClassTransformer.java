package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * Created by mvelezce on 4/3/17.
 */
public class ClassTransformer {

    private ClassTransformer classTransformer;

    public ClassTransformer(ClassTransformer classTransformer) {
        this.classTransformer = classTransformer;
    }

    public void transform(ClassNode classNode) {
        if(this.classTransformer != null) {
            this.classTransformer.transform(classNode);
        }

    }
}

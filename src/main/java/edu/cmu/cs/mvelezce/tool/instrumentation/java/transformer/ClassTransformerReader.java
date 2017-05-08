package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mvelezce on 5/8/17.
 */
public class ClassTransformerReader extends ClassTransformerBase {

    @Override
    public Set<ClassNode> transformClasses() throws IOException {
        throw new UnsupportedOperationException("This class is just for reading class files");
    }

    @Override
    public void transform(ClassNode classNode) {
        throw new UnsupportedOperationException("This class is just for reading class files");
    }
}

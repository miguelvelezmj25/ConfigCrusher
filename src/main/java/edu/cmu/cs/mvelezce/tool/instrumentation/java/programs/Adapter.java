package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.util.Set;

/**
 * Created by mvelezce on 4/26/17.
 */
public abstract class Adapter extends ClassLoader {
    public static final String MAIN = "main";

    protected Set<ClassNode> instrumentedClassNodes;

    public Adapter(Set<ClassNode> instrumentedClassNodes) {
        this.instrumentedClassNodes = instrumentedClassNodes;
    }

    public abstract void execute(Set<String> configuration) throws ClassNotFoundException, NoSuchMethodException;

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        for(ClassNode classNode : this.instrumentedClassNodes) {
            if(classNode.name.replace("/", ".").equals(name)) {
                return this.findClass(name);
            }
        }

        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for(ClassNode classNode : this.instrumentedClassNodes) {
            if(classNode.name.replace("/", ".").equals(name)) {
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                classNode.accept(classWriter);

                byte[] bytes = classWriter.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            }
        }

        throw new RuntimeException("The class node was not found for this name");
    }
}

package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by mvelezce on 4/3/17.
 */
public abstract class ClassTransformerBase implements ClassTransformer {

    private String fileName;

    public ClassTransformerBase(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ClassNode readClass() throws IOException {
        ClassReader classReader = new ClassReader(this.fileName);
        ClassNode classNode =  new ClassNode();
        classReader.accept(classNode, 0);

        return classNode;
    }

    @Override
    public void writeClass(ClassNode classNode, String fileName) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

        DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(this.fileName + ".class")));
        output.write(classWriter.toByteArray());
        output.flush();
        output.close();
    }

}

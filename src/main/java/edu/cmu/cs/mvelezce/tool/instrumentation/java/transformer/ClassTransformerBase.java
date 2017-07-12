package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mvelezce on 4/3/17.
 */
public abstract class ClassTransformerBase implements ClassTransformer {

    @Override
    public ClassNode readClass(String fileName) throws IOException {
        System.out.println(fileName);
        ClassReader classReader = new ClassReader(fileName);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        return classNode;
    }

    @Override
    public void writeClass(ClassNode classNode, String fileName) throws IOException {
        ClassWriter classWriter = new MyClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(fileName + ".class")));
        output.write(classWriter.toByteArray());
        output.flush();
        output.close();
    }

}

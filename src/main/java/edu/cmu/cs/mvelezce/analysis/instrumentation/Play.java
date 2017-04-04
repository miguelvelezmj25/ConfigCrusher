package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by mvelezce on 3/31/17.
 */
public class Play {

    public static void main(String[] args) {
        inc2(2);
    }

    public static void inc1(int a) {
//        System.out.println("Mom");
        int result = a + 1;
//        System.out.println(result);
    }

    public static void inc2(int a) {
//        Timer.startTimer("MiguelId");
        int result = a + 1;
//        System.out.println(result);
//        Timer.stopTimer("MiguelId");
    }

    public void inc3(int a) {
//        System.nanoTime();
        int result = a + 1;
        System.out.println(result);
//        System.nanoTime();
//        long finalTime = end - start;
    }

    public static void readJar(String jar) throws IOException {
        JarFile file = new JarFile(jar);
        Enumeration<JarEntry> entries = file.entries();

        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if(entry.getName().endsWith(".class")) {
                String name = entry.getName();
                name = name.substring(0, name.length() - 6);
                ClassReader classReader = new ClassReader(name);
                ClassNode classNode =  new ClassNode();
                classReader.accept(classNode, 0);
//                System.out.println(classNode.name);
            }
        }
    }

    public static void readFile(String fileName) throws IOException {
        ClassReader classReader = new ClassReader(fileName);
        ClassNode classNode =  new ClassNode();
        classReader.accept(classNode, 0);

        for(MethodNode method : classNode.methods) {
//            System.out.println(method.instructions.size());
        }
    }

//    public static void instrument(String fileName) throws IOException {
//        ClassReader classReader = new ClassReader(fileName);
//        ClassNode classNode =  new ClassNode();
//        classReader.accept(classNode, 0);
//
//        ClassTransformer classTransformer = new ClassTransformerTimer(fileName, null);
//        classTransformer.transform(classNode);
//
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        classNode.accept(classWriter);
//        File newfile = new File("target/classes/");
//        DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(newfile, fileName + ".class")));
//        output.write(classWriter.toByteArray());
//        output.flush();
//        output.close();
//    }

}

package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by mvelezce on 3/31/17.
 */
public class Play {

    public void inc(int a) {
        int result = a + 1;
        System.out.println(result);
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
//            if(method.name.equals(("inc"))) {
//                System.out.println(method.localVariables);
//                System.out.println(method.instructions);
//            }
        }
    }

}

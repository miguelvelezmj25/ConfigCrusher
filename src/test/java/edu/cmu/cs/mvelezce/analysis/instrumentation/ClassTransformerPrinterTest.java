package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/3/17.
 */
public class ClassTransformerPrinterTest {

    private static final String FILE_NAME = "edu/cmu/cs/mvelezce/analysis/instrumentation/Play";

    @Test
    public void testTransform() throws Exception {
        ClassReader classReader = new ClassReader(ClassTransformerPrinterTest.FILE_NAME);
        ClassNode classNode =  new ClassNode();
        classReader.accept(classNode, 0);

        Map<MethodNode, Integer> methodToInstructionCount = new HashMap<>();

        for(MethodNode methodNode : classNode.methods) {
            methodToInstructionCount.put(methodNode, methodNode.instructions.size());
        }

        Set<String> methods = new HashSet<>();
        methods.add("inc1");

        ClassTransformer classTransformer = new ClassTransformerPrinter(methods, "Hello, world!");
        classTransformer.transform(classNode);

        boolean transformed = false;

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode)) {
                transformed = true;
            }
        }

        Assert.assertTrue(transformed);
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        classNode.accept(classWriter);
//        File newfile = new File("target/classes/");
//        DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(newfile, FILE_NAME + ".class")));
//        output.write(classWriter.toByteArray());
//        output.flush();
//        output.close();
    }

}
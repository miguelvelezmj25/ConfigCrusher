package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/3/17.
 */
public class ClassTransformerPrinterTest {

    @Test
    public void testTransform1() throws IOException {
        int methodNumber = 1;
        Set<String> methods = new HashSet<>();
        methods.add(ClassTransformerBaseTest.METHOD + methodNumber);

        ClassTransformer classTransformer = new ClassTransformerPrinter(ClassTransformerBaseTest.FILE_TO_INSTRUMENT_NAME, methods, "Hello, world!");
        ClassNode classNode = classTransformer.readClass();
        classTransformer.transform(classNode);
        classTransformer.writeClass(classNode, ClassTransformerBaseTest.CLASS_CONTAINER + ClassTransformerBaseTest.FILE_TO_INSTRUMENT_NAME);

        Map<String, Integer> methodToInstructionCount = new HashMap<>();

        for(MethodNode methodNode : classNode.methods) {
            methodToInstructionCount.put(methodNode.name, methodNode.instructions.size());
        }

        classTransformer = new ClassTransformerPrinter(ClassTransformerBaseTest.FILE_TO_INSTRUMENT_NAME, methods, "Hello, world!");
        classNode = classTransformer.readClass();
        classTransformer.transform(classNode);

        boolean transformed = false;

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode.name)) {
                transformed = true;
            }
        }

        Assert.assertTrue(transformed);

//        String command = "java -cp " + ClassTransformerBaseTest.CLASS_CONTAINER + " " + ClassTransformerBaseTest.PACKAGE + "." + ClassTransformerBaseTest.CONTROLLER_CLASS + " " + methodNumber;
//        String output = ClassTransformerBaseTest.executeCommand(command);
//        Assert.assertNotEquals(0, output.length());
    }

}
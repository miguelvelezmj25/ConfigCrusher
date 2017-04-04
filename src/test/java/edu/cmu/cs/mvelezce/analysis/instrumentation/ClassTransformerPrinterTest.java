package edu.cmu.cs.mvelezce.analysis.instrumentation;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/3/17.
 */
public class ClassTransformerPrinterTest {

    private static final String FILE_NAME = "edu/cmu/cs/mvelezce/analysis/instrumentation/Play";

    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        Process process;

        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(output);
        return output.toString();
    }

    @Test
    public void testTransform() throws Exception {
        String command = "java -cp ./target/classes edu.cmu.cs.mvelezce.analysis.instrumentation.Play";
        String output = ClassTransformerPrinterTest.executeCommand(command);
        Assert.assertEquals(0, output.length());

        Set<String> methods = new HashSet<>();
        methods.add("inc1");

        ClassTransformer classTransformer = new ClassTransformerPrinter(ClassTransformerPrinterTest.FILE_NAME, methods, "Hello, world!");
        ClassNode classNode = classTransformer.readClass();

        Map<MethodNode, Integer> methodToInstructionCount = new HashMap<>();

        for(MethodNode methodNode : classNode.methods) {
            methodToInstructionCount.put(methodNode, methodNode.instructions.size());
        }

        classTransformer.transform(classNode);

        boolean transformed = false;

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode)) {
                transformed = true;
            }
        }

        Assert.assertTrue(transformed);

        classTransformer.writeClass(classNode, "target/classes/" + ClassTransformerPrinterTest.FILE_NAME);

        output = ClassTransformerPrinterTest.executeCommand(command);
        Assert.assertNotEquals(0, output.length());
    }

}
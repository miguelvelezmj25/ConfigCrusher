package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mvelezce on 4/3/17.
 */
public class JavaRegionClassTransformerPrinterTest {

    @Test
    public void testTransform1() throws IOException, CloneNotSupportedException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep.PACKAGE, Sleep.CLASS, Sleep.MAIN_METHOD, 23, 24);
        Regions.addRegion(region);

        // Get class
        JavaRegionClassTransformerPrinter printer = new JavaRegionClassTransformerPrinter(Sleep.FILENAME, "Money!");
        ClassNode classNode = printer.readClass();

        // Save size of instructions for each method in the class
        Map<String, Integer> methodToInstructionCount = new HashMap<>();

        for(MethodNode methodNode : classNode.methods) {
            methodToInstructionCount.put(methodNode.name, methodNode.instructions.size());
        }

        // Transform the Java region
        printer.transform(classNode);

        // Check if the transform actually made changes to the bytecode
        boolean transformed = false;

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode.name)) {
                transformed = true;
                break;
            }
        }

        Assert.assertTrue(transformed);

//        // Actually modify the class file
//        printer.writeClass(classNode, ClassTransformerBaseTest.CLASS_CONTAINER + Sleep.FILENAME.replace(".", "/"));
//
//        String command = "java -cp " + ClassTransformerBaseTest.CLASS_CONTAINER + " " + Sleep.FILENAME  + " true";
//        String output = ClassTransformerBaseTest.executeCommand(command);
//
//        Assert.assertNotEquals(0, output.length());
    }

    @Test
    public void testTransform2() throws IOException, CloneNotSupportedException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep.PACKAGE, Sleep.CLASS, Sleep.METHOD_1, 19, 20);
        Regions.addRegion(region);

        // Get class
        JavaRegionClassTransformerPrinter printer = new JavaRegionClassTransformerPrinter(Sleep.FILENAME, "Money!");
        ClassNode classNode = printer.readClass();

        // Save size of instructions for each method in the class
        Map<String, Integer> methodToInstructionCount = new HashMap<>();

        for(MethodNode methodNode : classNode.methods) {
            methodToInstructionCount.put(methodNode.name, methodNode.instructions.size());
        }

        // Transform the Java region
        printer.transform(classNode);

        // Check if the transform actually made changes to the bytecode
        boolean transformed = false;

        for(MethodNode methodNode : classNode.methods) {
            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode.name)) {
                transformed = true;
                break;
            }
        }

        Assert.assertTrue(transformed);

//        // Actually modify the class file
//        printer.writeClass(classNode, ClassTransformerBaseTest.CLASS_CONTAINER + Sleep.FILENAME.replace(".", "/"));
//
//        String command = "java -cp " + ClassTransformerBaseTest.CLASS_CONTAINER + " " + Sleep.FILENAME  + " true";
//        String output = ClassTransformerBaseTest.executeCommand(command);
//
//        Assert.assertNotEquals(0, output.length());
    }

}
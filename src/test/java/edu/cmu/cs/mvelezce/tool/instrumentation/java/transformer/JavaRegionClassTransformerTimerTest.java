package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class JavaRegionClassTransformerTimerTest {

    @After
    public void run() throws CloneNotSupportedException, InterruptedException {
//        String[] args = new String[1];
//        args[0] = "true";
//        Sleep1.main(args);
    }

    @Test
    public void transform1() throws IOException, CloneNotSupportedException, InterruptedException, ClassNotFoundException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 38, 39);
        Regions.addRegion(region);

        // Get class
        JavaRegionClassTransformerTimer printer = new JavaRegionClassTransformerTimer(Sleep1.FILENAME);
        ClassNode classNode = printer.readClass();

        // TODO abstract
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

        // Actually modify the class file
        printer.writeClass(classNode);

        String[] args = new String[1];
        args[0] = "true";
        Sleep1.main(args);

//        String command = "java -cp " + ClassTransformerBaseTest.CLASS_CONTAINER + " " + Sleep1.FILENAME  + " true";
//        String output = ClassTransformerBaseTest.executeCommand(command);

//        Assert.assertNotEquals(0, output.length());
    }

}
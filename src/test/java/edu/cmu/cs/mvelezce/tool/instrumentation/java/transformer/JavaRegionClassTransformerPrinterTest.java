package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Adapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
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
public class JavaRegionClassTransformerPrinterTest {

    @Test
    public void testTransform1() throws IOException, CloneNotSupportedException, NoSuchMethodException, ClassNotFoundException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
        Regions.addRegion(region);

        // Get class
        JavaRegionClassTransformerPrinter printer = new JavaRegionClassTransformerPrinter(Sleep1.FILENAME, "Money!");
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

        // Assert
        Assert.assertTrue(transformed);

        // Execute instrumented code
        Set<ClassNode> instrumentedClasses = new HashSet<>();
        instrumentedClasses.add(classNode);

        Set<String> configuration = new HashSet<>();
        configuration.add("true");

        Adapter adapter = new SleepAdapter(Sleep1.FILENAME, instrumentedClasses, configuration);
        adapter.execute();
    }

    @Test
    public void testTransform2() throws IOException, CloneNotSupportedException, NoSuchMethodException, ClassNotFoundException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 19, 20);
        Regions.addRegion(region);

        // Get class
        JavaRegionClassTransformerPrinter printer = new JavaRegionClassTransformerPrinter(Sleep2.FILENAME, "Money!");
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

        // Assert
        Assert.assertTrue(transformed);

        // Execute instrumented code
        Set<ClassNode> instrumentedClasses = new HashSet<>();
        instrumentedClasses.add(classNode);

        Set<String> configuration = new HashSet<>();
        configuration.add("true");

        Adapter adapter = new SleepAdapter(Sleep2.FILENAME, instrumentedClasses, configuration);
        adapter.execute();
    }

}
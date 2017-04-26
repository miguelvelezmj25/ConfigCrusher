package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.*;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class JavaRegionClassTransformerTimerTest {

    @Before
    public void before() {
        Regions.reset();
    }

    @After
    public void after() {
        System.out.println();
    }

    public static void execute(String fileName, Set<String> configuration) throws IOException, NoSuchMethodException, ClassNotFoundException {
        // Get class
        JavaRegionClassTransformerTimer printer = new JavaRegionClassTransformerTimer(fileName);
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

        Adapter adapter = new SleepAdapter(fileName, instrumentedClasses);
        adapter.execute(configuration);
    }

    @Test
    public void testTransform1() throws IOException, CloneNotSupportedException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
        Regions.addRegion(region);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        JavaRegionClassTransformerTimerTest.execute(Sleep1.FILENAME, configuration);

        // Assert it executed
        Set<Region> regions = Regions.getRegions();
        Region program = Regions.getProgram();

        Assert.assertTrue(program.getMilliExecutionTime() > 900.0);

        System.out.println("Program execution time: " + program.getMilliExecutionTime());

        for(Region measuredRegion : regions) {
            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
        }
    }

    @Test
    public void testTransform2() throws IOException, CloneNotSupportedException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.MAIN_METHOD, 23, 28);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 19, 20);
        Regions.addRegion(region);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        JavaRegionClassTransformerTimerTest.execute(Sleep2.FILENAME, configuration);

        // Assert it executed
        Set<Region> regions = Regions.getRegions();
        Region program = Regions.getProgram();

        Assert.assertTrue(program.getMilliExecutionTime() > 1800.0);

        System.out.println("Program execution time: " + program.getMilliExecutionTime());

        for(Region measuredRegion : regions) {
            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
        }
    }

    @Test
    public void testTransform3() throws IOException, CloneNotSupportedException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 23, 36);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 19, 20);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 19, 20);
        Regions.addRegion(region);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");

        JavaRegionClassTransformerTimerTest.execute(Sleep3.FILENAME, configuration);

        // Assert it executed
        Set<Region> regions = Regions.getRegions();
        Region program = Regions.getProgram();

        Assert.assertTrue(program.getMilliExecutionTime() > 2600.0);

        System.out.println("Program execution time: " + program.getMilliExecutionTime());

        for(Region measuredRegion : regions) {
            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
        }
    }

    @Test
    public void testTransform4() throws IOException, CloneNotSupportedException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 45, 53);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
        Regions.addRegion(region);

        // Configuration
        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");

        JavaRegionClassTransformerTimerTest.execute(Sleep4.FILENAME, configuration);

        // Assert it executed
        Set<Region> regions = Regions.getRegions();
        Region program = Regions.getProgram();

        Assert.assertTrue(program.getMilliExecutionTime() > 3500.0);

        System.out.println("Program execution time: " + program.getMilliExecutionTime());

        for(Region measuredRegion : regions) {
            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
        }
    }
}
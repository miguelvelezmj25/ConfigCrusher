package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer;

/**
 * Created by miguelvelez on 4/9/17.
 */
public class JavaRegionClassTransformerTimerTest {

//    @After
//    public void after() {
//        System.out.println();
//    }
//
//    // TODO we will have to pass multiple file names when instrumenting multiple files
//    public static void execute(List<String> fileNames, Set<String> configuration, Set<JavaRegion> regions) throws IOException, NoSuchMethodException, ClassNotFoundException {
//        // Get class
//        JavaRegionClassTransformerTimer printer = new JavaRegionClassTransformerTimer(fileNames, regions);
//        ClassNode classNode = printer.readClass();
//
//        // Save size of instructions for each method in the class
//        Map<String, Integer> methodToInstructionCount = new HashMap<>();
//
//        for(MethodNode methodNode : classNode.methods) {
//            methodToInstructionCount.put(methodNode.name, methodNode.instructions.size());
//        }
//
//        // Transform the Java region
//        printer.transform(classNode);
//
//        // Check if the transform actually made changes to the bytecode
//        boolean transformed = false;
//
//        for(MethodNode methodNode : classNode.methods) {
//            if(methodNode.instructions.size() != methodToInstructionCount.get(methodNode.name)) {
//                transformed = true;
//                break;
//            }
//        }
//
//        // Assert
//        Assert.assertTrue(transformed);
//
//        // Execute instrumented code
//        Set<ClassNode> instrumentedClasses = new HashSet<>();
//        instrumentedClasses.add(classNode);
//
//        DynamicAdapter.setInstrumentedClassNodes(instrumentedClasses);
//        DynamicAdapter adapter = new SleepDynamicAdapter(fileNames);
//        adapter.execute(configuration);
//    }
//
//    @Test
//    public void testTransform1() throws IOException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Set<Region> regions = new HashSet<>();
//        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
//        regions.add(region);
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep1.FILENAME);
////        JavaRegionClassTransformer.setMainClass(Sleep1.FILENAME);
//
//        // Configuration
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//
//        JavaRegionClassTransformerTimerTest.execute(Sleep1.FILENAME, configuration, regions);
//
//        // Assert it executed
//        Set<Region> regions = Regions.getRegions();
//        Region program = Regions.getProgram();
//
//        Assert.assertTrue(program.getMilliExecutionTime() > 900.0);
//
//        System.out.println("Program execution time: " + program.getMilliExecutionTime());
//
//        for(Region measuredRegion : regions) {
//            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
//        }
//    }
//
//    @Test
//    public void testTransform2() throws IOException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.MAIN_METHOD, 23, 28);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 19, 20);
//        Regions.addRegion(region);
//
//        // Program
//        JavaRegionClassTransformer.setMainClass(Sleep2.FILENAME);
//
//        // Configuration
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//
//        JavaRegionClassTransformerTimerTest.execute(Sleep2.FILENAME, configuration);
//
//        // Assert it executed
//        Set<Region> regions = Regions.getRegions();
//        Region program = Regions.getProgram();
//
//        Assert.assertTrue(program.getMilliExecutionTime() > 1800.0);
//
//        System.out.println("Program execution time: " + program.getMilliExecutionTime());
//
//        for(Region measuredRegion : regions) {
//            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
//        }
//    }
//
//    @Test
//    public void testTransform3() throws IOException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 23, 36);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 19, 20);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 19, 20);
//        Regions.addRegion(region);
//
//        // Program
//        JavaRegionClassTransformer.setMainClass(Sleep3.FILENAME);
//
//        // Configuration
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//
//        JavaRegionClassTransformerTimerTest.execute(Sleep3.FILENAME, configuration);
//
//        // Assert it executed
//        Set<Region> regions = Regions.getRegions();
//        Region program = Regions.getProgram();
//
//        Assert.assertTrue(program.getMilliExecutionTime() > 2600.0);
//
//        System.out.println("Program execution time: " + program.getMilliExecutionTime());
//
//        for(Region measuredRegion : regions) {
//            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
//        }
//    }
//
//    @Test
//    public void testTransform4() throws IOException, InterruptedException, ClassNotFoundException, NoSuchMethodException {
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 48, 53);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
//        Regions.addRegion(region);
//
//        // Program
//        JavaRegionClassTransformer.setMainClass(Sleep4.FILENAME);
//
//        // Configuration
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//
//        JavaRegionClassTransformerTimerTest.execute(Sleep4.FILENAME, configuration);
//
//        // Assert it executed
//        Set<Region> regions = Regions.getRegions();
//        Region program = Regions.getProgram();
//
//        Assert.assertTrue(program.getMilliExecutionTime() > 3500.0);
//
//        System.out.println("Program execution time: " + program.getMilliExecutionTime());
//
//        for(Region measuredRegion : regions) {
//            System.out.println(((JavaRegion) measuredRegion).getRegionMethod() + " Execution time: " + measuredRegion.getMilliExecutionTime());
//        }
//    }
}
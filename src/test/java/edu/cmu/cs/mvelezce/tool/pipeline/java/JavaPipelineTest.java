package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.*;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.PipelineTest;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipelineTest {

    public static final double TIMING_ERROR = 0.1;

    public static void checkExecutionTimes(Set<PerformanceEntry> expectedPerformances, Set<PerformanceEntry> actualPerformances) {
        for(PerformanceEntry expected : expectedPerformances) {
            for(PerformanceEntry actual : actualPerformances) {
                for(Region expectedRegion : expected.getRegions()) {
                    for(Region actualRegion : actual.getRegions()) {
                        if(expected.getConfiguration().equals(actual.getConfiguration()) && expectedRegion.equals(actualRegion)) {
                            System.out.println("Configuration: " + actual.getConfiguration());
                            System.out.println("Expected: " + expectedRegion.getExecutionTime()/1000.0);
                            System.out.println("Actual: " + actualRegion.getSecondsExecutionTime());
                            Assert.assertEquals(actualRegion.getSecondsExecutionTime(), expectedRegion.getExecutionTime()/1000.0, JavaPipelineTest.TIMING_ERROR);
                        }
                    }
                }
            }

            System.out.println();
        }
    }

    @Before
    public void before() {
        Regions.reset();
    }

    @Test
    public void testInstrumentRelevantRegions1() throws Exception {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 48, 53);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
        Regions.addRegion(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
        Regions.addRegion(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep4.FILENAME);

        // Instrument and assert
        Assert.assertTrue(JavaPipeline.instrumentRelevantRegions(Sleep4.FILENAME, programFiles).size() > 0);
    }

    @Test
    public void testMeasureConfigurationPerformance1() throws Exception {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 48, 53);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
        Regions.addRegion(region3);

        JavaRegion region4 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
        Regions.addRegion(region4);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep4.FILENAME);

        // Instrument
        Set<ClassNode> instrumentedClasses = JavaPipeline.instrumentRelevantRegions(Sleep4.FILENAME, programFiles);

        // Set of performance entries
        Set<PerformanceEntry> measuredPerformance = new HashSet<>();

        // Empty configuration
        Set<String> configuration = new HashSet<>();
        Regions.getProgram().startTime(0);
        Regions.getProgram().endTime(300);
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram());
        measuredPerformance.add(performanceEntry);

        // Configuration A
        configuration = new HashSet<>();
        configuration.add("A");
        Regions.resetRegions();
        Regions.getRegion(region1).startTime(0);
        Regions.getRegion(region1).endTime(1500);
        Regions.getRegion(region3).startTime(0);
        Regions.getRegion(region3).endTime(600);
        Regions.getProgram().startTime(0);
        Regions.getProgram().endTime(1800);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram());
        measuredPerformance.add(performanceEntry);

        // Configuration B
        configuration = new HashSet<>();
        configuration.add("B");
        Regions.resetRegions();
        Regions.getRegion(region2).startTime(0);
        Regions.getRegion(region2).endTime(1700);
        Regions.getRegion(region4).startTime(0);
        Regions.getRegion(region4).endTime(600);
        Regions.getProgram().startTime(0);
        Regions.getProgram().endTime(1900);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram());
        measuredPerformance.add(performanceEntry);

        // Configuration AB
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Regions.resetRegions();
        Regions.getRegion(region1).startTime(0);
        Regions.getRegion(region1).endTime(1500);
        Regions.getRegion(region2).startTime(0);
        Regions.getRegion(region2).endTime(1700);
        Regions.getRegion(region3).startTime(0);
        Regions.getRegion(region3).endTime(600);
        Regions.getRegion(region4).startTime(0);
        Regions.getRegion(region4).endTime(600);
        Regions.getProgram().startTime(0);
        Regions.getProgram().endTime(3500);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram());
        measuredPerformance.add(performanceEntry);

        // Configurations
        Set<Set<String>> optionsSet = PipelineTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Assert
        Set<PerformanceEntry> results = JavaPipeline.measureConfigurationPerformance(Sleep4.FILENAME, instrumentedClasses, configurationsToExecute);

        Assert.assertEquals(measuredPerformance, results);
        JavaPipelineTest.checkExecutionTimes(measuredPerformance, results);
    }

    public static void compareRegionOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
        // program, regions, options, BF configurations, constraints, compressed configurations, compressed over BF
        if(csv) {
            System.out.print(program + ", ");
        }
        else {
            System.out.println(program);
        }

        Map<JavaRegion, Set<String>> queryResult = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
        if(csv) {
            System.out.print(queryResult.size() + ", ");
        }
        else {
            System.out.println("Lotrack total number of regions: " + queryResult.size());
        }
        queryResult = LotrackProcessor.filterBooleans(queryResult);
        queryResult = LotrackProcessor.filterRegionsNoOptions(queryResult);

        Set<Set<String>> optionsSet = new HashSet<>(queryResult.values());
        Set<String> uniqueOptions = new HashSet<>();

        for(Set<String> options : optionsSet) {
            uniqueOptions.addAll(options);
        }

        JavaPipelineTest.compare(optionsSet, uniqueOptions, csv);
    }

    public static void compareOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
        // program, entries, options, BF configurations, constraints, compressed configurations, compressed over BF
        if(csv) {
            System.out.print(program + ", ");
        }
        else {
            System.out.println(program);
        }

        List<String> fields = new ArrayList<>();
        fields.add(LotrackProcessor.USED_TERMS);
        fields.add(LotrackProcessor.CONSTRAINT);

        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
        List<String> queryResult = ScalaMongoDriverConnector.findProjection(program, fields);
        ScalaMongoDriverConnector.close();

        if(csv) {
            System.out.print(queryResult.size() + ", ");
        }
        else {
            System.out.println("Lotrack total number of entries: " + queryResult.size());
        }

        Set<Set<String>> optionsSet = new HashSet<>();

//        for(String result : queryResult) {
//            JSONObject JSONResult = new JSONObject(result);
//            Set<String> options = new HashSet<>();
//
//            if(JSONResult.has(LotrackProcessor.USED_TERMS)) {
//                for(Object string : JSONResult.getJSONArray(LotrackProcessor.USED_TERMS).toList()) {
//                    String possibleString = string.toString();
//
//                    if(possibleString.equals("true") || possibleString.equals("false")) {
//                        continue;
//                    }
//
//                    options.add(string.toString());
//                }
//            }
//            else if(JSONResult.has(LotrackProcessor.CONSTRAINT)) {
//                // Be careful that this is imprecise since the constraints can be very large and does not fit in the db field
//                String[] constraints = JSONResult.getString(LotrackProcessor.CONSTRAINT).split(" ");
//
//                for(String constraint : constraints) {
//                    constraint = constraint.replaceAll("[()^|!=]", "");
//                    if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
//                        continue;
//                    }
//
//                    if(constraint.contains(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
//                        constraint = constraint.split(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
//                    }
//
//                    // Because the constraint gotten from Lotrack might be too long
//                    if(constraint.contains(".")) {
//                        continue;
//                    }
//
//                    if(constraint.equals("false") || constraint.equals("true")) {
//                        continue;
//                    }
//
//                    options.add(constraint);
//                }
//            }
//            else {
//                throw new NoSuchFieldException("The query result does not have neither a " + LotrackProcessor.USED_TERMS + " or " + LotrackProcessor.CONSTRAINT + " fields");
//            }
//
//            if(!options.isEmpty()) {
//                optionsSet.add(options);
//            }
//
//        }

        Set<String> uniqueOptions = new HashSet<>();

        for(Set<String> options : optionsSet) {
            uniqueOptions.addAll(options);
        }

        JavaPipelineTest.compare(optionsSet, uniqueOptions, csv);
    }

    public static void compare(Set<Set<String>> optionsSet, Set<String> uniqueOptions, boolean csv) {
        if(csv) {
            System.out.print(uniqueOptions.size() + ", ");
            System.out.print((int) Math.pow(2, uniqueOptions.size()) + ", ");
            System.out.print(optionsSet.size() + ", ");
        }
        else {
            System.out.println("Number of options: " + uniqueOptions.size());
            System.out.println("Brute force number of configurations: " + (int) Math.pow(2, uniqueOptions.size()));
            System.out.println("Number of constraints: " + optionsSet.size());
        }

        Set<Set<String>> configurations = edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.getConfigurationsToExecute(optionsSet);
        PipelineTest.checkConfigurationIsStatisfied(optionsSet, configurations);

        if(csv) {
            System.out.print(configurations.size() + ", ");
            System.out.println(configurations.size()/Math.pow(2, uniqueOptions.size()));
        }
        else {
            System.out.println("Compressed number of configurations: " + configurations.size());
            System.out.println("Compressed over BF: " + configurations.size()/Math.pow(2, uniqueOptions.size()));
        }
    }

    @Test
    public void testCompareRegionOptionsCompressionToBF1() throws NoSuchFieldException {
        JavaPipelineTest.compareRegionOptionsCompressionToBF("platypus", false);
    }

    @Test
    public void testCompareRegionOptionsCompressionToBFAll1() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                JavaPipelineTest.compareRegionOptionsCompressionToBF(collection, false);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//            System.out.println();
//        }
    }

    @Test
    public void testCompareRegionOptionsCompressionToBFALL2() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                JavaPipelineTest.compareRegionOptionsCompressionToBF(collection, true);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//        }
    }

    @Test
    public void testCompareOptionsCompressionToBF1() throws NoSuchFieldException {
        JavaPipelineTest.compareOptionsCompressionToBF("MGrid", false);
    }

    @Test
    public void testCompareOptionsCompressionToBF1All1() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                JavaPipelineTest.compareOptionsCompressionToBF(collection, false);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//            System.out.println();
//        }
    }

    @Test
    public void testCompareOptionsCompressionToBFAll2() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                JavaPipelineTest.compareOptionsCompressionToBF(collection, true);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//        }
    }

    @Test
    public void testBuildPerformanceModel4() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 48, 53);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
        Regions.addRegion(region3);

        JavaRegion region4 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
        Regions.addRegion(region4);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("B");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region3, options);

        options = new HashSet<>();
        options.add("B");
        regionsToOptions.put(region4, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep4.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep4.FILENAME, programFiles, regionsToOptions);

        // Compare
        double performance = 0.3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 3.5;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 2.0;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel1() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
        Regions.addRegion(region1);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep1.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep1.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);

        // Compare
        double performance = 0.3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 0.9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel2() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.MAIN_METHOD, 23, 28);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 19, 20);
        Regions.addRegion(region2);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep2.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep2.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);

        // Compare
        double performance = 0.3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel3() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 23, 36);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 19, 20);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 19, 20);
        Regions.addRegion(region3);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region3, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep3.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep3.FILENAME, programFiles, regionsToOptions);
        System.out.println(performanceModel);

        // Compare
        double performance = 0.3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 2.6;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel5() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep5.PACKAGE, Sleep5.CLASS, Sleep5.MAIN_METHOD, 31, 44);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep5.PACKAGE, Sleep5.CLASS, Sleep5.METHOD_1, 19, 20);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep5.PACKAGE, Sleep5.CLASS, Sleep5.METHOD_2, 19, 20);
        Regions.addRegion(region3);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        options.add("B");
        regionsToOptions.put(region3, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep5.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep5.FILENAME, programFiles, regionsToOptions);
        System.out.println(performanceModel);

        // Compare
        double performance = 0.3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 2.9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 0.3;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 3.5;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel6() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep6.PACKAGE, Sleep6.CLASS, Sleep6.MAIN_METHOD, 23, 24);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep6.PACKAGE, Sleep6.CLASS, Sleep6.MAIN_METHOD, 29, 30);
        Regions.addRegion(region2);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep6.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep6.FILENAME, programFiles, regionsToOptions);
        System.out.println(performanceModel);

        // Compare
        double performance = 0.9;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 0.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel7() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep7.PACKAGE, Sleep7.CLASS, Sleep7.MAIN_METHOD, 23, 24);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep7.PACKAGE, Sleep7.CLASS, Sleep7.MAIN_METHOD, 29, 30);
        Regions.addRegion(region2);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep7.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep7.FILENAME, programFiles, regionsToOptions);
        System.out.println(performanceModel);

        // Compare
        double performance = 1.0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 0.9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel8() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.MAIN_METHOD, 31, 36);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.MAIN_METHOD, 41, 46);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.METHOD_1, 19, 20);
        Regions.addRegion(region3);

        JavaRegion region4 = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.METHOD_2, 19, 20);
        Regions.addRegion(region4);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region3, options);

        options = new HashSet<>();
        options.add("A");
        options.add("B");
        regionsToOptions.put(region4, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep8.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep8.FILENAME, programFiles, regionsToOptions);
        System.out.println(performanceModel);

        // Compare
        double performance = 1.5;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 2.1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel9() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.MAIN_METHOD, 31, 36);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.MAIN_METHOD, 41, 46);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.METHOD_1, 19, 20);
        Regions.addRegion(region3);

        JavaRegion region4 = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.METHOD_1, 25, 26);
        Regions.addRegion(region4);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        options.add("B");
        regionsToOptions.put(region3, options);

        options = new HashSet<>();
        options.add("A");
        options.add("B");
        regionsToOptions.put(region4, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep9.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep9.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);

        // Compare
        double performance = 2.1;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.9;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel10() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.MAIN_METHOD, 31, 36);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.MAIN_METHOD, 45, 50);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.METHOD_1, 19, 20);
        Regions.addRegion(region3);

        JavaRegion region4 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.METHOD_2, 19, 20);
        Regions.addRegion(region4);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        options.add("B");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region3, options);

        options = new HashSet<>();
        options.add("A");
        options.add("B");
        regionsToOptions.put(region4, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep10.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep10.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);

        // Compare
        double performance = 0.3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 2.1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }

    @Test
    public void testBuildPerformanceModel11() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException {
        // TODO we still need to get Lotrack working
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep11.PACKAGE, Sleep11.CLASS, Sleep11.MAIN_METHOD, 19, 24);
        Regions.addRegion(region1);

        JavaRegion region2 = new JavaRegion(Sleep11.PACKAGE, Sleep11.CLASS, Sleep11.MAIN_METHOD, 30, 35);
        Regions.addRegion(region2);

        JavaRegion region3 = new JavaRegion(Sleep11.PACKAGE, Sleep11.CLASS, Sleep11.MAIN_METHOD, 39, 57);
        Regions.addRegion(region3);

        // Regions to options
        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region2, options);

        options = new HashSet<>();
        options.add("A");
        regionsToOptions.put(region3, options);
        // TODO we still need to get Lotrack working

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep11.FILENAME);

        // Performance model
        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep11.FILENAME, programFiles, regionsToOptions);
        System.out.println(performanceModel);

        // Compare
        double performance = 2.2;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);

        performance = 1.1;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), JavaPipelineTest.TIMING_ERROR);
    }
}
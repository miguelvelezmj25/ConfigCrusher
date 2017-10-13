package edu.cmu.cs.mvelezce.tool.compression.simple;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.BaseCompressionTest;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;


/**
 * Created by mvelezce on 4/28/17.
 */
public class SimpleCompressionTest {

    public static void checktOptionsPermuatationsToGetConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        Collection<List<Set<String>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<String>> permutation : permutations) {
//            System.out.println("\nPermutation: " + permutation);
            Compression compression = new SimpleCompression(relevantOptionsSet);
            Set<Set<String>> results = compression.compressConfigurations();
            SimpleCompressionTest.checkConfigurationIsStatisfied(new HashSet<>(permutation), results);
        }
    }

    public static void checkConfigurationIsStatisfied(Set<Set<String>> relevantOptionsSet, Set<Set<String>> results) {
//            System.out.println(results);
        for(Set<String> relevantOptions : relevantOptionsSet) {
            Set<Set<String>> powerSet = Helper.getConfigurations(relevantOptions);

            for(Set<String> configuration : powerSet) {
//                    System.out.println("Want configuration: " + configuration + " from: " + relevantOptionsConvenient);
                boolean hasConfiguration = false;

                for(Set<String> result : results) {
                    if(SimpleCompressionTest.matches(result, configuration, relevantOptions)) {
                        hasConfiguration = true;
                        break;
                    }
                }

                Assert.assertTrue(hasConfiguration);
            }
        }
    }

    public static boolean matches(Set<String> result, Set<String> configuration, Set<String> relevantOptions) {
        Set<String> valueOfResultInRelevantOption = new HashSet<>(relevantOptions);
        valueOfResultInRelevantOption.retainAll(result);
        return valueOfResultInRelevantOption.equals(configuration);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline1() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, AC, AD, BE");

        // Program
        String programName = "test1";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline2() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("ABC, BCD");

        // Program
        String programName = "test2";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline3() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, BCD");

        // Program
        String programName = "test3";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline4() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, BC");

        // Program
        String programName = "test4";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline5() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, CDE");

        // Program
        String programName = "test5";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline6() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, AC, BC");

        // Program
        String programName = "test6";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline7() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, AC, AD, BC, BD");

        // Program
        String programName = "test7";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline8() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, AC, AD, BC, CD");

        // Program
        String programName = "test8";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline9() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("ABC, CD, BD");

        // Program
        String programName = "test9";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline10() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("ABC, CD");

        // Program
        String programName = "test10";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline11() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("ABC, DEF");

        // Program
        String programName = "test11";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline12() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("");

        // Program
        String programName = "test12";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline13() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("ABCD, ADXY, ABDX");

        // Program
        String programName = "test13";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testGetConfigurationsToExecutePipeline14() throws IOException {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Options
        Set<Set<String>> relevantOptionsSet = BaseCompressionTest.getOptionsSet("AB, AC, AD, BC, CD, BD");

        // Program
        String programName = "test14";

        Compression compressor = new SimpleCompression(programName, relevantOptionsSet);
        Set<Set<String>> outputSave = compressor.compressConfigurations(args);

        args = new String[0];

        Set<Set<String>> outputRead = compressor.compressConfigurations(args);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testPngtastic() throws IOException {
//        String programName = "pngtastic";
//
//        // Program arguments
//        String[] args = new String[0];
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
////        args = new String[1];
////        args[0] = "-saveres";
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testSleep28() throws IOException {
//        String programName = "sleep28";
//
//        // Program arguments
//        String[] args = new String[0];
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
////        args = new String[1];
////        args[0] = "-saveres";
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testSleep30() throws IOException {
//        String programName = "sleep30";
//
//        // Program arguments
//        String[] args = new String[0];
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
////        args = new String[1];
////        args[0] = "-saveres";
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testElevator() throws IOException {
//        String programName = "elevator";
//
//        // Program arguments
//        String[] args = new String[0];
//
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
////        args = new String[1];
////        args[0] = "-saveres";
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testSleep3() throws IOException {
//        String programName = "sleep3";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testSleep10() throws IOException {
//        String programName = "sleep10";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testSleep14() throws IOException {
//        String programName = "sleep14";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testSleep15() throws IOException {
//        String programName = "sleep15";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void testGPL() throws IOException {
//        String programName = "gpl";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = ProgramAnalysis.analyze(programName, args);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args, options);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void runningExample() throws IOException {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());

        Compression compressor = new SimpleCompression(programName, options);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void runningExample1() throws IOException {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[0];

        Compression compressor = new SimpleCompression(programName);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void Sleep1() throws IOException {
//        String programName = "sleep1";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());
//
//        Compression compressor = new SimpleCompression(programName, options);
//        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
//        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void colorCounter() throws IOException {
        String programName = "pngtasticColorCounter";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());

        Compression compressor = new SimpleCompression(programName, options);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void optimizer() throws IOException {
        String programName = "pngtasticOptimizer";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());

        Compression compressor = new SimpleCompression(programName, options);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void prevayler() throws IOException {
        String programName = "prevayler";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());

        Compression compressor = new SimpleCompression(programName, options);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void regions13() throws IOException {
        String programName = "regions13";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());

        Compression compressor = new SimpleCompression(programName, options);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

    @Test
    public void regions14() throws IOException {
        String programName = "regions14";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis taintflowAnalysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptionsSet = taintflowAnalysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> options = SimpleCompression.expandOptions(decisionsToOptionsSet.values());

        Compression compressor = new SimpleCompression(programName, options);
        Set<Set<String>> configurationsToExecute = compressor.compressConfigurations(args);
        System.out.println(configurationsToExecute.size());
    }

}
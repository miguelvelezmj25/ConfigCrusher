package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.*;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.compression.SimpleTest;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.zip.ZipMain;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class ExecutorTest {

    public static void checkExecutionTimes(Set<PerformanceEntry> expectedPerformances, Set<PerformanceEntry> actualPerformances) {
        for(PerformanceEntry expected : expectedPerformances) {
            for(PerformanceEntry actual : actualPerformances) {
                if(expected.getConfiguration().equals(actual.getConfiguration())) {
                    boolean oneIsEqual = false;

                    for(Map.Entry<Region, Long> actualResultEntry : actual.getRegionsToExecutionTime().entrySet()) {
                        for(Map.Entry<Region, Long> expectedResultEntry : expected.getRegionsToExecutionTime().entrySet()) {
                            if(actualResultEntry.getKey().getRegionID().equals(expectedResultEntry.getKey().getRegionID())) {
                                if(expectedResultEntry.getValue().equals(actualResultEntry.getValue())) {
                                    oneIsEqual = true;
                                }
                            }
                        }
                    }

                    Assert.assertTrue(oneIsEqual);
                }
            }

            System.out.println();
        }
    }

    @Test
    public void testGetOptions() throws Exception {
        String progName = "elevator";
        Set<String> options = Executor.getOptions(progName);

        Assert.assertEquals(6, options.size());
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline1() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline2() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline3() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline4() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline7() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline8() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline9() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline10() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testMeasureConfigurationPerformancePipeline13() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Execute
        Set<PerformanceEntry> outputSave = Executor.measureConfigurationPerformance(Sleep13.CLASS, args, Sleep13.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        args = new String[0];
        Set<PerformanceEntry> outputRead = Executor.measureConfigurationPerformance(Sleep13.CLASS, args, Sleep13.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);

        ExecutorTest.checkExecutionTimes(outputSave, outputRead);
    }

    @Test
    public void testZipmeSimple() throws IOException, ParseException {
        String programName = "zipme-simple";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/zipme/out/production/zipme/";
        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";

        String[] args = new String[0];

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);

        String[] a = new String[0];
        ZipMain.main(a);
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//

//        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testElevator() throws IOException, ParseException {
        String programName = "elevator";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";

        String[] args = new String[0];

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

//        args = new String[1];
//        args[0] = "-i5";

        Set<PerformanceEntry> measured = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testSleep30() throws IOException, ParseException {
        String programName = "sleep30";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep30";

        String[] args = new String[0];

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);

//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i1";

        args = new String[1];
        args[0] = "-i1";

        Set<PerformanceEntry> measured = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testSleep31() throws IOException, ParseException {
        String programName = "sleep31";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep30";

        String[] args = new String[0];

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);

//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i1";

        args = new String[1];
        args[0] = "-i1";

        Set<PerformanceEntry> measured = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testZipme() throws IOException, ParseException {
        String programName = "zipme";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";

        Set<String> options = Executor.getOptions(programName);
        Set<Set<String>> configurations = Helper.getConfigurations(options);

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i1";

//        args = new String[1];
//        args[0] = "-i5";

        Set<PerformanceEntry> measured = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testGPL() throws IOException, ParseException {
        String programName = "gpl";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/gpl/out/production/gpl/";
        String entryPoint = "edu.cmu.cs.mvelezce.Main";

        String[] args = new String[0];

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        configurations = Helper.getConfigurations(options);
        options = new HashSet<>();
        configurations.clear();
        configurations.add(options);

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testSleep1() throws IOException, ParseException {
        String programName = "sleep1";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";

        String[] args = new String[0];

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<String> options = new HashSet<>();
        options.add("A");

        Set<Set<String>> configurations = Helper.getConfigurations(options);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
    }

    @Test
    public void testSleep2() throws IOException, ParseException {
        String programName = "Sleep2";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep2";

        String[] args = new String[0];

        Set<String> options = new HashSet<>();
        options.add("A");

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Set<Set<String>> configurations = Helper.getConfigurations(options);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);

    }

    @Test
    public void testPngtastic() throws IOException, ParseException {
        String programName = "pngtastic";

        String[] args = new String[1];
        args[0] = "-i1";

        Set<PerformanceEntry> measuredPerformance = Executor.repeatMeasureConfigurationPerformance(programName, args);
    }

}
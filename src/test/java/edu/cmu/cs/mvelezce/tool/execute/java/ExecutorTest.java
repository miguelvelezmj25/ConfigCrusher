package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.java.programs.*;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.compression.SimpleTest;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.junit.Assert;
import org.junit.Test;

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

}
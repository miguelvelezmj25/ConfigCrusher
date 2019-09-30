package edu.cmu.cs.mvelezce.tool.execute.java;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class BaseExecutorTest {

//    public static void checkExecutionTimes(Set<PerformanceEntry> expectedPerformances, Set<PerformanceEntry> actualPerformances) {
//        for(PerformanceEntry expected : expectedPerformances) {
//            for(PerformanceEntry actual : actualPerformances) {
//                if(expected.getConfiguration().equals(actual.getConfiguration())) {
//                    boolean oneIsEqual = false;
//
//                    for(Map.Entry<Region, Long> actualResultEntry : actual.getRegionsToExecutionTime().entrySet()) {
//                        for(Map.Entry<Region, Long> expectedResultEntry : expected.getRegionsToExecutionTime().entrySet()) {
//                            if(actualResultEntry.getKey().getRegionID().equals(expectedResultEntry.getKey().getRegionID())) {
//                                if(expectedResultEntry.getValue().equals(actualResultEntry.getValue())) {
//                                    oneIsEqual = true;
//                                }
//                            }
//                        }
//                    }
//
//                    Assert.assertTrue(oneIsEqual);
//                }
//            }
//
//            System.out.println();
//        }
//    }

//    @Test
//    public void testGetOptions() throws Exception {
//        String progName = "elevator";
//        Set<String> options = BaseExecutor.getOptions(progName);
//
//        Assert.assertEquals(6, options.size());
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline1() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline2() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline3() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("AB");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline4() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline7() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline8() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline9() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline10() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("AB");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformancePipeline13() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleCompressionTest.getOptionsSet("A");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Execute
//        Set<PerformanceEntry> outputSave = BaseExecutor.measureConfigurationPerformance(Sleep13.CLASS, args, Sleep13.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        args = new String[0];
//        Set<PerformanceEntry> outputRead = BaseExecutor.measureConfigurationPerformance(Sleep13.CLASS, args, Sleep13.FILENAME, ConfigCrusherTimerRegionInstrumenter.TARGET_DIRECTORY + "/" + SleepAdapter.TEST_DIRECTORY, configurationsToExecute);
//
//        BaseExecutorTest.checkExecutionTimes(outputSave, outputRead);
//    }
//
////    @Test
////    public void testZipmeSimple() throws IOException, ParseException {
////        String programName = "zipme-simple";
////        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/zipme/out/production/zipme/";
////        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";
////
////        String[] args = new String[0];
////
////        Compression compression = new SimpleCompression(programName);
////        Set<Set<String>> configurations = compression.compressConfigurations(args);
////
////        String[] a = new String[0];
////        ZipMain.main(a);
//////        args = new String[2];
//////        args[0] = "-delres";
//////        args[1] = "-saveres";
//////
////
//////        Set<PerformanceEntry> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
////    }
//
//    @Test
//    public void testElevator() throws IOException, ParseException {
//        String programName = "elevator";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
//        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";
//
//        String[] args = new String[0];
//
//        Compression compression = new SimpleCompression(programName);
//        Set<Set<String>> configurations = compression.compressConfigurations(args);
//
//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i5";
//
////        args = new String[1];
////        args[0] = "-i5";
//
//        Set<PerformanceEntry> measured = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//    }
//
//    @Test
//    public void testSleep30() throws IOException, ParseException {
//        String programName = "sleep30";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep30";
//
//        String[] args = new String[0];
//
//        Compression compression = new SimpleCompression(programName);
//        Set<Set<String>> configurations = compression.compressConfigurations(args);
//
////        args = new String[3];
////        args[0] = "-delres";
////        args[1] = "-saveres";
////        args[2] = "-i1";
//
//        args = new String[1];
//        args[0] = "-i1";
//
//        Set<PerformanceEntry> measured = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//    }
//
//    @Test
//    public void testSleep31() throws IOException, ParseException {
//        String programName = "sleep31";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep30";
//
//        String[] args = new String[0];
//
//        Compression compression = new SimpleCompression(programName);
//        Set<Set<String>> configurations = compression.compressConfigurations(args);
//
////        args = new String[3];
////        args[0] = "-delres";
////        args[1] = "-saveres";
////        args[2] = "-i1";
//
//        args = new String[1];
//        args[0] = "-i1";
//
//        Set<PerformanceEntry> measured = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//    }
//
//    @Test
//    public void testZipme() throws IOException, ParseException {
//        String programName = "zipme";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
//        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";
//
//        Set<String> options = BaseExecutor.getOptions(programName);
//        Set<Set<String>> configurations = Helper.getConfigurations(options);
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
////        args = new String[3];
////        args[0] = "-delres";
////        args[1] = "-saveres";
////        args[2] = "-i1";
//
////        args = new String[1];
////        args[0] = "-i5";
//
//        Set<PerformanceEntry> measured = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//    }
//
//    @Test
//    public void testGPL() throws IOException, ParseException {
//        String programName = "gpl";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/gpl/out/production/gpl/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Main";
//
//        String[] args = new String[0];
//
//        Compression compression = new SimpleCompression(programName);
//        Set<Set<String>> configurations = compression.compressConfigurations(args);
//        Set<String> options = new HashSet<>();
//
//        for(Set<String> configuration : configurations) {
//            options.addAll(configuration);
//        }
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        configurations = Helper.getConfigurations(options);
//        options = new HashSet<>();
//        configurations.clear();
//        configurations.add(options);
//
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//    }
//
//    @Test
//    public void testSleep1() throws IOException, ParseException {
//        String programName = "sleep1";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";
//
//        String[] args = new String[0];
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//
//        Set<Set<String>> configurations = Helper.getConfigurations(options);
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//    }
//
//    @Test
//    public void testSleep2() throws IOException, ParseException {
//        String programName = "Sleep2";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep2";
//
//        String[] args = new String[0];
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Set<Set<String>> configurations = Helper.getConfigurations(options);
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurations);
//
//    }
//
//    @Test
//    public void testPngtastic() throws IOException, ParseException {
//        String programName = "pngtastic";
//
//        String[] args = new String[1];
//        args[0] = "-i1";
//
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.repeatMeasureConfigurationPerformance(programName, args);
//    }

}
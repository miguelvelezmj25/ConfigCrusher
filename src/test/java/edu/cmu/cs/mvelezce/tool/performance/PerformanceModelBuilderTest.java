package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.*;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.ProgramAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.SimpleTest;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public class PerformanceModelBuilderTest {

    @Test
    public void testGPL() throws IOException, ParseException {
        String program = "gpl";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testElevator() throws IOException, ParseException {
        String program = "elevator";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testElevatorM() throws IOException, ParseException {
        String program = "elevator-m";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testSleep2() throws IOException, ParseException {
        String program = "Sleep2";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = new HashMap<>();//ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testSleep3() throws IOException, ParseException {
        String program = "Sleep3";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(program, args, measuredPerformance, regionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testSleep10() throws IOException, ParseException {
        String program = "Sleep10";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
    }

    @Test
    public void testSleep15() throws IOException, ParseException {
        String program = "Sleep15";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
    }

    @Test
    public void testSleep17() throws IOException, ParseException {
        String program = "Sleep17";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
    }

    @Test
    public void testSleep18() throws IOException, ParseException {
        String program = "Sleep18";

        // Program arguments
        String[] args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(program, args);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(program, args);

        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testCreatePerformanceModelPipeline1() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("d9e007bd-0c4a-43b9-ac70-4404378b02ee");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep1.CLASS, configurationsToExecute);

        // Program arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Execute
        PerformanceModel outputSave = PerformanceModelBuilder.createPerformanceModel(Sleep1.CLASS, args, measuredPerformance, regionsToOptions);

        args = new String[0];
        PerformanceModel outputRead = PerformanceModelBuilder.createPerformanceModel(Sleep1.CLASS, args, measuredPerformance, regionsToOptions);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testCreatePerformanceModel1() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("d9e007bd-0c4a-43b9-ac70-4404378b02ee");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep1.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 0.9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel2() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("ae664378-11a7-498f-bb1b-babc0a6dc4d6");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("a8aaaaf2-bfb3-49ab-b5c9-8ceebae16124");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep2.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.75;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 2.250;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel3() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("515dad72-acd8-4078-afd1-243d8a0a8159");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        region = new Region("e175b797-442f-4af3-ae7b-df17f690cbb5");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("49ccd1a0-4f72-4704-afed-da8c8fc07628");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        region = new Region("35d3ea7b-b151-406b-8c40-8402f2aaf2d7");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep3.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 2.0;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 3.5;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel4() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("869447bc-1159-43aa-8cd6-76a941928dec");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep4.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.9;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 0.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel7() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("6b5f3599-b7be-48dd-a1e2-403b66e065c2");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("95df7be1-6bd8-4675-bbfd-1c0bdcc170d8");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep7.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 1.2;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 0.7;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel8() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("62a843e8-fda7-4296-9cb0-06f2ab7e922a");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("55be5222-878e-480e-9b4c-19ee048e7d68");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep8.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 1.2;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 0.7;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel9() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("23cc14a5-0f8b-4647-9a9d-31124de65660");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("093455fe-df64-4f1f-9bcf-2c879c97ae1c");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep9.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 1.2;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 0.7;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel10() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("8991b0ff-a0a9-4967-8acd-ba823cf700f4");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        relevantOptions.add("B");
        region = new Region("44f7b4e3-3af8-46c1-b9d0-1a93736950c7");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("96c809ce-733d-4647-8d10-d1d587144c8b");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        relevantOptions.add("B");
        region = new Region("e80dd113-203e-477b-91bc-0c4889da64d2");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep10.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 2.1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

    @Test
    public void testCreatePerformanceModel13() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("37486f0a-e662-4a18-a7e7-a88ca76d9d2a");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep13.CLASS, args, Sleep13.FILENAME, Instrumenter.TARGET_DIRECTORY + "/" + Sleep13.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);

        performance = 0.9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.05);
    }

}
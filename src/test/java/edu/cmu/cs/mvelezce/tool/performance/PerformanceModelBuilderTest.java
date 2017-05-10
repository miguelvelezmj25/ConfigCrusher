package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.java.programs.*;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
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
    public void testCreatePerformanceModelPipeline1() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("ecb89258-9de1-416f-955c-194d09e9b249");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep1.CLASS, configurationsToExecute);

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
        Region region = new Region("686ed958-c03c-4a6a-90f3-83575b3d3312");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep1.CLASS, configurationsToExecute);

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
        Region region = new Region("73e49277-d0ba-4411-88da-f2f5857c279c");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("c545f85d-2722-4bfa-9d6a-dcf944dfac05");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep2.CLASS, configurationsToExecute);

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
        Region region = new Region("4cbc76d0-22a9-43f3-a01c-2ce902d3af04");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        region = new Region("88386ec4-6fec-471a-b676-97acc696830c");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("a91ca8dd-45b4-41eb-99a6-ee1774239a40");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        region = new Region("66e75054-9a1c-4ea4-a478-c205406a8a0a");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep3.CLASS, configurationsToExecute);

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
        Region region = new Region("f9ebeca2-376c-4c6e-b7c1-83aa7c795f51");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep4.CLASS, configurationsToExecute);

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
        Region region = new Region("a8b6f1d1-18be-4c50-81e2-3d56b91e4a68");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("eab6e1e5-edfe-46e6-814e-deed59390795");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep7.CLASS, configurationsToExecute);

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
        Region region = new Region("4567f7fa-84ce-4e38-ab82-57084191a32f");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("4edbec5f-0af6-4d7c-88ab-81991c601973");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep8.CLASS, configurationsToExecute);

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
        Region region = new Region("37bdb3f1-b973-4580-8db5-5478fdb86099");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("0154d129-081e-4359-925e-cc8d054589f5");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep9.CLASS, configurationsToExecute);

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
        Region region = new Region("478d1824-b386-42da-b24b-21cb8c79809b");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        relevantOptions.add("B");
        region = new Region("8694bd89-4a53-48b1-97a1-21128e6d8604");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("b5997cc5-faf0-49b6-99b3-c2f51227c909");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        relevantOptions.add("B");
        region = new Region("cf3aff4e-0fda-458c-ba6d-77beae6d5c0f");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep10.CLASS, configurationsToExecute);

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
        Region region = new Region("0fd507ca-aab9-46c4-8cdc-4bcf86f75043");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(Sleep13.CLASS, args, Sleep13.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep13.CLASS, configurationsToExecute);

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
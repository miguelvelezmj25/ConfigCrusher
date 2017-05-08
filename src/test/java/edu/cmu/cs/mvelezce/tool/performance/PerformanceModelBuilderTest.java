package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.java.programs.Sleep3;
import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.java.programs.Sleep7;
import edu.cmu.cs.mvelezce.java.programs.Sleep8;
import edu.cmu.cs.mvelezce.java.programs.Sleep9;
import edu.cmu.cs.mvelezce.java.programs.Sleep10;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.compression.SimpleTest;
import edu.cmu.cs.mvelezce.tool.execute.java.Executer;
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

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep1.CLASS, configurationsToExecute);

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
        Region region = new Region("ecb89258-9de1-416f-955c-194d09e9b249");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep1.CLASS, args, Sleep1.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep1.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 0.9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel2() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("ed9d27ad-9bbc-4532-9993-da786d3a8479");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("8ad7c2ec-0937-4b02-a774-71a3f118c308");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep2.CLASS, args, Sleep2.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep2.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.75;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 2.250;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel3() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("f3f4660a-6e7c-4f9a-be57-315f058bd17a");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        region = new Region("5d2b13b0-b9ff-4b69-b89d-dda9ed6c81a4");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("a8110578-2ed0-457f-838d-b4ab307a965e");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        region = new Region("7fdcdba2-cd2f-433e-82e7-4ddea2b808be");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep3.CLASS, args, Sleep3.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep3.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 2.0;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 3.5;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel4() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("d0a2da4a-2ab2-4bf4-b968-e7fd2e0852e3");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep4.CLASS, args, Sleep4.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep4.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.9;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 0.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel7() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("9e4ba19e-6a13-4749-8b63-a860d4f522d7");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("ef83d302-da38-4588-a5c9-7f15bdff48b5");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep7.CLASS, args, Sleep7.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep7.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 1.2;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 0.7;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel8() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("0c5a6d93-a36b-44fe-8d15-8c5a23ece2d0");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("1fab409e-3c1d-4f0b-94b5-a1aee97174cb");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep8.CLASS, args, Sleep8.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep8.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 1.2;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 0.7;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel9() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("59b21e7c-4334-4b3a-a54a-cda881a44a69");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("12aff165-3e3f-4407-a062-63711dca41c3");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("A");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep9.CLASS, args, Sleep9.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep9.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 1.2;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 0.7;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

    @Test
    public void testCreatePerformanceModel10() throws IOException, ParseException {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region = new Region("f72309ee-4cde-4ced-b7a7-57a7d0295938");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        relevantOptions.add("B");
        region = new Region("8e260388-de0c-4a6c-a5f9-c81cecd5776a");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        region = new Region("e0524ecf-548a-44b0-a619-662c994d0fab");
        regionsToOptions.put(region, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        relevantOptions.add("B");
        region = new Region("0e240af1-2818-437c-8d9c-ffce8ae82fb6");
        regionsToOptions.put(region, relevantOptions);

        // Program arguments
        String[] args = new String[0];

        // Configurations
        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(Sleep10.CLASS, args, Sleep10.FILENAME, Instrumenter.DIRECTORY + "/" + Sleep10.CLASS, configurationsToExecute);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        System.out.println(performanceModel);

        double performance = 0.3;
        HashSet<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 2.1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);

        performance = 1.8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0.1);
    }

}
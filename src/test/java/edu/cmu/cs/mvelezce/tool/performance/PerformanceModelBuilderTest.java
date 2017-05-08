package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
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

}
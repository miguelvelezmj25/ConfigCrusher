package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class PerformanceModelBuilderTest {

    @Test
    public void testCreatePerformanceModelPipeline1() throws IOException {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Map<Region, Set<String>> regionsToOptions
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//        Set<String> relevantOptions = new HashSet<>();
//        relevantOptions.add("A");
//        int duration1 = 3;
//        Statement timedStatement1 = new SleepStatement(new ConstantIntExpression(duration1));
//        Region region1 = new SleepRegion(timedStatement1);
//        Regions.addRegion(region1);
//        regionsToOptions.put(region1, relevantOptions);
//
//        relevantOptions = new HashSet<>();
//        relevantOptions.add("B");
//        int duration2 = 1;
//        Statement timedStatement2 = new SleepStatement(new ConstantIntExpression(duration2));
//        Region region2 = new SleepRegion(timedStatement2);
//        Regions.addRegion(region2);
//        regionsToOptions.put(region2, relevantOptions);
//
//        // Set<PerformanceEntry> measuredPerformance
//        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
//        Set<String> configuration = new HashSet<>();
//        Set<Region> regions = new HashSet<>();
//        region1.startTime(0);
//        region1.endTime(0);
//        region2.startTime(0);
//        region2.endTime(0);
//        regions.add(region1);
//        regions.add(region2);
//        int programDuration = 2;
//        Region program = new SleepRegion(new SleepStatement(new ConstantIntExpression(programDuration)));
//        Regions.addProgram(program);
//        Regions.addPossibleInnerRegion(program, region1);
//        Regions.addPossibleInnerRegion(program, region2);
//        program.startTime(0);
//        program.endTime(programDuration);
//        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, regions, program);
//        measuredPerformance.add(performanceEntry);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        regions = new HashSet<>();
//        region1.startTime(0);
//        region1.endTime(duration1);
//        region2.startTime(0);
//        region2.endTime(duration2);
//        regions.add(region1);
//        regions.add(region2);
//        programDuration += duration1 + duration2;
//        program = new SleepRegion(new SleepStatement(new ConstantIntExpression(programDuration)));
//        program.startTime(0);
//        program.endTime(programDuration);
//        performanceEntry = new PerformanceEntry(configuration, regions, program);
//        measuredPerformance.add(performanceEntry);
//
//        // Program
//        String programName = "test1";
//        PerformanceModel outputSave = PerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
//
//        args = new String[0];
//        PerformanceModel outputRead = PerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
//
//        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testCreatePerformanceModel1() {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        Region region1 = new Region("1");
        regionsToOptions.put(region1, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        Region region2 = new Region("2");
        regionsToOptions.put(region2, relevantOptions);

        // Set<PerformanceEntry> measuredPerformance
        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        List<Region> regions = new LinkedList<>();
        long programDuration = Region.toNanoTime(2);
        Region program = new Region("program", 0, programDuration);
        regions.add(program);
        regions.add(program);
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, regions);
        measuredPerformance.add(performanceEntry);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regions = new LinkedList<>();
        long duration1 = Region.toNanoTime(3);
        long duration2 = Region.toNanoTime(1);
        programDuration += duration1 + duration2;
        program = new Region("program", 0, programDuration);
        region1 = new Region(region1.getRegionID(), 0, duration1);
        region2 = new Region(region2.getRegionID(), 0, duration2);
        regions.add(program);
        regions.add(region1);
        regions.add(region1);
        regions.add(region2);
        regions.add(region2);
        regions.add(program);
        performanceEntry = new PerformanceEntry(configuration, regions);
        measuredPerformance.add(performanceEntry);

        // Performance model
        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        int performance = 2;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

}
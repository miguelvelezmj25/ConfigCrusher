package edu.cmu.cs.mvelezce.tool.pipeline;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantIntExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepRegion;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 4/14/17.
 */
public class PipelineTest {

    public static Set<Set<String>> getOptionsSet(String string) {
        Set<Set<String>> result = new HashSet<>();
        String[] allOptions = string.split(",");

        for(String options : allOptions) {
            Set<String> newOption = new HashSet<>();
            options = options.trim();

            for(int i = 0; i < options.length(); i++) {
                newOption.add(options.charAt(i) + "");
            }

            result.add(newOption);
        }

        return result;
    }

    public static void checktOptionsPermuatationsToGetConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        Collection<List<Set<String>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<String>> permutation : permutations) {
//            System.out.println("\nPermutation: " + permutation);
            Set<Set<String>> results = edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.getConfigurationsToExecute(relevantOptionsSet);
            PipelineTest.checkConfigurationIsStatisfied(new HashSet<>(permutation), results);
        }
    }

    public static void checkConfigurationIsStatisfied(Set<Set<String>> relevantOptionsSet, Set<Set<String>> results) {
//            System.out.println(results);
        for(Set<String> relevantOptions : relevantOptionsSet) {
            Set<Set<String>> powerSet = Helper.getConfigurations(relevantOptions);

            for (Set<String> configuration : powerSet) {
//                    System.out.println("Want configuration: " + configuration + " from: " + relevantOptionsConvenient);
                boolean hasConfiguration = false;

                for (Set<String> result : results) {
                    if (PipelineTest.matches(result, configuration, relevantOptions)) {
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
    public void testGetConfigurationsToExecute1() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BE");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute2() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, BCD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute3() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, BCD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute4() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, BC");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute5() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, CDE");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute6() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, BC");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute7() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BC, BD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute8() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BC, CD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test // TODO ERROR because I need to get the other 4 configs from ABC
    public void testGetConfigurationsToExecute9() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, CD, BD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute10() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, CD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute11() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABC, DEF");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute12() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute13() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("ABCD, ADXY, ABDX");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute14() {
        Set<Set<String>> relevantOptionsSet = PipelineTest.getOptionsSet("AB, AC, AD, BC, CD, BD");
        PipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void filterOptions1() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, AC");
        Assert.assertEquals(set, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions2() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("ABC, ACD");
        Assert.assertEquals(set, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions3() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, ABC");
        Set<Set<String>> result = PipelineTest.getOptionsSet("ABC");
        Assert.assertEquals(result, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions4() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<String>> result = PipelineTest.getOptionsSet("ABC, BCD, DEF");
        Assert.assertEquals(result, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void testCreatePerformanceModel1() {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        int duration1 = 3;
        Statement timedStatement1 = new SleepStatement(new ConstantIntExpression(duration1));
        Region region1 = new SleepRegion(timedStatement1);
        regionsToOptions.put(region1, relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        int duration2 = 1;
        Statement timedStatement2 = new SleepStatement(new ConstantIntExpression(duration2));
        Region region2 = new SleepRegion(timedStatement2);
        regionsToOptions.put(region2, relevantOptions);

        // Set<PerformanceEntry> measuredPerformance
        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        Set<Region> regions = new HashSet<>();
        region1.startTime(0);
        region1.endTime(0);
        region2.startTime(0);
        region2.endTime(0);
        regions.add(region1);
        regions.add(region2);
        int programDuration = 2;
        Region program = new SleepRegion(new SleepStatement(new ConstantIntExpression(programDuration)));
        program.startTime(0);
        program.endTime(programDuration);
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, regions, program);
        measuredPerformance.add(performanceEntry);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regions = new HashSet<>();
        region1.startTime(0);
        region1.endTime(duration1);
        region2.startTime(0);
        region2.endTime(duration2);
        regions.add(region1);
        regions.add(region2);
        programDuration += duration1 + duration2;
        program = new SleepRegion(new SleepStatement(new ConstantIntExpression(programDuration)));
        program.startTime(0);
        program.endTime(programDuration);
        performanceEntry = new PerformanceEntry(configuration, regions, program);
        measuredPerformance.add(performanceEntry);

        // Performance model
        PerformanceModel performanceModel = Pipeline.createPerformanceModel(measuredPerformance, regionsToOptions);

        int performance = 2;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

}
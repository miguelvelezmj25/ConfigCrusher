package edu.cmu.cs.mvelezce.analysis.pipeline;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.analysis.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.analysis.pipeline.sleep.SleepPipeline;
import edu.cmu.cs.mvelezce.analysis.pipeline.sleep.SleepRegion;
import edu.cmu.cs.mvelezce.analysis.taint.Processor;
import edu.cmu.cs.mvelezce.analysis.taint.Region;
import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.StatementSleep;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
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

    public static void compareRegionOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
        // program, regions, options, BF configurations, constraints, compressed configurations, compressed over BF
        if(csv) {
            System.out.print(program + ", ");
        }
        else {
            System.out.println(program);
        }

        Map<Region, Set<String>> queryResult = Processor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
        if(csv) {
            System.out.print(queryResult.size() + ", ");
        }
        else {
            System.out.println("Lotrack total number of regions: " + queryResult.size());
        }
        queryResult = Processor.filterBooleans(queryResult);
        queryResult = Processor.filterRegionsNoOptions(queryResult);

        Set<Set<String>> optionsSet = new HashSet<>(queryResult.values());
        Set<String> uniqueOptions = new HashSet<>();

        for(Set<String> options : optionsSet) {
            uniqueOptions.addAll(options);
        }

        PipelineTest.compare(optionsSet, uniqueOptions, csv);
    }

    public static void compareOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
        // program, entries, options, BF configurations, constraints, compressed configurations, compressed over BF
        if(csv) {
            System.out.print(program + ", ");
        }
        else {
            System.out.println(program);
        }

        List<String> fields = new ArrayList<>();
        fields.add(Processor.USED_TERMS);
        fields.add(Processor.CONSTRAINT);

        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
        List<String> queryResult = ScalaMongoDriverConnector.query(program, fields);
        ScalaMongoDriverConnector.close();

        if(csv) {
            System.out.print(queryResult.size() + ", ");
        }
        else {
            System.out.println("Lotrack total number of entries: " + queryResult.size());
        }

        Set<Set<String>> optionsSet = new HashSet<>();

        for(String result : queryResult) {
            JSONObject JSONResult = new JSONObject(result);
            Set<String> options = new HashSet<>();

            if(JSONResult.has(Processor.USED_TERMS)) {
                for(Object string : JSONResult.getJSONArray(Processor.USED_TERMS).toList()) {
                    String possibleString = string.toString();

                    if(possibleString.equals("true") || possibleString.equals("false")) {
                        continue;
                    }

                    options.add(string.toString());
                }
            }
            else if(JSONResult.has(Processor.CONSTRAINT)) {
                // Be careful that this is imprecise since the constraints can be very large and does not fit in the db field
                String[] constraints = JSONResult.getString(Processor.CONSTRAINT).split(" ");

                for(String constraint : constraints) {
                    constraint = constraint.replaceAll("[()^|!=]", "");
                    if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
                        continue;
                    }

                    if(constraint.contains(Processor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
                        constraint = constraint.split(Processor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
                    }

                    // Because the constraint gotten from Lotrack might be too long
                    if(constraint.contains(".")) {
                        continue;
                    }

                    if(constraint.equals("false") || constraint.equals("true")) {
                        continue;
                    }

                    options.add(constraint);
                }
            }
            else {
                throw new NoSuchFieldException("The query result does not have neither a " + Processor.USED_TERMS + " or " + Processor.CONSTRAINT + " fields");
            }

            if(!options.isEmpty()) {
                optionsSet.add(options);
            }

        }

        Set<String> uniqueOptions = new HashSet<>();

        for(Set<String> options : optionsSet) {
            uniqueOptions.addAll(options);
        }

        PipelineTest.compare(optionsSet, uniqueOptions, csv);
    }

    public static void compare(Set<Set<String>> optionsSet, Set<String> uniqueOptions, boolean csv) {
        if(csv) {
            System.out.print(uniqueOptions.size() + ", ");
            System.out.print((int) Math.pow(2, uniqueOptions.size()) + ", ");
            System.out.print(optionsSet.size() + ", ");
        }
        else {
            System.out.println("Number of options: " + uniqueOptions.size());
            System.out.println("Brute force number of configurations: " + (int) Math.pow(2, uniqueOptions.size()));
            System.out.println("Number of constraints: " + optionsSet.size());
        }

        Set<Set<String>> configurations = Pipeline.getConfigurationsToExecute(optionsSet);
        PipelineTest.checkConfigurationIsStatisfied(optionsSet, configurations);

        if(csv) {
            System.out.print(configurations.size() + ", ");
            System.out.println(configurations.size()/Math.pow(2, uniqueOptions.size()));
        }
        else {
            System.out.println("Compressed number of configurations: " + configurations.size());
            System.out.println("Compressed over BF: " + configurations.size()/Math.pow(2, uniqueOptions.size()));
        }
    }

    public static void checktOptionsPermuatationsToGetConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        Collection<List<Set<String>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<String>> permutation : permutations) {
//            System.out.println("\nPermutation: " + permutation);
            Set<Set<String>> results = Pipeline.getConfigurationsToExecute(relevantOptionsSet);
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
    public void testCompareRegionOptionsCompressionToBF1() throws NoSuchFieldException {
        PipelineTest.compareRegionOptionsCompressionToBF("ZipMeChangedSimplified", false);
    }

    @Test
    public void testCompareRegionOptionsCompressionToBFAll1() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                PipelineTest.compareRegionOptionsCompressionToBF(collection, false);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//            System.out.println();
//        }
    }

    @Test
    public void testCompareRegionOptionsCompressionToBFALL2() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                PipelineTest.compareRegionOptionsCompressionToBF(collection, true);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//        }
    }

    @Test
    public void testCompareOptionsCompressionToBF1() throws NoSuchFieldException {
        PipelineTest.compareOptionsCompressionToBF("MGrid", false);
    }

    @Test
    public void testCompareOptionsCompressionToBF1All1() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                PipelineTest.compareOptionsCompressionToBF(collection, false);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//            System.out.println();
//        }
    }

    @Test
    public void testCompareOptionsCompressionToBFAll2() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                PipelineTest.compareOptionsCompressionToBF(collection, true);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//        }
    }

    @Test
    public void filterOptions1() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, AC");
        Assert.assertEquals(set, Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions2() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("ABC, ACD");
        Assert.assertEquals(set, Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions3() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, ABC");
        Set<Set<String>> result = PipelineTest.getOptionsSet("ABC");
        Assert.assertEquals(result, Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions4() throws Exception {
        Set<Set<String>> set = PipelineTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<String>> result = PipelineTest.getOptionsSet("ABC, BCD, DEF");
        Assert.assertEquals(result, Pipeline.filterOptions(set));
    }

    @Test
    public void testCreatePerformanceModel1() throws Exception {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        int duration1 = 3;
        Statement timedStatement1 = new StatementSleep(new ExpressionConstantInt(duration1));
        Region region1 = new SleepRegion(timedStatement1);
        regionsToOptions.put(region1.clone(), relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        int duration2 = 1;
        Statement timedStatement2 = new StatementSleep(new ExpressionConstantInt(duration2));
        Region region2 = new SleepRegion(timedStatement2);
        regionsToOptions.put(region2.clone(), relevantOptions);

        // Set<PerformanceEntry> measuredPerformance
        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        Set<Region> regions = new HashSet<>();
        region1.startTime(0);
        region1.endTime(0);
        region2.startTime(0);
        region2.endTime(0);
        regions.add(region1.clone());
        regions.add(region2.clone());
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, regions);
        measuredPerformance.add(performanceEntry);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        regions = new HashSet<>();
        region1.startTime(0);
        region1.endTime(duration1);
        region2.startTime(0);
        region2.endTime(duration2);
        regions.add(region1.clone());
        regions.add(region2.clone());
        performanceEntry = new PerformanceEntry(configuration, regions);
        measuredPerformance.add(performanceEntry);

        // Performance model
        PerformanceModel performanceModel = SleepPipeline.createPerformanceModel(measuredPerformance, regionsToOptions);

        int performance = 0;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

    // TODO test create compareRegionOptionsCompressionToBF performance mondel with compareRegionOptionsCompressionToBF base time

}
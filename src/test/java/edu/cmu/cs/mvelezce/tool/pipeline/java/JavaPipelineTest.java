package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantIntExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepPipeline;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.LotrackProcessor;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipelineTest {

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

        Map<Region, Set<String>> queryResult = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
        if(csv) {
            System.out.print(queryResult.size() + ", ");
        }
        else {
            System.out.println("Lotrack total number of regions: " + queryResult.size());
        }
        queryResult = LotrackProcessor.filterBooleans(queryResult);
        queryResult = LotrackProcessor.filterRegionsNoOptions(queryResult);

        Set<Set<String>> optionsSet = new HashSet<>(queryResult.values());
        Set<String> uniqueOptions = new HashSet<>();

        for(Set<String> options : optionsSet) {
            uniqueOptions.addAll(options);
        }

        JavaPipelineTest.compare(optionsSet, uniqueOptions, csv);
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
        fields.add(LotrackProcessor.USED_TERMS);
        fields.add(LotrackProcessor.CONSTRAINT);

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

            if(JSONResult.has(LotrackProcessor.USED_TERMS)) {
                for(Object string : JSONResult.getJSONArray(LotrackProcessor.USED_TERMS).toList()) {
                    String possibleString = string.toString();

                    if(possibleString.equals("true") || possibleString.equals("false")) {
                        continue;
                    }

                    options.add(string.toString());
                }
            }
            else if(JSONResult.has(LotrackProcessor.CONSTRAINT)) {
                // Be careful that this is imprecise since the constraints can be very large and does not fit in the db field
                String[] constraints = JSONResult.getString(LotrackProcessor.CONSTRAINT).split(" ");

                for(String constraint : constraints) {
                    constraint = constraint.replaceAll("[()^|!=]", "");
                    if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
                        continue;
                    }

                    if(constraint.contains(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
                        constraint = constraint.split(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
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
                throw new NoSuchFieldException("The query result does not have neither a " + LotrackProcessor.USED_TERMS + " or " + LotrackProcessor.CONSTRAINT + " fields");
            }

            if(!options.isEmpty()) {
                optionsSet.add(options);
            }

        }

        Set<String> uniqueOptions = new HashSet<>();

        for(Set<String> options : optionsSet) {
            uniqueOptions.addAll(options);
        }

        JavaPipelineTest.compare(optionsSet, uniqueOptions, csv);
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

        Set<Set<String>> configurations = edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.getConfigurationsToExecute(optionsSet);
        JavaPipelineTest.checkConfigurationIsStatisfied(optionsSet, configurations);

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
            Set<Set<String>> results = edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.getConfigurationsToExecute(relevantOptionsSet);
            JavaPipelineTest.checkConfigurationIsStatisfied(new HashSet<>(permutation), results);
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
                    if (JavaPipelineTest.matches(result, configuration, relevantOptions)) {
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
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, AC, AD, BE");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute2() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("ABC, BCD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute3() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, BCD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute4() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, BC");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute5() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, CDE");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute6() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, AC, BC");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute7() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, AC, AD, BC, BD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute8() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, AC, AD, BC, CD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test // TODO ERROR because I need to get the other 4 configs from ABC
    public void testGetConfigurationsToExecute9() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("ABC, CD, BD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute10() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("ABC, CD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute11() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("ABC, DEF");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute12() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute13() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("ABCD, ADXY, ABDX");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute14() {
        Set<Set<String>> relevantOptionsSet = JavaPipelineTest.getOptionsSet("AB, AC, AD, BC, CD, BD");
        JavaPipelineTest.checktOptionsPermuatationsToGetConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testCompareRegionOptionsCompressionToBF1() throws NoSuchFieldException {
        JavaPipelineTest.compareRegionOptionsCompressionToBF("ZipMeChangedSimplified", false);
    }

    @Test
    public void testCompareRegionOptionsCompressionToBFAll1() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                JavaPipelineTest.compareRegionOptionsCompressionToBF(collection, false);
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
//                JavaPipelineTest.compareRegionOptionsCompressionToBF(collection, true);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//        }
    }

    @Test
    public void testCompareOptionsCompressionToBF1() throws NoSuchFieldException {
        JavaPipelineTest.compareOptionsCompressionToBF("MGrid", false);
    }

    @Test
    public void testCompareOptionsCompressionToBF1All1() {
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
//
//        for(String collection : collections) {
//            try {
//                JavaPipelineTest.compareOptionsCompressionToBF(collection, false);
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
//                JavaPipelineTest.compareOptionsCompressionToBF(collection, true);
//            }
//            catch(NoSuchFieldException nsfe) {
//                System.out.println("This program does not have taint tracking info");
//            }
//        }
    }

    @Test
    public void filterOptions1() throws Exception {
        Set<Set<String>> set = JavaPipelineTest.getOptionsSet("AB, AC");
        Assert.assertEquals(set, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions2() throws Exception {
        Set<Set<String>> set = JavaPipelineTest.getOptionsSet("ABC, ACD");
        Assert.assertEquals(set, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions3() throws Exception {
        Set<Set<String>> set = JavaPipelineTest.getOptionsSet("AB, ABC");
        Set<Set<String>> result = JavaPipelineTest.getOptionsSet("ABC");
        Assert.assertEquals(result, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void filterOptions4() throws Exception {
        Set<Set<String>> set = JavaPipelineTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<String>> result = JavaPipelineTest.getOptionsSet("ABC, BCD, DEF");
        Assert.assertEquals(result, edu.cmu.cs.mvelezce.tool.pipeline.Pipeline.filterOptions(set));
    }

    @Test
    public void testCreatePerformanceModel1() throws Exception {
        // Map<Region, Set<String>> regionsToOptions
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
        Set<String> relevantOptions = new HashSet<>();
        relevantOptions.add("A");
        int duration1 = 3;
        Statement timedStatement1 = new SleepStatement(new ConstantIntExpression(duration1));
        Region region1 = new SleepRegion(timedStatement1);
        regionsToOptions.put(region1.clone(), relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add("B");
        int duration2 = 1;
        Statement timedStatement2 = new SleepStatement(new ConstantIntExpression(duration2));
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
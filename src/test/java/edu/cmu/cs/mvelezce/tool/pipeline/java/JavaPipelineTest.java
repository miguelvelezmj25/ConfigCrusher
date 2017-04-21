package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.tool.pipeline.PipelineTest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipelineTest {

    public static void compareRegionOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
        // program, regions, options, BF configurations, constraints, compressed configurations, compressed over BF
        if(csv) {
            System.out.print(program + ", ");
        }
        else {
            System.out.println(program);
        }

        Map<JavaRegion, Set<String>> queryResult = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
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
        List<String> queryResult = ScalaMongoDriverConnector.findProjection(program, fields);
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

    @Test
    public void testCompareRegionOptionsCompressionToBF1() throws NoSuchFieldException {
        JavaPipelineTest.compareRegionOptionsCompressionToBF("platypus", false);
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
    public void testBuildPerformanceModel1() throws NoSuchFieldException {
        JavaPipeline.buildPerformanceModel(JavaPipeline.PLAYYPUS_PROGRAM);
    }
}
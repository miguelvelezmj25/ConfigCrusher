package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.mongo.connector.scaladriver.ScalaMongoDriverConnector;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 4/6/17.
 */
public class LotrackProcessorTest {
    @Test
    public void filterRegionsNoOptions1() {
        Region region = new Region("a", "b", "c");
        Set<String> options = new HashSet<>();
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Assert.assertTrue(LotrackProcessor.filterRegionsNoOptions(regionToOptions).isEmpty());
    }

    @Test
    public void filterRegionsNoOptions2() {
        Region region = new Region("a", "b", "c");
        Set<String> options = new HashSet<>();
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        region = new Region("d", "e", "c");
        options = new HashSet<>();
        options.add("String");
        regionToOptions.put(region, options);

        Assert.assertTrue(LotrackProcessor.filterRegionsNoOptions(regionToOptions).size() == 1);
    }

    @Test
    public void getRegionsToOptions1() throws NoSuchFieldException {
        Map<Region, Set<String>> output = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, JavaPipeline.PLAYYPUS_PROGRAM);
//        for(Map.Entry<Region, Set<String>> entry : output.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue().size() + " " + entry.getValue());
//        }
        Assert.assertFalse(output.isEmpty());
    }

    @Test
    public void getRegionsToOptions2() throws NoSuchFieldException {
        Map<Region, Set<String>> output = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, JavaPipeline.LANGUAGETOOL_PROGRAM);
//        for(Map.Entry<Region, Set<String>> entry : output.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue().size() + " " + entry.getValue());
//        }
        Assert.assertFalse(output.isEmpty());
    }

    @Test
    public void filterBooleans() {
        String TRUE = "true";
        String FALSE = "false";
        Region region = new Region("a", "b", "c");
        Set<String> options = new HashSet<>();
        options.add(TRUE);
        options.add(FALSE);
        options.add("WIFI");
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Map<Region, Set<String>> result = LotrackProcessor.filterBooleans(regionToOptions);

        Assert.assertTrue(!result.get(region).contains(TRUE));
        Assert.assertTrue(!result.get(region).contains(FALSE));
    }

    @Test
    public void test() {
        List<String> projection = new ArrayList<>();
        projection.add(LotrackProcessor.JAVA_LINE_NO);
//        projection.add(LotrackProcessor.JIMPLE_LINE_NO);
        projection.add(LotrackProcessor.CONSTRAINT_PRETTY);
//        projection.add(LotrackProcessor.USED_TERMS);

        List<String> sort = new ArrayList<>();
        sort.add(LotrackProcessor.JIMPLE_LINE_NO);

        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
        List<String> queryResult = ScalaMongoDriverConnector.findProjectionFilterAscending(JavaPipeline.PLAYYPUS_PROGRAM, projection, LotrackProcessor.METHOD, "getCommandData", sort);
        ScalaMongoDriverConnector.close();

        for(String result : queryResult) {
            JSONObject JSONResult = new JSONObject(result);
//            System.out.println(JSONResult);

        }
    }

}
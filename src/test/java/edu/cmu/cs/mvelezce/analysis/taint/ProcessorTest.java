package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.pipeline.JavaPipeline;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/6/17.
 */
public class ProcessorTest {
    @Test
    public void filterRegionsNoOptions1() throws Exception {
        Region region = new Region("compareCompressionToBF", "b", "c");
        Set<String> options = new HashSet<>();
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Assert.assertTrue(Processor.filterRegionsNoOptions(regionToOptions).isEmpty());
    }

    @Test
    public void filterRegionsNoOptions2() throws Exception {
        Region region = new Region("compareCompressionToBF", "b", "c");
        Set<String> options = new HashSet<>();
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        region = new Region("d", "e", "c");
        options = new HashSet<>();
        options.add("String");
        regionToOptions.put(region, options);

        Assert.assertTrue(Processor.filterRegionsNoOptions(regionToOptions).size() == 1);
    }

    @Test
    public void getRegionsToOptions1() throws Exception {
        Map<Region, Set<String>> output = Processor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, JavaPipeline.PLAYYPUS_PROGRAM);
//        for(Map.Entry<Region, Set<String>> entry : output.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue().size() + " " + entry.getValue());
//        }
        Assert.assertFalse(output.isEmpty());
    }

    @Test
    public void getRegionsToOptions2() throws Exception {
        Map<Region, Set<String>> output = Processor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, JavaPipeline.LANGUAGETOOL_PROGRAM);
//        for(Map.Entry<Region, Set<String>> entry : output.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue().size() + " " + entry.getValue());
//        }
        Assert.assertFalse(output.isEmpty());
    }

    @Test
    public void filterBooleans() throws Exception {
        String TRUE = "true";
        String FALSE = "false";
        Region region = new Region("compareCompressionToBF", "b", "c");
        Set<String> options = new HashSet<>();
        options.add(TRUE);
        options.add(FALSE);
        options.add("WIFI");
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Map<Region, Set<String>> result = Processor.filterBooleans(regionToOptions);

        Assert.assertTrue(!result.get(region).contains(TRUE));
        Assert.assertTrue(!result.get(region).contains(FALSE));
    }

}
package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * TODO might not be needed anymore
 * <p>
 * Created by mvelezce on 4/6/17.
 */
public class LotrackProcessorTest {
    @Test
    public void filterRegionsNoOptions1() {
        JavaRegion region = new JavaRegion("a", "buildPerformanceModel", "c");
        Set<String> options = new HashSet<>();
        Map<JavaRegion, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Assert.assertTrue(LotrackProcessor.filterRegionsNoOptions(regionToOptions).isEmpty());
    }

    @Test
    public void filterRegionsNoOptions2() {
        JavaRegion region = new JavaRegion("a", "buildPerformanceModel", "c");
        Set<String> options = new HashSet<>();
        Map<JavaRegion, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        region = new JavaRegion("d", "e", "c");
        options = new HashSet<>();
        options.add("String");
        regionToOptions.put(region, options);

        Assert.assertTrue(LotrackProcessor.filterRegionsNoOptions(regionToOptions).size() == 1);
    }

    @Test
    public void filterBooleans() {
        String TRUE = "true";
        String FALSE = "false";
        JavaRegion region = new JavaRegion("a", "buildPerformanceModel", "c");
        Set<String> options = new HashSet<>();
        options.add(TRUE);
        options.add(FALSE);
        options.add("WIFI");
        Map<JavaRegion, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Map<JavaRegion, Set<String>> result = LotrackProcessor.filterBooleans(regionToOptions);

        Assert.assertTrue(!result.get(region).contains(TRUE));
        Assert.assertTrue(!result.get(region).contains(FALSE));
    }

}
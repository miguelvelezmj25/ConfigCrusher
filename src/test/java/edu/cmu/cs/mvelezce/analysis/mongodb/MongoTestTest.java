package edu.cmu.cs.mvelezce.analysis.mongodb;

import edu.cmu.cs.mvelezce.analysis.mapper.Region;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/6/17.
 */
public class MongoTestTest {
    @Test
    public void getRegionsToOptions() throws Exception {
        Assert.assertFalse(MongoTest.getRegionsToOptions().isEmpty());
    }

    @Test
    public void filterBooleans() throws Exception {
        String TRUE = "true";
        String FALSE = "false";
        Region region = new Region("a", "b", "c");
        Set<String> options = new HashSet<>();
        options.add(TRUE);
        options.add(FALSE);
        options.add("WIFI");
        Map<Region, Set<String>> regionToOptions = new HashMap<>();
        regionToOptions.put(region, options);

        Map<Region, Set<String>> result = MongoTest.filterBooleans(regionToOptions);

        Assert.assertTrue(!result.get(region).contains(TRUE));
        Assert.assertTrue(!result.get(region).contains(FALSE));
    }

}
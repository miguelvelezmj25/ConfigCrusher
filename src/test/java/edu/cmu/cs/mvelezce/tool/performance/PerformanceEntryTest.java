package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 5/2/17.
 */
public class PerformanceEntryTest {
    @Test
    public void testCalculateRealPerformance() throws Exception {
        // Configuration
        Set<String> configuration = new HashSet<>();

        // List of regions
        List<Region> executedRegions = new LinkedList<>();

        // Regions
        Region region = new Region("program", 14723625672461L, 14727163866751L);
        executedRegions.add(region);

        region = new Region("1", 14723832354023L, 14725346965687L);
        executedRegions.add(region);

        region = new Region("2", 14724639346780L, 14725242502968L);
        executedRegions.add(region);

        region = new Region("2", 14724639346780L, 14725242502968L);
        executedRegions.add(region);

        region = new Region("1", 14723832354023L, 14725346965687L);
        executedRegions.add(region);

        region = new Region("3", 14725451504656L, 14727163824175L);
        executedRegions.add(region);

        region = new Region("4", 14726357054866L, 14726960045845L);
        executedRegions.add(region);

        region = new Region("4", 14726357054866L, 14726960045845L);
        executedRegions.add(region);

        region = new Region("3", 14725451504656L, 14727163824175L);
        executedRegions.add(region);

        region = new Region("program", 14723625672461L, 14727163866751L);
        executedRegions.add(region);

        // Performance entry
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, executedRegions);

        // Expected
        Map<Region, Long> expectedResult = new HashMap<>();

        region = new Region("1", 14723832354023L, 14725346965687L);
        expectedResult.put(region, 911455476L);

        region = new Region("2", 14724639346780L, 14725242502968L);
        expectedResult.put(region, 603156188L);

        region = new Region("3", 14725451504656L, 14727163824175L);
        expectedResult.put(region, 1109328540L);

        region = new Region("4", 14726357054866L, 14726960045845L);
        expectedResult.put(region, 602990979L);

        region = new Region("program", 14723625672461L, 14727163866751L);
        expectedResult.put(region, 311263107L);

        Assert.assertTrue(expectedResult.equals(performanceEntry.getRegionsToExecutionTime()));
    }

}
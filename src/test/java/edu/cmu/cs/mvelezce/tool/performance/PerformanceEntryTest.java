package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 5/2/17.
 */
public class PerformanceEntryTest {
    @Test
    public void testCalculateRealPerformance1() throws Exception {
        // Configuration
        Set<String> configuration = new HashSet<>();

        // List of regions
        List<Region> executedRegions = new LinkedList<>();

        // Regions
        Region region = new Region(Regions.PROGRAM_REGION_ID, 14723625672461L, 0);
        executedRegions.add(region);

        region = new Region("1", 14723832354023L, 0);
        executedRegions.add(region);

        region = new Region("2", 14724639346780L, 0);
        executedRegions.add(region);

        region = new Region("2", 0, 14725242502968L);
        executedRegions.add(region);

        region = new Region("1", 0, 14725346965687L);
        executedRegions.add(region);

        region = new Region("3", 14725451504656L, 0);
        executedRegions.add(region);

        region = new Region("4", 14726357054866L, 0);
        executedRegions.add(region);

        region = new Region("4", 0, 14726960045845L);
        executedRegions.add(region);

        region = new Region("3", 0, 14727163824175L);
        executedRegions.add(region);

        region = new Region("program", 0, 14727163866751L);
        executedRegions.add(region);

        // Performance entry
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, executedRegions);

        // Expected
        Map<Region, Long> expectedResult = new HashMap<>();

        region = new Region("1");
        expectedResult.put(region, 911455476L);

        region = new Region("2");
        expectedResult.put(region, 603156188L);

        region = new Region("3");
        expectedResult.put(region, 1109328540L);

        region = new Region("4");
        expectedResult.put(region, 602990979L);

        region = new Region(Regions.PROGRAM_REGION_ID);
        expectedResult.put(region, 311263107L);

        for(Map.Entry<Region, Long> actualResultEntry : performanceEntry.getRegionsToExecutionTime().entrySet()) {
            for(Map.Entry<Region, Long> expectedResultEntry : expectedResult.entrySet()) {
                if(actualResultEntry.getKey().getRegionID().equals(expectedResultEntry.getKey().getRegionID())) {
                    Assert.assertEquals(expectedResultEntry.getValue(), actualResultEntry.getValue());
                }
            }
        }
    }

}
package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.HashMap;
import java.util.Map;

public class RegionsCounter {

    public static int startCount = 0;
    public static int endCount = 0;

    private static Map<String, Long> regionsToCount = new HashMap<>();

    public static void enter(String id) {
        RegionsCounter.startCount++;
        Long count = RegionsCounter.regionsToCount.get(id);

        if(count == null) {
            count = 0L;
        }

        RegionsCounter.regionsToCount.put(id, count + 1L);
    }

    public static void exit(String id) {
        RegionsCounter.endCount++;

        Long count = RegionsCounter.regionsToCount.get(id);

        if(count == null) {
            count = 0L;
        }

        RegionsCounter.regionsToCount.put(id, count + 1L);
    }

    public static Map<String, Long> getRegionsToCount() {
        return RegionsCounter.regionsToCount;
    }
}

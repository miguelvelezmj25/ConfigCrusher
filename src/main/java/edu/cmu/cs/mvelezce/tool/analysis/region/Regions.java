package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {

    public static final String PROGRAM_REGION_ID = "program";

    private static Stack<String> executingRegions = new Stack<>();
    private static Stack<Long> executingRegionsStart = new Stack<>();
    private static Stack<Long> innerRegionsExecutionTime = new Stack<>();
    private static Map<String, Long> regionsToProcessedPerformance = new HashMap<>();

    public static void enter(String id) {
//        System.out.println("Enter " + id);
        long start = System.nanoTime();
        Regions.executingRegions.push(id);
        Regions.executingRegionsStart.push(start);
        Regions.innerRegionsExecutionTime.push(0L);
    }

    public static void exit(String id) {
        if(!Regions.executingRegions.peek().equals(id)) {
//            System.out.println("Could not exit " + id);
            return;
        }

//        System.out.println("Exit " + id);

        long end = System.nanoTime();
        long start = Regions.executingRegionsStart.pop();
        long innerTime = Regions.innerRegionsExecutionTime.pop();
        long regionExecutionTime = end - start - innerTime;
        long currentRegionExecutionTime = regionExecutionTime;

        String region = Regions.executingRegions.pop();
        Long time = Regions.regionsToProcessedPerformance.get(region);

        if(time != null) {
            regionExecutionTime += time;
        }

        Regions.regionsToProcessedPerformance.put(region, regionExecutionTime);

        if(executingRegions.isEmpty()) {
            return;
        }

        Stack<Long> added = new Stack<>();

        while(!innerRegionsExecutionTime.isEmpty()) {
            long currentInnerRegionExecutionTime = innerRegionsExecutionTime.pop();
            added.push(currentInnerRegionExecutionTime + currentRegionExecutionTime);
        }

        while(!added.isEmpty()) {
            innerRegionsExecutionTime.push(added.pop());
        }
    }

    public static Map<String, Long> getRegionsToProcessedPerformance() {
        return regionsToProcessedPerformance;
    }
}

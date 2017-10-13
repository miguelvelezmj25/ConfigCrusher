package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.*;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {

    public static final String PROGRAM_REGION_ID = "program";

    public static int startCount = 0;
    public static int endCount = 0;

    private static Stack<String> executingRegions = new Stack<>();
    private static Stack<Long> executingRegionsStart = new Stack<>();
    private static Stack<Long> innerRegionsExecutionTime = new Stack<>();
    private static Map<String, Long> regionsToProcessedPerformance = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Started");
        long start = System.nanoTime();

        for(int i = 0; i < 96313444; i++) {
            Regions.enter("dsf");
            Regions.exit("dsf");
        }

        long end = System.nanoTime();
        long time = end - start;

        System.out.println("start count " + Regions.startCount);
        System.out.println("end count " + Regions.endCount);
        System.out.println(time / 1000000000.0);
    }

//    public static void enter(RegionID regionID) {
//        long start = System.nanoTime();
//        executingRegionsStart.push(start);
//
//        Regions.executingRegions.push(regionID.toString());
//        Regions.innerRegionsExecutionTime.push(0L);
//        Regions.startCount++;
//    }

    public static void enter(String id) {
        long start = System.nanoTime();
        executingRegionsStart.push(start);

        Regions.executingRegions.push(id);
        Regions.innerRegionsExecutionTime.push(0L);
        Regions.startCount++;

    }

    public static void exit(String id) {
        long end = System.nanoTime();
        long start = executingRegionsStart.pop();
        long innerTime = innerRegionsExecutionTime.pop();
        long regionExecutionTime = end - start - innerTime;
        long currentRegionExecutionTime = regionExecutionTime;

        String region = Regions.executingRegions.pop();
        Long time = regionsToProcessedPerformance.get(region);

        if(time != null) {
            regionExecutionTime += time;
        }

        regionsToProcessedPerformance.put(region, regionExecutionTime);

        Regions.endCount++;

        if(executingRegions.isEmpty()) {
            return;
        }

        Stack<Long> added = new Stack<>();

        while (!innerRegionsExecutionTime.isEmpty()) {
            long currentInnerRegionExecutionTime = innerRegionsExecutionTime.pop();
            added.push(currentInnerRegionExecutionTime + currentRegionExecutionTime);
        }

        while (!added.isEmpty()) {
            innerRegionsExecutionTime.push(added.pop());
        }
    }

    public static Map<String, Long> getRegionsToProcessedPerformance() {
        return regionsToProcessedPerformance;
    }
}

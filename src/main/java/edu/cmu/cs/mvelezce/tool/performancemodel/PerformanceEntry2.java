package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
// TODO save this in a json file
public class PerformanceEntry2 {
    private Set<String> configuration;
    private Map<Region, Long> regionsToRawPerformance = new HashMap<>();
    private Map<Region, Double> regionsToRawPerformanceHumanReadable = new HashMap<>();
    private Map<Region, Set<Region>> regionsToInnerRegions = new HashMap<>();
    private Map<Region, Long> regionsToProcessedPerformance = new HashMap<>();
    private Map<Region, Double> regionsToProcessedPerformanceHumanReadable = new HashMap<>();

    private PerformanceEntry2() {
        ;
    }

    public PerformanceEntry2(Set<String> configuration, Map<Region, Long> regionsToRawPerformance) {
        this.configuration = configuration;
        this.regionsToRawPerformance = regionsToRawPerformance;

        // TODO maybe add a method that changes longs to doubles and the another one that divides by 10^9
//        this.regionsToRawPerformanceHumanReadable = regionsToRawPerformance;
    }

    public PerformanceEntry2(Execution execution) {
        this.configuration = execution.getConfiguration();
        List<Region> trace = execution.getTrace();

        this.calculatePerformance(trace);
        this.calculateInnerRegions(trace);
        this.calculateProcessedPerformance(trace);
    }

    private void calculateProcessedPerformance(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();
        Stack<Long> innerRegionExecutionTime = new Stack<>();
//        Region previousRegionEntry = null;

        for(Region region : trace) {
            if(executingRegions.isEmpty() || !executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.add(region);
                innerRegionExecutionTime.push(0L);
            }
            else {
                Region top = executingRegions.pop();
                long regionExecutionTime = region.getEndTime() - top.getStartTime();
                regionExecutionTime -= innerRegionExecutionTime.pop();

                this.regionsToProcessedPerformance.put(region, regionExecutionTime);

                // TODO to human readable method
                double humanTime = regionExecutionTime / 1000000000.0;
                this.regionsToProcessedPerformanceHumanReadable.put(region, humanTime);

                // Adding new inner execution time
                if(!executingRegions.isEmpty()) {
                    Stack<Long> added = new Stack<>();

                    while (!innerRegionExecutionTime.isEmpty()) {
                        long currentInnerRegionExecutionTime = innerRegionExecutionTime.pop();
                        added.push(currentInnerRegionExecutionTime + regionExecutionTime);
                    }

                    while (!added.isEmpty()) {
                        innerRegionExecutionTime.push(added.pop());
                    }
                }
            }
        }
    }

    private void calculatePerformance(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();

        for(Region region : trace) {
            if(executingRegions.empty() || !executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.add(region);
            }
            else {
                Region top = executingRegions.pop();
                long start = top.getStartTime();
                long end = region.getEndTime();
                long time = (end - start);
                time -= top.getOverhead();
                time -= region.getOverhead();
                this.regionsToRawPerformance.put(region, time);

                // TODO to human readable method
                double humanTime = time / 1000000000.0;
                this.regionsToRawPerformanceHumanReadable.put(region, humanTime);
            }
        }
    }

    public void calculateInnerRegions(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();

        // Get all regions
        for(Region region : trace) {
            if(executingRegions.isEmpty() || !executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.push(region);
                this.regionsToInnerRegions.put(region, new HashSet<>());
            }
            else {
                executingRegions.pop();
            }
        }

        for(Region region : trace) {
            if(executingRegions.isEmpty()) {
                executingRegions.push(region);
            }
            else if(executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.pop();
            }
            else {
                for(Region executingRegion : executingRegions) {
                    Set<Region> innerRegions = this.regionsToInnerRegions.get(executingRegion);
                    innerRegions.add(region);
                }

                executingRegions.push(region);
            }
        }
    }

//
//    public PerformanceEntry2(Set<String> configuration, Map<Region, Long> regionsToExecutionTime, Map<Region, Set<Region>> regionsToInnerRegions) {
//        this.configuration = configuration;
//        this.regionsToExecutionTime = regionsToExecutionTime;
//        this.regionsToInnerRegions = regionsToInnerRegions;
//
//        for(Map.Entry<Region, Long> entry : regionsToExecutionTime.entrySet()) {
//            this.regionsToExecutionTime2.put(entry.getKey(), entry.getValue() / 1000000000.0);
//        }
//    }
//
//

//
//    public long getTimeOfRegionID(String ID) {
//        long time = -1;
//
//        for(Map.Entry<Region, Long> regionToTime : this.regionsToExecutionTime.entrySet()) {
//            if(regionToTime.getKey().getRegionID().equals(ID)) {
//                time += regionToTime.getValue();
//            }
//        }
//
//        return time;
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if(this == o) {
//            return true;
//        }
//        if(o == null || getClass() != o.getClass()) {
//            return false;
//        }
//
//        PerformanceEntry2 that = (PerformanceEntry2) o;
//
//        if(!configuration.equals(that.configuration)) {
//            return false;
//        }
//        if(!regionsToExecutionTime.equals(that.regionsToExecutionTime)) {
//            return false;
//        }
//        return regionsToInnerRegions.equals(that.regionsToInnerRegions);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = configuration.hashCode();
//        result = 31 * result + regionsToExecutionTime.hashCode();
//        result = 31 * result + regionsToInnerRegions.hashCode();
//        return result;
//    }
}

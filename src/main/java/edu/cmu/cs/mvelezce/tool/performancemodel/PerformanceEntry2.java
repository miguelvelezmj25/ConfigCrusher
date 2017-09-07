package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class PerformanceEntry2 {
    private Set<String> configuration;
    private Map<Region, Long> regionsToPerformance = new HashMap<>();
    private Map<Region, Double> regionsToPerformanceHumanReadable = new HashMap<>();
    private Map<Region, Set<Region>> regionsToInnerRegions = new HashMap<>();

    public PerformanceEntry2(Set<String> configuration, Map<Region, Long> regionsToPerformance) {
        this.configuration = configuration;
        this.regionsToPerformance = regionsToPerformance;

        // TODO maybe add a method that changes longs to doubles and the another one that divides by 10^9
//        this.regionsToPerformanceHumanReadable = regionsToPerformance;
    }

    public PerformanceEntry2(Execution execution) {
        this.configuration = execution.getConfiguration();
        List<Region> trace = execution.getTrace();

        this.calculatePerformance(trace);
        this.calculateInnerRegions(trace);
    }

    private void calculatePerformance(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();

        for(Region region : trace) {
            if(executingRegions.empty()) {
                executingRegions.add(region);

                continue;
            }

            Region top = executingRegions.peek();

            if(top.getRegionID().equals(region.getRegionID())) {
                long start = top.getStartTime();
                long end = region.getEndTime();
                long time = (end - start);
                time -= top.getOverhead();
                time -= region.getOverhead();
                this.regionsToPerformance.put(region, time);

                double humanTime = time / 1000000000.0;
                this.regionsToPerformanceHumanReadable.put(region, humanTime);

                executingRegions.pop();
            }
            else {
                executingRegions.add(region);
            }
        }
    }

    public void calculateInnerRegions(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();

        // Get all regions
        for(Region region : trace) {
            if(executingRegions.isEmpty()) {
                executingRegions.push(region);
                this.regionsToInnerRegions.put(region, new HashSet<>());
            }
            else if(executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.pop();
            }
            else {
                executingRegions.push(region);
                this.regionsToInnerRegions.put(region, new HashSet<>());
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
//    /**
//     * Recreating execution trace to calculate real performancemodel of each region
//     *
//     * @param executedRegions
//     */
//    public void calculateRealPerformance(List<Region> executedRegions) {
//        Stack<Region> executingRegions = new Stack<>();
//        Stack<Long> innerRegionExecutionTime = new Stack<>();
//        Region previousRegionEntry = null;
//
//        for(Region executingRegion : executedRegions) {
//            if(executingRegions.isEmpty()) {
//                executingRegions.add(executingRegion);
//                innerRegionExecutionTime.push((long) 0);
//            }
//            else if(executingRegions.peek().getRegionID().equals(executingRegion.getRegionID())) {
//                if(executingRegions.peek().getStartTime() > executingRegion.getEndTime()) {
//                    throw new RuntimeException("A region has a negative execution time. This might be caused by incorrect instrumentation");
//                }
//
//                Region startInfoRegion = executingRegions.pop();
//                long regionExecutionTime = executingRegion.getEndTime() - startInfoRegion.getStartTime();
//
//                if(!previousRegionEntry.getRegionID().equals(executingRegion.getRegionID())) {
//                    regionExecutionTime -= innerRegionExecutionTime.peek();
//                }
//
//                this.regionsToExecutionTime.put(executingRegion, regionExecutionTime);
//                this.regionsToExecutionTime2.put(executingRegion, regionExecutionTime / 1000000000.0);
//                innerRegionExecutionTime.pop();
//
//                // Adding new inner execution time
//                if(!executingRegions.isEmpty()) {
//                    Stack<Long> added = new Stack<>();
//
//                    while (!innerRegionExecutionTime.isEmpty()) {
//                        long currentInnerRegionExecutionTime = innerRegionExecutionTime.pop();
//                        added.push(currentInnerRegionExecutionTime + regionExecutionTime);
//                    }
//
//                    while (!added.isEmpty()) {
//                        innerRegionExecutionTime.push(added.pop());
//                    }
//                }
//            }
//            else {
//                executingRegions.add(executingRegion);
//                innerRegionExecutionTime.push((long) 0);
//            }
//
//            previousRegionEntry = executingRegion;
//        }
//    }
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

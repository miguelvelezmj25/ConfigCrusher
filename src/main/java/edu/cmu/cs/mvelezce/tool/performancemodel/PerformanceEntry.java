package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class PerformanceEntry {
    private Set<String> configuration;
    private Map<Region, Long> regionsToExecutionTime;
    private Map<Region, Double> regionsToExecutionTime2 = new LinkedHashMap<>();
    private Map<Region, Set<Region>> regionsToInnerRegions;

    public PerformanceEntry(Set<String> configuration, List<Region> executedRegions) {
        this.configuration = configuration;
        this.regionsToExecutionTime = new LinkedHashMap<>();
        this.regionsToInnerRegions = new HashMap<>();

        long program = 0;

        for(Region region : executedRegions) {
            if(region.getRegionID().equals("program")) {
                if(region.getStartTime() != 0) {
                    program = region.getStartTime();
                }
                else {
                    program = region.getEndTime() - program;
                }
            }
        }

        System.out.println(configuration /* + " " + program / 1000000000.0*/);

//        this.calculateInnerRegions(executedRegions);
//        this.calculateRealPerformance(executedRegions);
        
        this.calculatePerformance(executedRegions);

        for(Map.Entry<Region, Long> entry : this.regionsToExecutionTime.entrySet()) {
            if(entry.getKey().getRegionID().equals("program")) {
                System.out.println(entry.getValue());
            }
        }
    }

    private void calculatePerformance(List<Region> executedRegions) {
        Stack<Region> executingRegions = new Stack<>();

        for(Region region : executedRegions) {
            if(executingRegions.empty()) {
                executingRegions.add(region);

                continue;
            }

            Region top = executingRegions.peek();

            if(top.getRegionID().equals(region.getRegionID())) {
                if(region.getEndTime() < top.getStartTime()) {
                    throw new RuntimeException("The end time of a future region is less than the start time of previously visited region");
                }

                long start = top.getStartTime();
                long end = region.getEndTime();
                double time = (end - start) / 1000000000.0;
                this.regionsToExecutionTime2.put(region, time);

                executingRegions.pop();
            }
            else {
                executingRegions.add(region);
            }
        }

    }

    public PerformanceEntry(Set<String> configuration, Map<Region, Long> regionsToExecutionTime, Map<Region, Set<Region>> regionsToInnerRegions) {
        this.configuration = configuration;
        this.regionsToExecutionTime = regionsToExecutionTime;
        this.regionsToInnerRegions = regionsToInnerRegions;

        for(Map.Entry<Region, Long> entry : regionsToExecutionTime.entrySet()) {
            this.regionsToExecutionTime2.put(entry.getKey(), entry.getValue() / 1000000000.0);
        }
    }

    public void calculateInnerRegions(List<Region> executedRegions) {
        Stack<Region> executingRegions = new Stack<>();

        // Get all regions
        for(Region executingRegion : executedRegions) {
            if(executingRegions.isEmpty()) {
                executingRegions.push(executingRegion);
                this.regionsToInnerRegions.put(executingRegion, new HashSet<>());
            }
            else if(executingRegions.peek().getRegionID().equals(executingRegion.getRegionID())) {
                executingRegions.pop();
            }
            else {
                executingRegions.push(executingRegion);
                this.regionsToInnerRegions.put(executingRegion, new HashSet<>());
            }
        }

        for(Region executingRegion : executedRegions) {
            if(executingRegions.isEmpty()) {
                executingRegions.push(executingRegion);
            }
            else if(executingRegions.peek().getRegionID().equals(executingRegion.getRegionID())) {
                executingRegions.pop();
            }
            else {
                for(Map.Entry<Region, Set<Region>> regionToInnerRegions : this.regionsToInnerRegions.entrySet()) {
                    for(Region region : executingRegions) {
                        if(regionToInnerRegions.getKey().getRegionID().equals(region.getRegionID())) {
                            regionToInnerRegions.getValue().add(executingRegion);
                        }
                    }
                }

                executingRegions.push(executingRegion);
            }
        }
    }

    /**
     * Recreating execution trace to calculate real performancemodel of each region
     *
     * @param executedRegions
     */
    public void calculateRealPerformance(List<Region> executedRegions) {
        Stack<Region> executingRegions = new Stack<>();
        Stack<Long> innerRegionExecutionTime = new Stack<>();
        Region previousRegionEntry = null;

        for(Region executingRegion : executedRegions) {
            if(executingRegions.isEmpty()) {
                executingRegions.add(executingRegion);
                innerRegionExecutionTime.push((long) 0);
            }
            else if(executingRegions.peek().getRegionID().equals(executingRegion.getRegionID())) {
                if(executingRegions.peek().getStartTime() > executingRegion.getEndTime()) {
                    throw new RuntimeException("A region has a negative execution time. This might be caused by incorrect instrumentation");
                }

                Region startInfoRegion = executingRegions.pop();
                long regionExecutionTime = executingRegion.getEndTime() - startInfoRegion.getStartTime();

                if(!previousRegionEntry.getRegionID().equals(executingRegion.getRegionID())) {
                    regionExecutionTime -= innerRegionExecutionTime.peek();
                }

                this.regionsToExecutionTime.put(executingRegion, regionExecutionTime);
                this.regionsToExecutionTime2.put(executingRegion, regionExecutionTime / 1000000000.0);
                innerRegionExecutionTime.pop();

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
            else {
                executingRegions.add(executingRegion);
                innerRegionExecutionTime.push((long) 0);
            }

            previousRegionEntry = executingRegion;
        }
    }

    public long getTimeOfRegionID(String ID) {
        long time = -1;

        for(Map.Entry<Region, Long> regionToTime : this.regionsToExecutionTime.entrySet()) {
            if(regionToTime.getKey().getRegionID().equals(ID)) {
                time += regionToTime.getValue();
            }
        }

        return time;
    }

    public Set<String> getConfiguration() {
        return this.configuration;
    }

    public Map<Region, Long> getRegionsToExecutionTime() {
        return this.regionsToExecutionTime;
    }

    public Map<Region, Set<Region>> getRegionsToInnerRegions() {
        return this.regionsToInnerRegions;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        PerformanceEntry that = (PerformanceEntry) o;

        if(!configuration.equals(that.configuration)) {
            return false;
        }
        if(!regionsToExecutionTime.equals(that.regionsToExecutionTime)) {
            return false;
        }
        return regionsToInnerRegions.equals(that.regionsToInnerRegions);
    }

    @Override
    public int hashCode() {
        int result = configuration.hashCode();
        result = 31 * result + regionsToExecutionTime.hashCode();
        result = 31 * result + regionsToInnerRegions.hashCode();
        return result;
    }
}

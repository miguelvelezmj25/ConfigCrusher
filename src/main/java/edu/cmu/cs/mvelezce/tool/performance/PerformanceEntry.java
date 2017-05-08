package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class PerformanceEntry {
    private Set<String> configuration;
    private Map<Region, Long> regionsToExecutionTime;
    private Map<Region, Set<Region>> regionToInnerRegions;

    public PerformanceEntry(Set<String> configuration, List<Region> executedRegions) {
        this.configuration = configuration;
        this.regionsToExecutionTime = new HashMap<>();
        this.regionToInnerRegions = new HashMap<>();

        this.calculateRealPerformance(executedRegions);
    }

    /**
     * Recreating execution trace to calculate real performance of each region
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
                this.regionToInnerRegions.put(executingRegion, new HashSet<>());
            }
            else if(!executingRegions.peek().getRegionID().equals(executingRegion.getRegionID())) {
                if(!this.regionToInnerRegions.containsKey(executingRegions.peek())) {
                    this.regionToInnerRegions.put(executingRegions.peek(), new HashSet<>());
                }

                if(!this.regionToInnerRegions.containsKey(executingRegion)) {
                    this.regionToInnerRegions.put(executingRegion, new HashSet<>());
                }

                Set<Region> previousInnerRegions = this.regionToInnerRegions.get(executingRegions.peek());
                previousInnerRegions.add(executingRegion);

                executingRegions.add(executingRegion);
                innerRegionExecutionTime.push((long) 0);
            }
            else {
                if(executingRegions.peek().getStartTime() > executingRegion.getEndTime()) {
                    throw new RuntimeException("A region has a negative execution time. This might be caused by incorrect instrumentation");
                }

                Region startInfoRegion = executingRegions.pop();
                long regionExecutionTime = executingRegion.getEndTime() - startInfoRegion.getStartTime();

                if(!previousRegionEntry.getRegionID().equals(executingRegion.getRegionID())) {
                    regionExecutionTime -= innerRegionExecutionTime.peek();
                }

                innerRegionExecutionTime.pop();

                if(!executingRegions.isEmpty()) {
                    Stack<Long> added = new Stack<>();

                    while(!innerRegionExecutionTime.isEmpty()) {
                        long currentInnerRegionExecutionTime = innerRegionExecutionTime.pop();
                        added.push(currentInnerRegionExecutionTime + regionExecutionTime);
                    }

                    while(!added.isEmpty()) {
                        innerRegionExecutionTime.push(added.pop());
                    }
                }

                this.regionsToExecutionTime.put(executingRegion, regionExecutionTime);
            }

            previousRegionEntry = executingRegion;
        }
    }

    public long getTimeOfRegionID(String ID) {
        for(Map.Entry<Region, Long> regionToTime : this.regionsToExecutionTime.entrySet()) {
            if(regionToTime.getKey().getRegionID().equals(ID)) {
                return regionToTime.getValue();
            }
        }

        return -1;
    }

    public Set<String> getConfiguration() { return this.configuration; }

    public Map<Region, Long> getRegionsToExecutionTime() { return this.regionsToExecutionTime; }

    public Map<Region, Set<Region>> getRegionToInnerRegions() { return this.regionToInnerRegions; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PerformanceEntry that = (PerformanceEntry) o;

        if (!configuration.equals(that.configuration)) return false;
        if (!regionsToExecutionTime.equals(that.regionsToExecutionTime)) return false;
        return regionToInnerRegions.equals(that.regionToInnerRegions);
    }

    @Override
    public int hashCode() {
        int result = configuration.hashCode();
        result = 31 * result + regionsToExecutionTime.hashCode();
        result = 31 * result + regionToInnerRegions.hashCode();
        return result;
    }
}

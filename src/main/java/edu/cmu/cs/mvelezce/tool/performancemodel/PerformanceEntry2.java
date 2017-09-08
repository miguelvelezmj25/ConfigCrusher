package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
// TODO save this in a json file
public class PerformanceEntry2 implements IPerformanceEntry {
    private Set<String> configuration;
    private Map<Region, Long> regionsToRawPerformance = new LinkedHashMap<>();
    private Map<Region, Double> regionsToRawPerformanceHumanReadable = new LinkedHashMap<>();
    private Map<Region, Set<Region>> regionsToInnerRegions = new LinkedHashMap<>();
    private Map<Region, Long> regionsToProcessedPerformance = new LinkedHashMap<>();
    private Map<Region, Double> regionsToProcessedPerformanceHumanReadable = new LinkedHashMap<>();

    private PerformanceEntry2() {
        ;
    }

    public PerformanceEntry2(Execution execution) {
        this.configuration = execution.getConfiguration();
        List<Region> trace = execution.getTrace();

        this.calculatePerformance(trace);
        this.calculateInnerRegions(trace);
        this.calculateProcessedPerformance(trace);

        this.regionsToRawPerformanceHumanReadable = PerformanceEntry2.toHumanReadable(this.regionsToRawPerformance);
        this.regionsToProcessedPerformanceHumanReadable = PerformanceEntry2.toHumanReadable(this.regionsToProcessedPerformance);
    }

    // TODO where to put this method?

    /**
     * The assumption is that the value of type long is in nanoseconds.
     *
     * @param nanoSeconds
     * @return
     */
    public static double toSeconds(long nanoSeconds) {
        return nanoSeconds / 1000000000.0;
    }

    public static double toHumanReadable(double seconds) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String value = decimalFormat.format(seconds);

        return Double.valueOf(value);
    }

    // TODO where to put this method?

    /**
     * The assumption is that the values in the map are in nanoseconds and we transform them to seconds
     *
     * @param regionsToPerformance
     * @return
     */
    public static Map<Region, Double> toHumanReadable(Map<Region, Long> regionsToPerformance) {
        Map<Region, Double> result = new LinkedHashMap<>();

        for(Map.Entry<Region, Long> entry : regionsToPerformance.entrySet()) {
            result.put(entry.getKey(), PerformanceEntry2.toSeconds(entry.getValue()));
        }

        return result;
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

    public Set<String> getConfiguration() {
        return configuration;
    }

    public Map<Region, Long> getRegionsToRawPerformance() {
        return regionsToRawPerformance;
    }

    public Map<Region, Double> getRegionsToRawPerformanceHumanReadable() {
        return regionsToRawPerformanceHumanReadable;
    }

    public Map<Region, Set<Region>> getRegionsToInnerRegions() {
        return regionsToInnerRegions;
    }

    public Map<Region, Long> getRegionsToProcessedPerformance() {
        return regionsToProcessedPerformance;
    }

    public Map<Region, Double> getRegionsToProcessedPerformanceHumanReadable() {
        return regionsToProcessedPerformanceHumanReadable;
    }

    @Override
    public void writeToFile(IPerformanceEntry performanceEntry) throws IOException {
        // TODO
        throw new RuntimeException();
    }

    @Override
    public IPerformanceEntry readFromFile(File file) throws IOException {
        // TODO
        throw new RuntimeException();
    }
}

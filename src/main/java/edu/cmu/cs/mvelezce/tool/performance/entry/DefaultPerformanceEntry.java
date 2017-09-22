package edu.cmu.cs.mvelezce.tool.performance.entry;

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
public class DefaultPerformanceEntry implements PerformanceEntry {
    private Set<String> configuration;
    private Map<Region, Long> regionsToRawPerformance = new LinkedHashMap<>();
    private Map<Region, Double> regionsToRawPerformanceHumanReadable = new LinkedHashMap<>();
    private Map<Region, Set<Region>> regionsToInnerRegions = new LinkedHashMap<>();
    private Map<Region, Long> regionsToProcessedPerformance = new LinkedHashMap<>();
    private Map<Region, Double> regionsToProcessedPerformanceHumanReadable = new LinkedHashMap<>();

    private DefaultPerformanceEntry() {
        ;
    }

    public DefaultPerformanceEntry(Execution execution) {
        this.configuration = execution.getConfiguration();
        List<Region> trace = execution.getTrace();

        this.calculatePerformance(trace);
        this.calculateInnerRegions(trace);
        this.calculateProcessedPerformance(trace);

        this.regionsToRawPerformanceHumanReadable = DefaultPerformanceEntry.toHumanReadable(this.regionsToRawPerformance);
        this.regionsToProcessedPerformanceHumanReadable = DefaultPerformanceEntry.toHumanReadable(this.regionsToProcessedPerformance);
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
            result.put(entry.getKey(), DefaultPerformanceEntry.toSeconds(entry.getValue()));
        }

        return result;
    }

    private void calculateProcessedPerformance(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();
        Stack<Long> innerRegionExecutionTime = new Stack<>();

        for(Region region : trace) {
            if(executingRegions.isEmpty() || !executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.add(region);
                innerRegionExecutionTime.push(0L);
            }

            Region top = executingRegions.peek();

            if(!top.getRegionID().equals(region.getRegionID())) {
                continue;
            }

            if(top.getStartTime() == 0 || region.getEndTime() == 0) {
                continue;
            }

            long start = top.getStartTime();
            long end = region.getEndTime();
            long topOverhead = top.getOverhead();
            long regionExecutionTime = end - start;
            regionExecutionTime -= topOverhead;
            regionExecutionTime -= innerRegionExecutionTime.pop();
            // Thread.sleep might sleep for less time and can give us a negative time
            regionExecutionTime = Math.max(0, regionExecutionTime);

            this.regionsToProcessedPerformance.put(top, regionExecutionTime);
            executingRegions.pop();

            // Adding new inner execution time
            if(executingRegions.isEmpty()) {
                continue;
            }

            long regionOverhead = region.getOverhead();
            Stack<Long> added = new Stack<>();

            while (!innerRegionExecutionTime.isEmpty()) {
                long currentInnerRegionExecutionTime = innerRegionExecutionTime.pop();
                added.push(currentInnerRegionExecutionTime + regionExecutionTime + topOverhead + regionOverhead);
            }

            while (!added.isEmpty()) {
                innerRegionExecutionTime.push(added.pop());
            }
        }

        if(!executingRegions.isEmpty()) {
            throw new RuntimeException("There were executing regions left that were not processed");
        }
    }

    private void calculatePerformance(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();

        for(Region region : trace) {
            if(executingRegions.empty() || !executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.add(region);
                continue;
            }

            Region top = executingRegions.peek();

            if(!top.getRegionID().equals(region.getRegionID())) {
                continue;
            }

            if(top.getStartTime() == 0 || region.getEndTime() == 0) {
                continue;
            }

            long start = top.getStartTime();
            long end = region.getEndTime();
            long time = end - start;
            time -= top.getOverhead();
            // Thread.sleep might sleep for less time and can give us a negative time
            time = Math.max(0, time);

            if(this.regionsToRawPerformance.containsKey(top)) {
                throw new RuntimeException("What?");
            }

            this.regionsToRawPerformance.put(top, time);
            executingRegions.pop();
        }

        if(!executingRegions.isEmpty()) {
            throw new RuntimeException("There were executing regions left that were not processed");
        }
    }

    public void calculateInnerRegions(List<Region> trace) {
        Stack<Region> executingRegions = new Stack<>();

        // Get all regions
        for(Region region : trace) {
            if(executingRegions.isEmpty() || !executingRegions.peek().getRegionID().equals(region.getRegionID())) {
                executingRegions.push(region);
                continue;
            }

            Region top = executingRegions.peek();

            if(top.getRegionID().equals(region.getRegionID())) {
                if(top.getStartTime() != 0 && region.getEndTime() != 0) {
                    this.regionsToInnerRegions.put(top, new HashSet<>());
                    executingRegions.pop();
                }
            }
        }

        if(!executingRegions.isEmpty()) {
            throw new RuntimeException("There were executing regions left that were not processed");
        }

        if(!this.regionsToRawPerformance.keySet().equals(this.regionsToInnerRegions.keySet())) {
            throw new RuntimeException("The regions obtained in the raw performance do not match the region obtained" +
                    " when calculating inner regions");
        }

        for(Region region : trace) {
            if(executingRegions.isEmpty()) {
                executingRegions.push(region);

                continue;
            }

            Region top = executingRegions.peek();

            if(top.getRegionID().equals(region.getRegionID())) {
                if(top.getStartTime() != 0 && region.getEndTime() != 0) {
                    executingRegions.pop();
                }
            }
            else {
                for(Region executingRegion : executingRegions) {
                    Set<Region> innerRegions = this.regionsToInnerRegions.get(executingRegion);
                    innerRegions.add(region);
                }

                executingRegions.push(region);
            }
        }

        if(!executingRegions.isEmpty()) {
            throw new RuntimeException("There were executing regions left that were not processed");
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
    public void writeToFile(PerformanceEntry performanceEntry) throws IOException {
        // TODO
        throw new RuntimeException();
    }

    @Override
    public PerformanceEntry readFromFile(File file) throws IOException {
        // TODO
        throw new RuntimeException();
    }

}

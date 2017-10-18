package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

// TODO use the interface instead of the performance entry class
public interface Executor {

    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException, InterruptedException;

    public Set<PerformanceEntryStatistic> execute(String[] args) throws IOException, InterruptedException;

    public Set<PerformanceEntryStatistic> execute() throws IOException, InterruptedException;

    //    public void writeToFile(String iteration, Set<String> configuration, List<Region> executedRegions) throws IOException;
//    public void writeToFile(String iteration, Set<String> configuration, Map<RegionID, Long> regionsToProcessedPerformance) throws IOException;
    public void writeToFile(String iteration, Set<String> configuration, Map<String, Long> regionsToProcessedPerformance) throws IOException;

    public DefaultPerformanceEntry readFromFile(File file) throws IOException;

    public Set<String> getOptions(File file) throws IOException;
}

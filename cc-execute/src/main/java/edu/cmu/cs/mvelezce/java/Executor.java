package edu.cmu.cs.mvelezce.java;

import java.io.IOException;

public interface Executor {

  Object execute(String[] args) throws IOException, InterruptedException;

  Object execute(int iterations) throws InterruptedException, IOException;

  Object executeIteration(int iteration) throws InterruptedException, IOException;

  void executeProgram(String programClassPath, String mainClass, String[] configuration)
      throws InterruptedException, IOException;

  String outputDir();

  //    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException,
  // InterruptedException;
  //
  //    public Set<PerformanceEntryStatistic> execute(String[] args) throws IOException,
  // InterruptedException;
  //
  //    public Set<PerformanceEntryStatistic> execute() throws IOException, InterruptedException;
  //
  //    //    public void writeToFile(String iteration, Set<String> configuration, List<Region>
  // executedRegions) throws IOException;
  ////    public void writeToFile(String iteration, Set<String> configuration, Map<RegionID, Long>
  // regionsToProcessedPerformance) throws IOException;
  //    public void writeToFile(String iteration, Set<String> configuration, Map<String, Long>
  // regionsToProcessedPerformance) throws IOException;
  //
  //    public DefaultPerformanceEntry readFromFile(File file) throws IOException;
  //
  //    public Set<String> getOptions(File file) throws IOException;
  //
  //    public String getOutputDir();
  //
  //    void writeToFile(T results) throws IOException;
  //
  //    T readFromFile(File file) throws IOException;
}

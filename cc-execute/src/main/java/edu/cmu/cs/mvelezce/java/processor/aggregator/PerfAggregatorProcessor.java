package edu.cmu.cs.mvelezce.java.processor.aggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class PerfAggregatorProcessor implements Analysis<Set<PerformanceEntry>> {

  private final Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution;
  private final String outputDir;

  public PerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    this.itersToProcessedPerfExecution = itersToProcessedPerfExecution;

    this.outputDir = this.outputDir() + "/" + programName + "/execution/averaged";
  }

  @Override
  public Set<PerformanceEntry> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    File file = new File(this.outputDir);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      throw new UnsupportedOperationException("implement");
    }

    Set<PerformanceEntry> performanceEntries = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(performanceEntries);
    }

    return performanceEntries;
  }

  @Override
  public Set<PerformanceEntry> analyze() {
    Set<PerformanceEntry> perfEntries = new HashSet<>();
    Set<Set<String>> configs = this.getConfigs();

    for (Set<String> config : configs) {
      PerformanceEntry performanceEntry = this.averageExecs(config);
      perfEntries.add(performanceEntry);
    }

    return perfEntries;
  }

  private PerformanceEntry averageExecs(Set<String> config) {
    Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions =
        this.itersToProcessedPerfExecution.values();

    Map<String, Long> regionsToPerf = this.addRegions(config, allProcessedPerfExecutions);
    this.addAllExecutions(regionsToPerf, config, allProcessedPerfExecutions);

    for (Map.Entry<String, Long> entry : regionsToPerf.entrySet()) {
      long totalTime = entry.getValue();
      regionsToPerf.put(entry.getKey(), totalTime / this.itersToProcessedPerfExecution.size());
    }

    return new PerformanceEntry(config, regionsToPerf);
  }

  private void addAllExecutions(
      Map<String, Long> regionsToPerf,
      Set<String> config,
      Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (Map.Entry<String, Long> entry : processedPerfExecution.getRegionsToPerf().entrySet()) {
          String region = entry.getKey();
          long currentTime = regionsToPerf.get(region);
          currentTime += entry.getValue();
          regionsToPerf.put(region, currentTime);
        }

        break;
      }
    }
  }

  private Map<String, Long> addRegions(
      Set<String> config, Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    Map<String, Long> regionsToPerf = new HashMap<>();

    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (String region : processedPerfExecution.getRegionsToPerf().keySet()) {
          regionsToPerf.putIfAbsent(region, 0L);
        }

        break;
      }
    }

    return regionsToPerf;
  }

  private Set<Set<String>> getConfigs() {
    Set<Set<String>> configs = new HashSet<>();
    Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions =
        this.itersToProcessedPerfExecution.values();

    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {

      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        configs.add(processedPerfExecution.getConfiguration());
      }
    }

    return configs;
  }

  @Override
  public void writeToFile(Set<PerformanceEntry> results) throws IOException {
    for (PerformanceEntry performanceEntry : results) {
      String outputFile = this.outputDir + "/" + UUID.randomUUID() + Options.DOT_JSON;
      File file = new File(outputFile);
      file.getParentFile().mkdirs();

      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(file, performanceEntry);
    }
  }

  @Override
  public Set<PerformanceEntry> readFromFile(File file) {
    throw new UnsupportedOperationException("implement");
  }
}

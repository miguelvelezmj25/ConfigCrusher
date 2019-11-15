package edu.cmu.cs.mvelezce.java.processor.aggregator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public abstract class PerfAggregatorProcessor implements Analysis<Set<PerformanceEntry>> {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0000000");

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
      return this.readFromFile(file);
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
      System.out.println("Config " + config);
      PerformanceEntry performanceEntry = this.averageExecs(config);
      perfEntries.add(performanceEntry);
    }

    return perfEntries;
  }

  private PerformanceEntry averageExecs(Set<String> config) {
    Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions =
        this.itersToProcessedPerfExecution.values();

    Map<UUID, Long> regionsToPerf = this.addRegions(config, allProcessedPerfExecutions, 0L);
    this.addAllExecutions(regionsToPerf, config, allProcessedPerfExecutions);

    for (Map.Entry<UUID, Long> entry : regionsToPerf.entrySet()) {
      long totalTime = entry.getValue();
      regionsToPerf.put(entry.getKey(), totalTime / this.itersToProcessedPerfExecution.size());
    }

    Map<UUID, Long> regionsToMin = this.getRegionsToMin(config, allProcessedPerfExecutions);
    Map<UUID, Long> regionsToMax = this.getRegionsToMax(config, allProcessedPerfExecutions);
    Map<UUID, Long> regionsToDiff = this.getRegionsToDiff(regionsToMin, regionsToMax);

    return new PerformanceEntry(
        config,
        regionsToPerf,
        regionsToMin,
        regionsToMax,
        regionsToDiff,
        this.toHumanReadable(regionsToPerf),
        this.toHumanReadable(regionsToMin),
        this.toHumanReadable(regionsToMax),
        this.toHumanReadable(regionsToDiff));
  }

  private Map<UUID, String> toHumanReadable(Map<UUID, Long> regionsToData) {

    Map<UUID, String> regionsToHumanReadableData = new HashMap<>();

    for (Map.Entry<UUID, Long> entry : regionsToData.entrySet()) {
      regionsToHumanReadableData.put(entry.getKey(), entry.getValue().toString());
    }

    for (Map.Entry<UUID, String> entry : regionsToHumanReadableData.entrySet()) {
      double data = Double.parseDouble(entry.getValue());
      data = data / 1E9;
      regionsToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return regionsToHumanReadableData;
  }

  private Map<UUID, Long> getRegionsToMin(
      Set<String> config, Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    Map<UUID, Long> regionsToMin =
        this.addRegions(config, allProcessedPerfExecutions, Long.MAX_VALUE);

    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (Map.Entry<String, Long> entry : processedPerfExecution.getRegionsToPerf().entrySet()) {
          UUID region = UUID.fromString(entry.getKey());
          long currentMin = regionsToMin.get(region);
          currentMin = Math.min(currentMin, entry.getValue());
          regionsToMin.put(region, currentMin);
        }

        break;
      }
    }

    return regionsToMin;
  }

  private Map<UUID, Long> getRegionsToMax(
      Set<String> config, Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    Map<UUID, Long> regionsToMax =
        this.addRegions(config, allProcessedPerfExecutions, Long.MIN_VALUE);

    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (Map.Entry<String, Long> entry : processedPerfExecution.getRegionsToPerf().entrySet()) {
          UUID region = UUID.fromString(entry.getKey());
          long currentMax = regionsToMax.get(region);
          currentMax = Math.max(currentMax, entry.getValue());
          regionsToMax.put(region, currentMax);
        }

        break;
      }
    }

    return regionsToMax;
  }

  private Map<UUID, Long> getRegionsToDiff(
      Map<UUID, Long> regionsToMin, Map<UUID, Long> regionsToMax) {
    Map<UUID, Long> regionsToDiff = new HashMap<>();

    for (UUID region : regionsToMin.keySet()) {
      regionsToDiff.put(region, 0L);
    }

    for (UUID region : regionsToDiff.keySet()) {
      long max = regionsToMax.get(region);
      long min = regionsToMin.get(region);
      long diff = max - min;

      if (diff >= 1E9) {
        System.err.println(
            "The difference between the min and max executions of region "
                + region
                + " is greater than 1 sec. It is "
                + (diff / 1E9));
      }

      regionsToDiff.put(region, diff);
    }

    return regionsToDiff;
  }

  private void addAllExecutions(
      Map<UUID, Long> regionsToPerf,
      Set<String> config,
      Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (Map.Entry<String, Long> entry : processedPerfExecution.getRegionsToPerf().entrySet()) {
          UUID region = UUID.fromString(entry.getKey());
          long currentTime = regionsToPerf.get(region);
          currentTime += entry.getValue();
          regionsToPerf.put(region, currentTime);
        }

        break;
      }
    }
  }

  private Map<UUID, Long> addRegions(
      Set<String> config,
      Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions,
      long defaultValue) {
    Map<UUID, Long> regionsToPerf = new HashMap<>();

    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (String region : processedPerfExecution.getRegionsToPerf().keySet()) {
          regionsToPerf.putIfAbsent(UUID.fromString(region), defaultValue);
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
  public Set<PerformanceEntry> readFromFile(File dir) throws IOException {
    Collection<File> files = FileUtils.listFiles(dir, new String[] {"json"}, false);
    Set<PerformanceEntry> entries = new HashSet<>();

    for (File file : files) {
      ObjectMapper mapper = new ObjectMapper();
      PerformanceEntry performanceEntry =
          mapper.readValue(file, new TypeReference<PerformanceEntry>() {});
      entries.add(performanceEntry);
    }

    return entries;
  }
}

package edu.cmu.cs.mvelezce.java.processor.aggregator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

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

    Map<UUID, SummaryStatistics> regionsToStats =
        this.addRegions(config, allProcessedPerfExecutions);
    this.addAllExecutions(regionsToStats, config, allProcessedPerfExecutions);

    Map<UUID, Double> regionsToPerf = this.getRegionsToPerf(regionsToStats);
    Map<UUID, Double> regionsToMin = this.getRegionsToMin(regionsToStats);
    Map<UUID, Double> regionsToMax = this.getRegionsToMax(regionsToStats);
    Map<UUID, Double> regionsToDiff = this.getRegionsToDiff(regionsToMin, regionsToMax);
    Map<UUID, Double> regionsToSampleVariance = this.getRegionsToSampleVariance(regionsToStats);
    Map<UUID, List<Double>> regionsToConfidenceInterval =
        this.getRegionsToConfidenceInterval(regionsToStats);

    return new PerformanceEntry(
        config,
        regionsToPerf,
        regionsToMin,
        regionsToMax,
        regionsToDiff,
        regionsToSampleVariance,
        regionsToConfidenceInterval,
        this.toHumanReadable(regionsToPerf),
        this.toHumanReadable(regionsToMin),
        this.toHumanReadable(regionsToMax),
        this.toHumanReadable(regionsToDiff),
        this.toHumanReadable(regionsToSampleVariance),
        this.toHumanReadableCI(regionsToConfidenceInterval));
  }

  private Map<UUID, List<Double>> getRegionsToConfidenceInterval(
      Map<UUID, SummaryStatistics> regionsToStats) {
    Map<UUID, List<Double>> regionsToConfidenceInterval = new HashMap<>();

    for (Map.Entry<UUID, SummaryStatistics> entry : regionsToStats.entrySet()) {
      SummaryStatistics stats = entry.getValue();
      TDistribution tDist = new TDistribution(stats.getN() - 1);
      double critVal = tDist.inverseCumulativeProbability(1.0 - (1 - 0.95) / 2);
      double ciValue = critVal * Math.sqrt(stats.getVariance()) / Math.sqrt(stats.getN());
      double lowerCI = Math.max(0, stats.getMean() - ciValue);
      double higherCI = stats.getMean() + ciValue;

      if ((higherCI - lowerCI) >= 1E9) {
        System.err.println(
            "The difference between the lower and higher confidence interval bounds of region "
                + entry.getKey()
                + " is greater than 1 sec. It is "
                + ((higherCI - lowerCI) / 1E9));
      }

      List<Double> confidenceInterval = new ArrayList<>();
      confidenceInterval.add(lowerCI);
      confidenceInterval.add(higherCI);
      regionsToConfidenceInterval.put(entry.getKey(), confidenceInterval);
    }

    return regionsToConfidenceInterval;
  }

  private Map<UUID, Double> getRegionsToSampleVariance(
      Map<UUID, SummaryStatistics> regionsToStats) {
    Map<UUID, Double> regionsToSampleVariance = new HashMap<>();

    for (Map.Entry<UUID, SummaryStatistics> entry : regionsToStats.entrySet()) {
      regionsToSampleVariance.put(entry.getKey(), entry.getValue().getVariance());
    }

    return regionsToSampleVariance;
  }

  private Map<UUID, String> toHumanReadable(Map<UUID, Double> regionsToData) {
    Map<UUID, String> regionsToHumanReadableData = new HashMap<>();

    for (Map.Entry<UUID, Double> entry : regionsToData.entrySet()) {
      double data = entry.getValue();
      data = data / 1E9;
      regionsToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return regionsToHumanReadableData;
  }

  private Map<UUID, List<String>> toHumanReadableCI(
      Map<UUID, List<Double>> regionsToConfidenceIntervals) {
    Map<UUID, List<String>> regionsToHumanReadableCI = new HashMap<>();

    for (Map.Entry<UUID, List<Double>> entry : regionsToConfidenceIntervals.entrySet()) {
      List<Double> confidenceInterval = entry.getValue();
      double lower = confidenceInterval.get(0) / 1E9;
      double higher = confidenceInterval.get(1) / 1E9;
      List<String> confidenceIntervalHumanReadable = new ArrayList<>();
      confidenceIntervalHumanReadable.add(DECIMAL_FORMAT.format(lower));
      confidenceIntervalHumanReadable.add(DECIMAL_FORMAT.format(higher));
      regionsToHumanReadableCI.put(entry.getKey(), confidenceIntervalHumanReadable);
    }

    return regionsToHumanReadableCI;
  }

  private Map<UUID, Double> getRegionsToPerf(Map<UUID, SummaryStatistics> regionsToStats) {
    Map<UUID, Double> regionsToPerf = new HashMap<>();

    for (Map.Entry<UUID, SummaryStatistics> entry : regionsToStats.entrySet()) {
      regionsToPerf.put(entry.getKey(), entry.getValue().getMean());
    }

    return regionsToPerf;
  }

  private Map<UUID, Double> getRegionsToMin(Map<UUID, SummaryStatistics> regionsToStats) {
    Map<UUID, Double> regionsToMin = new HashMap<>();

    for (Map.Entry<UUID, SummaryStatistics> entry : regionsToStats.entrySet()) {
      regionsToMin.put(entry.getKey(), entry.getValue().getMin());
    }

    return regionsToMin;
  }

  private Map<UUID, Double> getRegionsToMax(Map<UUID, SummaryStatistics> regionsToStats) {
    Map<UUID, Double> regionsToMax = new HashMap<>();

    for (Map.Entry<UUID, SummaryStatistics> entry : regionsToStats.entrySet()) {
      regionsToMax.put(entry.getKey(), entry.getValue().getMax());
    }

    return regionsToMax;
  }

  private Map<UUID, Double> getRegionsToDiff(
      Map<UUID, Double> regionsToMin, Map<UUID, Double> regionsToMax) {
    Map<UUID, Double> regionsToDiff = new HashMap<>();

    for (UUID region : regionsToMin.keySet()) {
      regionsToDiff.put(region, 0.0);
    }

    for (UUID region : regionsToDiff.keySet()) {
      double max = regionsToMax.get(region);
      double min = regionsToMin.get(region);
      double diff = max - min;

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
      Map<UUID, SummaryStatistics> regionsToStats,
      Set<String> config,
      Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (Map.Entry<String, Long> entry : processedPerfExecution.getRegionsToPerf().entrySet()) {
          UUID region = UUID.fromString(entry.getKey());
          SummaryStatistics stats = regionsToStats.get(region);
          stats.addValue(entry.getValue());
        }

        break;
      }
    }
  }

  private Map<UUID, SummaryStatistics> addRegions(
      Set<String> config, Collection<Set<ProcessedPerfExecution>> allProcessedPerfExecutions) {
    Map<UUID, SummaryStatistics> regionsToPerf = new HashMap<>();

    for (Set<ProcessedPerfExecution> processedPerfExecutions : allProcessedPerfExecutions) {
      for (ProcessedPerfExecution processedPerfExecution : processedPerfExecutions) {
        if (!processedPerfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (String region : processedPerfExecution.getRegionsToPerf().keySet()) {
          regionsToPerf.putIfAbsent(UUID.fromString(region), new SummaryStatistics());
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

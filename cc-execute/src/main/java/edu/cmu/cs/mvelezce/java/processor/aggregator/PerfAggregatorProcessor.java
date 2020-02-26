package edu.cmu.cs.mvelezce.java.processor.aggregator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;
import edu.cmu.cs.mvelezce.utils.stats.DescriptiveStatisticsMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public abstract class PerfAggregatorProcessor implements Analysis<Set<PerformanceEntry>> {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0000000");

  private final Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution;
  private final String outputDir;

  public PerfAggregatorProcessor(
      String programName, Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution) {
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
    Collection<Set<PerfExecution>> allProcessedPerfExecutions =
        this.itersToProcessedPerfExecution.values();

    DescriptiveStatisticsMap<UUID> regionsToStats =
        this.addRegions(config, allProcessedPerfExecutions);
    this.addAllExecutions(regionsToStats, config, allProcessedPerfExecutions);

    Map<UUID, Double> regionsToPerf = regionsToStats.getEntriesToData();
    Map<UUID, Double> regionsToMin = regionsToStats.getEntriesToMin();
    Map<UUID, Double> regionsToMax = regionsToStats.getEntriesToMax();
    Map<UUID, Double> regionsToDiff = regionsToStats.getEntriesToDiff(regionsToMin, regionsToMax);
    Map<UUID, Double> regionsToSampleVariance = regionsToStats.getEntriesToSampleVariance();
    Map<UUID, List<Double>> regionsToConfidenceInterval =
        regionsToStats.getEntriesToConfidenceInterval();
    Map<UUID, Double> regionsToCoefficientsOfVariation =
        regionsToStats.getCoefficientsOfVariation();

    return new PerformanceEntry(
        config,
        regionsToPerf,
        regionsToMin,
        regionsToMax,
        regionsToDiff,
        regionsToSampleVariance,
        regionsToConfidenceInterval,
        regionsToCoefficientsOfVariation,
        this.toHumanReadable(regionsToPerf),
        this.toHumanReadable(regionsToMin),
        this.toHumanReadable(regionsToMax),
        this.toHumanReadable(regionsToDiff),
        this.toHumanReadableSampleVariance(regionsToSampleVariance),
        this.toHumanReadableCI(regionsToConfidenceInterval),
        this.toHumanReadableCoefficientOfVariation(regionsToCoefficientsOfVariation));
  }

  private Map<UUID, String> toHumanReadableCoefficientOfVariation(
      Map<UUID, Double> regionsToCoefficientsOfVariation) {
    Map<UUID, String> regionsToHumanReadableData = new HashMap<>();

    for (Map.Entry<UUID, Double> entry : regionsToCoefficientsOfVariation.entrySet()) {
      double data = entry.getValue();
      regionsToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return regionsToHumanReadableData;
  }

  private Map<UUID, String> toHumanReadableSampleVariance(
      Map<UUID, Double> regionsToSampleVariance) {
    Map<UUID, String> regionsToHumanReadableData = new HashMap<>();

    for (Map.Entry<UUID, Double> entry : regionsToSampleVariance.entrySet()) {
      double data = entry.getValue();
      data = data / 1E18;
      regionsToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return regionsToHumanReadableData;
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

  private void addAllExecutions(
      DescriptiveStatisticsMap<UUID> regionsToStats,
      Set<String> config,
      Collection<Set<PerfExecution>> allProcessedPerfExecutions) {
    for (Set<PerfExecution> perfExecutions : allProcessedPerfExecutions) {
      for (PerfExecution perfExecution : perfExecutions) {
        if (!perfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (Map.Entry<String, Long> entry : perfExecution.getRegionsToPerf().entrySet()) {
          UUID region = UUID.fromString(entry.getKey());
          DescriptiveStatistics stats = regionsToStats.get(region);
          stats.addValue(entry.getValue());
        }

        break;
      }
    }
  }

  private DescriptiveStatisticsMap<UUID> addRegions(
      Set<String> config, Collection<Set<PerfExecution>> allProcessedPerfExecutions) {
    DescriptiveStatisticsMap<UUID> regionsToPerf = new DescriptiveStatisticsMap<>();

    for (Set<PerfExecution> perfExecutions : allProcessedPerfExecutions) {
      for (PerfExecution perfExecution : perfExecutions) {
        if (!perfExecution.getConfiguration().equals(config)) {
          continue;
        }

        for (String region : perfExecution.getRegionsToPerf().keySet()) {
          regionsToPerf.putIfAbsent(UUID.fromString(region));
        }

        break;
      }
    }

    return regionsToPerf;
  }

  private Set<Set<String>> getConfigs() {
    Set<Set<String>> configs = new HashSet<>();
    Collection<Set<PerfExecution>> allProcessedPerfExecutions =
        this.itersToProcessedPerfExecution.values();

    for (Set<PerfExecution> perfExecutions : allProcessedPerfExecutions) {

      for (PerfExecution perfExecution : perfExecutions) {
        configs.add(perfExecution.getConfiguration());
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

package edu.cmu.cs.mvelezce.blackbox.processor.aggregator.fw;

import edu.cmu.cs.mvelezce.blackbox.execute.fw.FeatureWiseExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeatureWisePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = FeatureWiseExecutor.OUTPUT_DIR;

  public FeatureWisePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  FeatureWisePerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

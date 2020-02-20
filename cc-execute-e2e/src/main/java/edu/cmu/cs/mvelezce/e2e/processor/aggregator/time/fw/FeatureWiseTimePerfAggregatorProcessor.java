package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.fw;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.fw.FeatureWiseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeatureWiseTimePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = FeatureWiseInstrumentExecutor.OUTPUT_DIR;

  public FeatureWiseTimePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  FeatureWiseTimePerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

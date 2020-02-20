package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.fw;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.fw.FeatureWiseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeatureWiseInstrumentPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = FeatureWiseInstrumentExecutor.OUTPUT_DIR;

  public FeatureWiseInstrumentPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  FeatureWiseInstrumentPerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

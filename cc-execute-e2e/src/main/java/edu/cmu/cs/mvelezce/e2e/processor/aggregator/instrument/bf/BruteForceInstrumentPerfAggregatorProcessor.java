package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.bf;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.bf.BruteForceInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BruteForceInstrumentPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = BruteForceInstrumentExecutor.OUTPUT_DIR;

  public BruteForceInstrumentPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  BruteForceInstrumentPerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

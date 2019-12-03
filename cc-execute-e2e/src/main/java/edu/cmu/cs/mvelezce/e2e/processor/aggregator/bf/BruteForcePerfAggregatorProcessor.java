package edu.cmu.cs.mvelezce.e2e.processor.aggregator.bf;

import edu.cmu.cs.mvelezce.e2e.execute.bf.BruteForceExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BruteForcePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = BruteForceExecutor.OUTPUT_DIR;

  public BruteForcePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  BruteForcePerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

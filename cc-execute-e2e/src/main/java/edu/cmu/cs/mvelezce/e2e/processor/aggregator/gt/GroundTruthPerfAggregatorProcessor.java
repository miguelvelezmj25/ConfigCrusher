package edu.cmu.cs.mvelezce.e2e.processor.aggregator.gt;

import edu.cmu.cs.mvelezce.e2e.execute.gt.GroundTruthExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroundTruthPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = GroundTruthExecutor.OUTPUT_DIR;

  public GroundTruthPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  GroundTruthPerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

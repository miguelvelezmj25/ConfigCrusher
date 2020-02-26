package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.gt;

import edu.cmu.cs.mvelezce.e2e.execute.time.gt.GroundTruthTimeExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroundTruthTimePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = GroundTruthTimeExecutor.OUTPUT_DIR;

  public GroundTruthTimePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  GroundTruthTimePerfAggregatorProcessor(
      String programName, Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

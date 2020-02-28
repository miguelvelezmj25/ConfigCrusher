package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.bf;

import edu.cmu.cs.mvelezce.e2e.execute.time.bf.BruteForceTimeExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BruteForceTimePerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = BruteForceTimeExecutor.OUTPUT_DIR;

  public BruteForceTimePerfAggregatorProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), measuredTime);
  }

  BruteForceTimePerfAggregatorProcessor(
      String programName,
      Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution,
      String measuredTime) {
    super(programName, itersToProcessedPerfExecution, measuredTime);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + super.outputDir();
  }
}

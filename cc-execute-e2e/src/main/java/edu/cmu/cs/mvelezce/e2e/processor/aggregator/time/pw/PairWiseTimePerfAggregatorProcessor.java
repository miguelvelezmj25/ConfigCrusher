package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.pw;

import edu.cmu.cs.mvelezce.e2e.execute.time.pw.PairWiseTimeExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PairWiseTimePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = PairWiseTimeExecutor.OUTPUT_DIR;

  public PairWiseTimePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  PairWiseTimePerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.e2e.processor.aggregator.pw;

import edu.cmu.cs.mvelezce.e2e.execute.pw.PairWiseExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PairWisePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = PairWiseExecutor.OUTPUT_DIR;

  public PairWisePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  PairWisePerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

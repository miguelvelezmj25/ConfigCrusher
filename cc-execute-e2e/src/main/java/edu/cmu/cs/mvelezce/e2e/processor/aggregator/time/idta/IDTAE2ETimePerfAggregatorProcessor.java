package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.idta;

import edu.cmu.cs.mvelezce.e2e.execute.time.idta.IDTAE2ETimeExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAE2ETimePerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = IDTAE2ETimeExecutor.OUTPUT_DIR;

  public IDTAE2ETimePerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  IDTAE2ETimePerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

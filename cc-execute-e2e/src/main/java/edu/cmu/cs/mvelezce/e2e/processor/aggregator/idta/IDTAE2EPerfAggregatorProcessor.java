package edu.cmu.cs.mvelezce.e2e.processor.aggregator.idta;

import edu.cmu.cs.mvelezce.e2e.execute.idta.IDTAExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAE2EPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = IDTAExecutor.OUTPUT_DIR;

  public IDTAE2EPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  IDTAE2EPerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

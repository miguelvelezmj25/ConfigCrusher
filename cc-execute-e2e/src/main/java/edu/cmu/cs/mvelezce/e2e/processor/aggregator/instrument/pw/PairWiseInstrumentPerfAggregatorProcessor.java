package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.pw;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.pw.PairWiseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PairWiseInstrumentPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = PairWiseInstrumentExecutor.OUTPUT_DIR;

  public PairWiseInstrumentPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  PairWiseInstrumentPerfAggregatorProcessor(
      String programName, Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.idta;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.idta.IDTAE2EInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAE2EInstrumentPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = IDTAE2EInstrumentExecutor.OUTPUT_DIR;

  public IDTAE2EInstrumentPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  IDTAE2EInstrumentPerfAggregatorProcessor(
      String programName, Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

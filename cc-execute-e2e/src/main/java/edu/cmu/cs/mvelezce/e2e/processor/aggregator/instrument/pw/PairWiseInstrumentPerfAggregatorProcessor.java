package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.pw;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.pw.PairWiseInstrumentExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PairWiseInstrumentPerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = PairWiseInstrumentExecutor.OUTPUT_DIR;

  public PairWiseInstrumentPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  PairWiseInstrumentPerfAggregatorProcessor(
      String programName, Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution) {
    super(programName, itersToProcessedPerfExecution, BaseExecutor.REAL);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + super.outputDir();
  }
}

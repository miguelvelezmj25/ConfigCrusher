package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.pw;

import edu.cmu.cs.mvelezce.e2e.execute.time.pw.PairWiseTimeExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PairWiseTimePerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = PairWiseTimeExecutor.OUTPUT_DIR;

  public PairWiseTimePerfAggregatorProcessor(String programName, String measuredTime) {
    super(programName, new HashMap<>(), measuredTime, 0);
  }

  PairWiseTimePerfAggregatorProcessor(
      String programName,
      Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution,
      String measuredTime,
      long addedTime) {
    super(programName, itersToProcessedPerfExecution, measuredTime, addedTime);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + super.outputDir();
  }
}

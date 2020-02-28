package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.idta;

import edu.cmu.cs.mvelezce.e2e.execute.time.idta.IDTAE2ETimeExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAE2ETimePerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = IDTAE2ETimeExecutor.OUTPUT_DIR;

  public IDTAE2ETimePerfAggregatorProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), measuredTime);
  }

  IDTAE2ETimePerfAggregatorProcessor(
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

package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.gt;

import edu.cmu.cs.mvelezce.e2e.execute.time.gt.GroundTruthTimeExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroundTruthTimePerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = GroundTruthTimeExecutor.OUTPUT_DIR;

  public GroundTruthTimePerfAggregatorProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), measuredTime, 0);
  }

  GroundTruthTimePerfAggregatorProcessor(
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

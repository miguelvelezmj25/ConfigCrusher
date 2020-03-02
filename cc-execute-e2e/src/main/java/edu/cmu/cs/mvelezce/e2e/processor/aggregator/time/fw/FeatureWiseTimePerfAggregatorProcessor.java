package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.fw;

import edu.cmu.cs.mvelezce.e2e.execute.time.fw.FeatureWiseTimeExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeatureWiseTimePerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = FeatureWiseTimeExecutor.OUTPUT_DIR;

  public FeatureWiseTimePerfAggregatorProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), measuredTime, 0);
  }

  FeatureWiseTimePerfAggregatorProcessor(
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

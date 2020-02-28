package edu.cmu.cs.mvelezce.e2e.processor.aggregator;

import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.Map;
import java.util.Set;

public abstract class E2EPerfAggregatorProcessor extends PerfAggregatorProcessor {

  public E2EPerfAggregatorProcessor(
      String programName,
      Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution,
      String measuredTime) {
    super(programName, itersToProcessedPerfExecution, measuredTime);
  }

  @Override
  public String outputDir() {
    return this.getProgramName()
        + "/"
        + this.getMeasuredTime()
        + PerfAggregatorProcessor.OUTPUT_DIR;
  }
}

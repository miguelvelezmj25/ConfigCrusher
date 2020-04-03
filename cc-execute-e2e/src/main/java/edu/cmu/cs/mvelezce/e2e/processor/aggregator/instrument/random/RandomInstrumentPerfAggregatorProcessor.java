package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.random;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.random.RandomInstrumentExecutor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.E2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RandomInstrumentPerfAggregatorProcessor extends E2EPerfAggregatorProcessor {

  private static final String OUTPUT_DIR = RandomInstrumentExecutor.OUTPUT_DIR;

  public RandomInstrumentPerfAggregatorProcessor(String programName) {
    this(programName, new HashMap<>(), 0);
  }

  RandomInstrumentPerfAggregatorProcessor(
      String programName,
      Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution,
      long addedTime) {
    super(programName, itersToProcessedPerfExecution, BaseExecutor.REAL, addedTime);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + super.outputDir();
  }
}

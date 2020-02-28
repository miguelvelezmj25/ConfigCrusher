package edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = IDTAJProfilerSamplingExecutor.OUTPUT_DIR;

  public IDTAPerfAggregatorProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), measuredTime);
  }

  IDTAPerfAggregatorProcessor(
      String programName,
      Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution,
      String measuredTime) {
    super(programName, itersToProcessedPerfExecution, measuredTime);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR
        + "/"
        + this.getMeasuredTime()
        + "/"
        + this.getProgramName()
        + PerfAggregatorProcessor.OUTPUT_DIR;
  }
}

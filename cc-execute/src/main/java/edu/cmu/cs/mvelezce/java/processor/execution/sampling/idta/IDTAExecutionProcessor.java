package edu.cmu.cs.mvelezce.java.processor.execution.sampling.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAExecutor;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.ExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawPerfExecution;

import java.util.Map;
import java.util.Set;

public class IDTAExecutionProcessor extends ExecutionProcessor {

  private static final String OUTPUT_DIR = IDTAExecutor.OUTPUT_DIR;

  public IDTAExecutionProcessor(
      String programName,
      Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecs,
      Set<JavaRegion> regions) {
    super(programName, itersToRawPerfExecs, regions);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.JProfilerSamplingExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawJProfilerSamplingPerfExecution;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTAJProfilerSamplingExecutionProcessor extends JProfilerSamplingExecutionProcessor {

  private static final String OUTPUT_DIR = IDTAJProfilerSamplingExecutor.OUTPUT_DIR;

  public IDTAJProfilerSamplingExecutionProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), new HashSet<>(), measuredTime);
  }

  IDTAJProfilerSamplingExecutionProcessor(
      String programName,
      Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs,
      Set<JavaRegion> regions,
      String measuredTime) {
    super(programName, itersToRawPerfExecs, regions, measuredTime);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.java.processor.execution.sampling.idta;

import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAExecutor;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.ExecutionProcessor;

public class IDTAExecutionProcessor extends ExecutionProcessor {

  private static final String OUTPUT_DIR = IDTAExecutor.OUTPUT_DIR;

  public IDTAExecutionProcessor(String programName) {
    super(programName);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

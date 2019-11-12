package edu.cmu.cs.mvelezce.java.processor.execution.idta;

import edu.cmu.cs.mvelezce.java.execute.instrumentation.idta.IDTAExecutor;
import edu.cmu.cs.mvelezce.java.processor.execution.ExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.raw.RawPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAExecutionProcessor extends ExecutionProcessor {

  private static final String OUTPUT_DIR = IDTAExecutor.OUTPUT_DIR;

  public IDTAExecutionProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  IDTAExecutionProcessor(
      String programName, Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions) {
    super(programName, itersToRawPerfExecutions);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

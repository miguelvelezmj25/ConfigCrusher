package edu.cmu.cs.mvelezce.java.processor.execution.instrumentation.idta;

import edu.cmu.cs.mvelezce.java.execute.instrumentation.idta.IDTAInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.processor.execution.instrumentation.InstrumentExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.instrumentation.raw.RawInstrumentPerfExecution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IDTAInstrumentExecutionProcessor extends InstrumentExecutionProcessor {

  private static final String OUTPUT_DIR = IDTAInstrumentExecutor.OUTPUT_DIR;

  public IDTAInstrumentExecutionProcessor(String programName) {
    this(programName, new HashMap<>());
  }

  IDTAInstrumentExecutionProcessor(
      String programName, Map<Integer, Set<RawInstrumentPerfExecution>> itersToRawPerfExecutions) {
    super(programName, itersToRawPerfExecutions);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

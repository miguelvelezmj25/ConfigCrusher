package edu.cmu.cs.mvelezce.e2e.execute.instrument.idta;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.E2EInstrumentExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.instrument.parser.E2EInstrumentExecutionParser;

import java.util.HashSet;
import java.util.Set;

public class IDTAE2EInstrumentExecutor extends E2EInstrumentExecutor {

  public static final String OUTPUT_DIR = E2EInstrumentExecutor.OUTPUT_DIR + "/java/programs/idta";

  public IDTAE2EInstrumentExecutor(String programName) {
    this(programName, new HashSet<>(), -1);
  }

  IDTAE2EInstrumentExecutor(
      String programName, Set<Set<String>> configurations, int waitAfterExecution) {
    super(
        programName,
        configurations,
        new E2EInstrumentExecutionParser(programName, OUTPUT_DIR),
        waitAfterExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.e2e.execute.instrument.fw;

import edu.cmu.cs.mvelezce.e2e.execute.instrument.E2EInstrumentExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.instrument.parser.E2EInstrumentExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class FeatureWiseInstrumentExecutor extends E2EInstrumentExecutor {

  public static final String OUTPUT_DIR = E2EInstrumentExecutor.OUTPUT_DIR + "/java/programs/fw";

  public FeatureWiseInstrumentExecutor(String programName) {
    this(programName, new HashSet<>(), -1);
  }

  FeatureWiseInstrumentExecutor(String programName, Set<Set<String>> configurations, int waitAfterExecution) {
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

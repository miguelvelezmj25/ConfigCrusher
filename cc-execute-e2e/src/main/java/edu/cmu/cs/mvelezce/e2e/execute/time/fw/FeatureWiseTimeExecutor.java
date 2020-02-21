package edu.cmu.cs.mvelezce.e2e.execute.time.fw;

import edu.cmu.cs.mvelezce.e2e.execute.time.E2ETimeExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.E2ETimeExecutionParser;

import java.util.HashSet;
import java.util.Set;

public class FeatureWiseTimeExecutor extends E2ETimeExecutor {

  public static final String OUTPUT_DIR = E2ETimeExecutor.OUTPUT_DIR + "/java/programs/fw";

  public FeatureWiseTimeExecutor(String programName) {
    this(programName, new HashSet<>(), -1);
  }

  FeatureWiseTimeExecutor(
      String programName, Set<Set<String>> configurations, int waitAfterExecution) {
    super(
        programName,
        configurations,
        new E2ETimeExecutionParser(programName, OUTPUT_DIR),
        waitAfterExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

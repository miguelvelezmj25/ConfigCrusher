package edu.cmu.cs.mvelezce.e2e.execute.idta;

import edu.cmu.cs.mvelezce.e2e.execute.E2EExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.parser.E2EExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class IDTAExecutor extends E2EExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute-e2e/" + Options.DIRECTORY + "/execute/java/programs/idta";

  public IDTAExecutor(String programName) {
    this(programName, new HashSet<>(), -1);
  }

  IDTAExecutor(String programName, Set<Set<String>> configurations, int waitAfterExecution) {
    super(
        programName,
        configurations,
        new E2EExecutionParser(programName, OUTPUT_DIR),
        waitAfterExecution);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.java.execute.instrumentation.idta;

import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseExecutor;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class IDTAExecutor extends BaseExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute/" + Options.DIRECTORY + "/executor/java/idta/programs";

  public IDTAExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  IDTAExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

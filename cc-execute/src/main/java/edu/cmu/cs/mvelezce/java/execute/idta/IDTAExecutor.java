package edu.cmu.cs.mvelezce.java.execute.idta;

import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.Set;

public class IDTAExecutor extends BaseExecutor {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/executor/java/idta/programs";

  IDTAExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

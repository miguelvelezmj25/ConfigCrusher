package edu.cmu.cs.mvelezce.blackbox.execute.bf;

import edu.cmu.cs.mvelezce.blackbox.execute.BlackBoxExecutor;
import edu.cmu.cs.mvelezce.blackbox.execute.parser.BlackBoxExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class BruteForceExecutor extends BlackBoxExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute-blackbox/" + Options.DIRECTORY + "/execute/java/programs/bf";

  public BruteForceExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  BruteForceExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new BlackBoxExecutionParser(programName, OUTPUT_DIR));
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

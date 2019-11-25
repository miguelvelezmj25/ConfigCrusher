package edu.cmu.cs.mvelezce.blackbox.execute.gt;

import edu.cmu.cs.mvelezce.blackbox.execute.BlackBoxExecutor;
import edu.cmu.cs.mvelezce.blackbox.execute.parser.BlackBoxExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class GroundTruthExecutor extends BlackBoxExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute-blackbox/" + Options.DIRECTORY + "/execute/java/programs/gt";

  public GroundTruthExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  GroundTruthExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new BlackBoxExecutionParser(programName, OUTPUT_DIR));
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

package edu.cmu.cs.mvelezce.eval.java.blackbox.execute.bf;

import edu.cmu.cs.mvelezce.eval.java.blackbox.execute.BlackBoxExecutor;
import edu.cmu.cs.mvelezce.eval.java.blackbox.execute.parser.BlackBoxExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.Set;

public class BruteForceExecutor extends BlackBoxExecutor {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/bf";

  BruteForceExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new BlackBoxExecutionParser(programName, OUTPUT_DIR));
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

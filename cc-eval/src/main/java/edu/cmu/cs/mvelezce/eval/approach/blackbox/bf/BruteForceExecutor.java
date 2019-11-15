package edu.cmu.cs.mvelezce.eval.approach.blackbox.bf;

import edu.cmu.cs.mvelezce.eval.approach.blackbox.BlackBoxExecutor;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.Set;

public class BruteForceExecutor extends BlackBoxExecutor {

  public static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/idta/programs/bf";

  BruteForceExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

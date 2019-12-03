package edu.cmu.cs.mvelezce.blackbox.execute.fw;

import edu.cmu.cs.mvelezce.blackbox.execute.BlackBoxExecutor;
import edu.cmu.cs.mvelezce.blackbox.execute.parser.BlackBoxExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeatureWiseExecutor extends BlackBoxExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute-blackbox/" + Options.DIRECTORY + "/execute/java/programs/fw";

  public FeatureWiseExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  FeatureWiseExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new BlackBoxExecutionParser(programName, OUTPUT_DIR));
  }

  static Set<Set<String>> getConfigs(List<String> options) {
    Set<Set<String>> configs = new HashSet<>();

    for (String option : options) {
      Set<String> config = new HashSet<>();
      config.add(option);
      configs.add(config);
    }

    return configs;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

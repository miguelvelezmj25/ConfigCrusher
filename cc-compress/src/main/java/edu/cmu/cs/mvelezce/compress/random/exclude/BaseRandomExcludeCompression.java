package edu.cmu.cs.mvelezce.compress.random.exclude;

import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.List;
import java.util.Set;

public abstract class BaseRandomExcludeCompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/" + Options.DIRECTORY + "/compression/java/programs/random/exclude";

  private final Set<Set<String>> configsToExclude;

  public BaseRandomExcludeCompression(
      String programName, List<String> options, Set<Set<String>> configsToExclude) {
    super(programName, options);

    this.configsToExclude = configsToExclude;
  }

  public static String getOutputDir() {
    return OUTPUT_DIR;
  }

  public Set<Set<String>> getConfigsToExclude() {
    return configsToExclude;
  }
}

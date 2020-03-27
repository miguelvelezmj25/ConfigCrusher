package edu.cmu.cs.mvelezce.compress.random.exclude;

import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;

import java.util.List;
import java.util.Set;

public class NumberRandomExcludeCompression extends BaseRandomExcludeCompression {

  private final int size;

  public NumberRandomExcludeCompression(
      String programName, List<String> options, Set<Set<String>> configsToExclude, int size) {
    super(programName, options, configsToExclude);

    this.size = size;
  }

  @Override
  public Set<Set<String>> analyze() {
    return ConfigHelper.getRandomConfigs(this.getOptions(), this.size, this.getConfigsToExclude());
  }

  @Override
  public String outputDir() {
    return BaseRandomExcludeCompression.getOutputDir()
        + "/"
        + this.getProgramName()
        + "/"
        + this.size;
  }
}

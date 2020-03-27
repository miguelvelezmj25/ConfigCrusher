package edu.cmu.cs.mvelezce.compress.random.exclude;

import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumberRandomExcludeCompression extends BaseRandomExcludeCompression {

  private final int numberOfConfigs;

  public NumberRandomExcludeCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>(), -1);
  }

  public NumberRandomExcludeCompression(
      String programName,
      List<String> options,
      Set<Set<String>> configsToExclude,
      int numberOfConfigs) {
    super(programName, options, configsToExclude);

    this.numberOfConfigs = numberOfConfigs;
  }

  @Override
  public Set<Set<String>> analyze() {
    return ConfigHelper.getRandomConfigs(
        this.getOptions(), this.numberOfConfigs, this.getConfigsToExclude());
  }

  @Override
  public String outputDir() {
    return BaseRandomExcludeCompression.getOutputDir() + "/" + this.getProgramName();
  }
}

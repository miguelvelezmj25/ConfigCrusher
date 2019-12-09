package edu.cmu.cs.mvelezce.compress.gt;

import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.utils.config.Options;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GTCompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/" + Options.DIRECTORY + "/compression/java/programs/gt";
  private static final int ALL_CONFIGS = -1;

  private final int configNumber;

  public GTCompression(String programName) {
    this(programName, new ArrayList<>(), 0);
  }

  public GTCompression(String programName, List<String> options) {
    this(programName, options, ALL_CONFIGS);
  }

  public GTCompression(String programName, List<String> options, int configNumber) {
    super(programName, options);

    this.configNumber = configNumber;
  }

  @Override
  public Set<Set<String>> analyze() {
    if (this.configNumber == ALL_CONFIGS) {
      return ConfigHelper.getConfigurations(this.getOptions());
    }

    return ConfigHelper.getRandomConfigs(this.getOptions(), this.configNumber);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

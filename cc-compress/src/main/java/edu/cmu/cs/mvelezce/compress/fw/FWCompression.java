package edu.cmu.cs.mvelezce.compress.fw;

import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FWCompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/" + Options.DIRECTORY + "/compression/java/programs/fw";

  public FWCompression(String programName) {
    this(programName, new ArrayList<>());
  }

  public FWCompression(String programName, List<String> options) {
    super(programName, options);
  }

  @Override
  public Set<Set<String>> analyze() {
    return FeatureWiseSampling.getInstance().getConfigs(this.getOptions());
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}

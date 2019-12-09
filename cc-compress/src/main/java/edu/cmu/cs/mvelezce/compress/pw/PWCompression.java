package edu.cmu.cs.mvelezce.compress.pw;

import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PWCompression extends BaseCompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/" + Options.DIRECTORY + "/compression/java/programs/pw";

  public PWCompression(String programName) {
    this(programName, new ArrayList<>());
  }

  public PWCompression(String programName, List<String> options) {
    super(programName, options);
  }

  @Override
  public Set<Set<String>> analyze() {
    return PairWiseSampling.getInstance().getConfigs(this.getOptions());
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

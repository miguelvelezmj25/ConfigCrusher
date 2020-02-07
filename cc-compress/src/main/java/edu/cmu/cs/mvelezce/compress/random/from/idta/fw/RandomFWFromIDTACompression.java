package edu.cmu.cs.mvelezce.compress.random.from.idta.fw;

import edu.cmu.cs.mvelezce.compress.random.from.idta.BaseRandomFromIDTACompression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomFWFromIDTACompression extends BaseRandomFromIDTACompression {

  public RandomFWFromIDTACompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  public RandomFWFromIDTACompression(
      String programName, List<String> options, Set<Set<String>> gtConfigs) {
    super(programName, options, gtConfigs);
  }

  @Override
  public Set<Set<String>> analyze() {
    return this.getRandomConfigsFromOther(this.getOptions().size());
  }

  @Override
  public String outputDir() {
    return BaseRandomFromIDTACompression.getOutputDir() + "/fw/" + this.getProgramName();
  }
}

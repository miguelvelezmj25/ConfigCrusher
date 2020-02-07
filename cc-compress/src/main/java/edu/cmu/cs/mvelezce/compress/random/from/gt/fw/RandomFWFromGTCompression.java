package edu.cmu.cs.mvelezce.compress.random.from.gt.fw;

import edu.cmu.cs.mvelezce.compress.random.from.gt.BaseRandomFromGTCompression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomFWFromGTCompression extends BaseRandomFromGTCompression {

  public RandomFWFromGTCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  public RandomFWFromGTCompression(
      String programName, List<String> options, Set<Set<String>> gtConfigs) {
    super(programName, options, gtConfigs);
  }

  @Override
  public Set<Set<String>> analyze() {
    return this.getRandomConfigsFromOther(this.getOptions().size());
  }

  @Override
  public String outputDir() {
    return BaseRandomFromGTCompression.getOutputDir() + "/fw/" + this.getProgramName();
  }
}

package edu.cmu.cs.mvelezce.compress.random.from.gt.idta;

import edu.cmu.cs.mvelezce.compress.random.from.gt.BaseRandomFromGTCompression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomIDTAFromGTCompression extends BaseRandomFromGTCompression {

  private final int numConfigs;

  public RandomIDTAFromGTCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>(), -1);
  }

  public RandomIDTAFromGTCompression(
      String programName, List<String> options, Set<Set<String>> gtConfigs, int numConfigs) {
    super(programName, options, gtConfigs);

    this.numConfigs = numConfigs;
  }

  @Override
  public Set<Set<String>> analyze() {
    return this.getRandomConfigsFromOther(this.numConfigs);
  }

  @Override
  public String outputDir() {
    return BaseRandomFromGTCompression.getOutputDir() + "/idta/" + this.getProgramName();
  }
}

package edu.cmu.cs.mvelezce.compress.random.from.gt.pw;

import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.compress.random.from.gt.BaseRandomFromGTCompression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomPWFromGTCompression extends BaseRandomFromGTCompression {

  public RandomPWFromGTCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  public RandomPWFromGTCompression(
      String programName, List<String> options, Set<Set<String>> gtConfigs) {
    super(programName, options, gtConfigs);
  }

  @Override
  public Set<Set<String>> analyze() {
    int numConfigs = PairWiseSampling.getInstance().getConfigs(this.getOptions()).size();

    return this.getRandomConfigsFromOther(numConfigs);
  }

  @Override
  public String outputDir() {
    return BaseRandomFromGTCompression.getOutputDir() + "/pw/" + this.getProgramName();
  }
}

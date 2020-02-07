package edu.cmu.cs.mvelezce.compress.random.from.idta.pw;

import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.compress.random.from.idta.BaseRandomFromIDTACompression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomPWFromIDTACompression extends BaseRandomFromIDTACompression {

  public RandomPWFromIDTACompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  public RandomPWFromIDTACompression(
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
    return BaseRandomFromIDTACompression.getOutputDir() + "/pw/" + this.getProgramName();
  }
}

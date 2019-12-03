package edu.cmu.cs.mvelezce.approaches.sampling.fw;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FeatureWiseSampling implements SamplingApproach {

  private static final FeatureWiseSampling INSTANCE = new FeatureWiseSampling();

  private FeatureWiseSampling() {}

  public static FeatureWiseSampling getInstance() {
    return INSTANCE;
  }

  @Override
  public Set<Set<String>> getConfigs(List<String> options) {
    Set<Set<String>> configs = new HashSet<>();

    for (String option : options) {
      Set<String> config = new HashSet<>();
      config.add(option);
      configs.add(config);
    }

    return configs;
  }
}

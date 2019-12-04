package edu.cmu.cs.mvelezce.approaches.sampling.fw;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FeatureWiseSampling implements SamplingApproach {

  private static final FeatureWiseSampling INSTANCE = new FeatureWiseSampling();
  private static final String NAME = "feature_wise";

  private FeatureWiseSampling() {}

  public static FeatureWiseSampling getInstance() {
    return INSTANCE;
  }

  @Override
  public Set<FeatureExpr> getConfigsAsConstraints(List<String> options) {
    Set<FeatureExpr> configsAsConstraints = new HashSet<>();

    for (String option : options) {
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(option);
      configsAsConstraints.add(constraint);
    }

    return configsAsConstraints;
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

  @Override
  public String getName() {
    return NAME;
  }
}

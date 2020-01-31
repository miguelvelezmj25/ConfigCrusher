package edu.cmu.cs.mvelezce.approaches.sampling.fw;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

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
  public String getLinearModelType() {
    return "linear";
  }

  @Override
  public Set<Partition> getConfigsAsPartitions(List<String> options) {
    Set<Partition> configsAsPartitions = new HashSet<>();

    for (String option : options) {
      Partition partition =
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, option));
      configsAsPartitions.add(partition);
    }

    return configsAsPartitions;
  }

  @Override
  public Set<Partition> getLinearModelPartitions(List<String> options) {
    return this.getConfigsAsPartitions(options);
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

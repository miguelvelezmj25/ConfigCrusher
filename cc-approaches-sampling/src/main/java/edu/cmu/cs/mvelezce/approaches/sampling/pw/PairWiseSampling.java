package edu.cmu.cs.mvelezce.approaches.sampling.pw;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import org.apache.commons.math3.util.Combinations;

import java.util.*;

public final class PairWiseSampling implements SamplingApproach {

  private static final PairWiseSampling INSTANCE = new PairWiseSampling();
  private static final String NAME = "pair_wise";

  private PairWiseSampling() {}

  public static PairWiseSampling getInstance() {
    return INSTANCE;
  }

  @Override
  public String getLinearModelType() {
    return "interactions";
  }

  @Override
  public Set<FeatureExpr> getConfigsAsConstraints(List<String> options) {
    throw new UnsupportedOperationException("implement");
    //    Set<FeatureExpr> configsAsConstraints = new HashSet<>();
    //
    //    for (String option : options) {
    //      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(option);
    //      configsAsConstraints.add(constraint);
    //    }
    //
    //    return configsAsConstraints;
  }

  @Override
  public Set<FeatureExpr> getLinearModelConstraints(List<String> options) {
    Set<FeatureExpr> constraints = new HashSet<>();

    if (options.isEmpty()) {
      return constraints;
    }

    for (String option : options) {
      FeatureExpr constraint = FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, option);
      constraints.add(constraint);
    }

    Set<List<String>> pairs = this.getPairs(options);

    for (List<String> pair : pairs) {
      Iterator<String> pairIter = pair.iterator();
      StringBuilder stringBuilder = new StringBuilder();

      while (pairIter.hasNext()) {
        String term = pairIter.next();
        stringBuilder.append(term);

        if (pairIter.hasNext()) {
          stringBuilder.append(" && ");
        }
      }

      FeatureExpr constraint =
          FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringBuilder.toString());
      constraints.add(constraint);
    }

    return constraints;
  }

  @Override
  public Set<Set<String>> getConfigs(List<String> options) {
    Set<List<String>> pairs = this.getPairs(options);
    Set<Set<String>> configs = new HashSet<>();

    for (List<String> pair : pairs) {
      Set<Set<String>> config = this.getPairWiseConfigs(pair);
      configs.addAll(config);
    }

    return configs;
  }

  private Set<Set<String>> getPairWiseConfigs(List<String> pair) {
    Set<Set<String>> configurations = new HashSet<>();

    Set<String> configuration = new HashSet<>();
    configurations.add(configuration);

    for (int i = 1; i <= pair.size(); i++) {
      Combinations combinations = new Combinations(pair.size(), i);

      for (int[] combination : combinations) {
        configuration = new HashSet<>();

        for (int index : combination) {
          configuration.add(pair.get(index));
        }

        configurations.add(configuration);
      }
    }

    return configurations;
  }

  private Set<List<String>> getPairs(Collection<String> options) {
    List<String> optionsList = new ArrayList<>(options);

    Combinations combinations = new Combinations(options.size(), 2);
    Set<List<String>> pairs = new HashSet<>();

    for (int[] combination : combinations) {
      List<String> pair = new ArrayList<>();

      for (int index : combination) {
        pair.add(optionsList.get(index));
      }

      pairs.add(pair);
    }

    return pairs;
  }

  @Override
  public String getName() {
    return NAME;
  }
}

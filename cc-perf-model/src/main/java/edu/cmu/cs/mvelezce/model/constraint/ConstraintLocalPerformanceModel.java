package edu.cmu.cs.mvelezce.model.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;

import java.util.*;

public class ConstraintLocalPerformanceModel extends LocalPerformanceModel<FeatureExpr> {

  private static final Map<Set<String>, FeatureExpr> CONFIG_TO_CONSTRAINT = new HashMap<>();

  public ConstraintLocalPerformanceModel(
      UUID region,
      Map<FeatureExpr, Double> model,
      Map<FeatureExpr, Double> modelToMin,
      Map<FeatureExpr, Double> modelToMax,
      Map<FeatureExpr, Double> modelToDiff,
      Map<FeatureExpr, Double> modelToSampleVariance,
      Map<FeatureExpr, List<Double>> modelToConfidenceInterval,
      Map<FeatureExpr, String> modelToPerfHumanReadable,
      Map<FeatureExpr, String> modelToMinHumanReadable,
      Map<FeatureExpr, String> modelToMaxHumanReadable,
      Map<FeatureExpr, String> modelToDiffHumanReadable,
      Map<FeatureExpr, String> modelToSampleVarianceHumanReadable,
      Map<FeatureExpr, List<String>> modelToConfidenceIntervalHumanReadable) {
    super(
        region,
        model,
        modelToMin,
        modelToMax,
        modelToDiff,
        modelToSampleVariance,
        modelToConfidenceInterval,
        modelToPerfHumanReadable,
        modelToMinHumanReadable,
        modelToMaxHumanReadable,
        modelToDiffHumanReadable,
        modelToSampleVarianceHumanReadable,
        modelToConfidenceIntervalHumanReadable);
  }

  protected static FeatureExpr getConfigAsConstraint(Set<String> config, List<String> options) {
    FeatureExpr configAsConstraint = CONFIG_TO_CONSTRAINT.get(config);

    if (configAsConstraint == null) {
      String stringConstraint = ConstraintUtils.parseAsConstraint(config, options);
      configAsConstraint = MinConfigsGenerator.parseAsFeatureExpr(stringConstraint);
      CONFIG_TO_CONSTRAINT.put(config, configAsConstraint);
    }

    return configAsConstraint;
  }

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    FeatureExpr configAsConstraint = getConfigAsConstraint(config, options);
    double time = 0;

    for (Map.Entry<FeatureExpr, Double> entry : this.getModel().entrySet()) {
      if (!configAsConstraint.implies(entry.getKey()).isTautology()) {
        continue;
      }

      time += entry.getValue();
      break;
    }

    return time;
  }
}

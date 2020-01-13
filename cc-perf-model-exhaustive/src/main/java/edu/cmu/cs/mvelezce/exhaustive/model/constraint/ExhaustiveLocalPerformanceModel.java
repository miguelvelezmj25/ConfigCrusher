package edu.cmu.cs.mvelezce.exhaustive.model.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.constraint.ConstraintLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ExhaustiveLocalPerformanceModel extends ConstraintLocalPerformanceModel {

  public ExhaustiveLocalPerformanceModel(
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

    System.err.println(
        "If this is an exhaustive model maybe I could check for equality when evaluating a config and exclude featureExpr that have already been used");
  }

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    FeatureExpr configAsConstraint = getConfigAsConstraint(config, options);

    if (this.getModel().containsKey(configAsConstraint)) {
      return this.getModel().get(configAsConstraint);
    }

    return super.evaluate(config, options);
  }

  @Override
  public double evaluateVariance(Set<String> config, List<String> options) {
    FeatureExpr configAsConstraint = getConfigAsConstraint(config, options);

    if (this.getModel().containsKey(configAsConstraint)) {
      return this.getModelToSampleVariance().get(configAsConstraint);
    }

    return super.evaluateVariance(config, options);
  }

  @Override
  public List<Double> evaluateConfidenceInterval(Set<String> config, List<String> options) {
    FeatureExpr configAsConstraint = getConfigAsConstraint(config, options);

    if (this.getModel().containsKey(configAsConstraint)) {
      return this.getModelToConfidenceInterval().get(configAsConstraint);
    }

    return super.evaluateConfidenceInterval(config, options);
  }
}

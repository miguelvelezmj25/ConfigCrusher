package edu.cmu.cs.mvelezce.learning.model.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.constraint.ConstraintLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class LearnedLocalPerformanceModel extends ConstraintLocalPerformanceModel {

  public LearnedLocalPerformanceModel(
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

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    return Math.max(super.evaluate(config, options), 0.0);
  }
}

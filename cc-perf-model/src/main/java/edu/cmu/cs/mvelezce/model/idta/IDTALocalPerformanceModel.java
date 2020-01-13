package edu.cmu.cs.mvelezce.model.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.constraint.ConstraintLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IDTALocalPerformanceModel extends ConstraintLocalPerformanceModel {

  public IDTALocalPerformanceModel(
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
    FeatureExpr configAsConstraint =
        ConstraintLocalPerformanceModel.getConfigAsConstraint(config, options);
    double time = 0;
    int entriesCovered = 0;

    for (Map.Entry<FeatureExpr, Double> entry : this.getModel().entrySet()) {
      if (!configAsConstraint.implies(entry.getKey()).isTautology()) {
        continue;
      }

      time += entry.getValue();
      entriesCovered++;
    }

    if (entriesCovered == 0) {
      return time;
    }

    return time / entriesCovered;
  }
}

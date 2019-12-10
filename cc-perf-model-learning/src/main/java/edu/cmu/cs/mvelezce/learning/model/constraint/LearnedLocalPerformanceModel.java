package edu.cmu.cs.mvelezce.learning.model.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.model.constraint.ConstraintLocalPerformanceModel;

import java.util.*;

public class LearnedLocalPerformanceModel extends ConstraintLocalPerformanceModel {

  public LearnedLocalPerformanceModel(
      UUID region,
      Map<FeatureExpr, Double> model,
      Map<FeatureExpr, Double> modelToMin,
      Map<FeatureExpr, Double> modelToMax,
      Map<FeatureExpr, Double> modelToDiff,
      Map<FeatureExpr, String> modelToPerfHumanReadable,
      Map<FeatureExpr, String> modelToMinHumanReadable,
      Map<FeatureExpr, String> modelToMaxHumanReadable,
      Map<FeatureExpr, String> modelToDiffHumanReadable) {
    super(
        region,
        model,
        modelToMin,
        modelToMax,
        modelToDiff,
        modelToPerfHumanReadable,
        modelToMinHumanReadable,
        modelToMaxHumanReadable,
        modelToDiffHumanReadable);
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
    }

    return Math.max(time, 0.0);
  }
}

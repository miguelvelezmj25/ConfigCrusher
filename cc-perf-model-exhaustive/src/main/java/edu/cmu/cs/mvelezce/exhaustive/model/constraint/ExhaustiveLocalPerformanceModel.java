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

    System.out.println(
        "If this is an exhaustive model maybe I could check for equality and exclude featureExpr that have already been used");
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

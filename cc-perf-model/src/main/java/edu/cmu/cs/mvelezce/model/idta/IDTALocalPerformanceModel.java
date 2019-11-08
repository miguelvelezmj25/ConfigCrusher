package edu.cmu.cs.mvelezce.model.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;

import java.util.Map;
import java.util.UUID;

public class IDTALocalPerformanceModel extends LocalPerformanceModel<FeatureExpr> {

  public IDTALocalPerformanceModel(
      UUID region,
      Map<FeatureExpr, Long> model,
      Map<FeatureExpr, Long> modelToMin,
      Map<FeatureExpr, Long> modelToMax,
      Map<FeatureExpr, Long> modelToDiff,
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
}

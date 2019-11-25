package edu.cmu.cs.mvelezce.model.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IDTAMultiEntryLocalPerformanceModel
    extends MultiEntryLocalPerformanceModel<FeatureExpr> {

  public IDTAMultiEntryLocalPerformanceModel(
      UUID region,
      Map<FeatureExpr, Set<Double>> model,
      Map<FeatureExpr, Set<Double>> modelToMins,
      Map<FeatureExpr, Set<Double>> modelToMaxs,
      Map<FeatureExpr, Set<Double>> modelToDiffs,
      Map<FeatureExpr, Set<Double>> modelToSampleVariances,
      Map<FeatureExpr, Set<List<Double>>> modelToConfidenceIntervals) {
    super(
        region,
        model,
        modelToMins,
        modelToMaxs,
        modelToDiffs,
        modelToSampleVariances,
        modelToConfidenceIntervals);
  }
}

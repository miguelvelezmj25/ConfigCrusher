package edu.cmu.cs.mvelezce.model.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;

import java.util.Map;
import java.util.UUID;

public class IDTALocalPerformanceModel extends LocalPerformanceModel<FeatureExpr> {

  public IDTALocalPerformanceModel(UUID region, Map<FeatureExpr, Long> model) {
    super(region, model);
  }
}

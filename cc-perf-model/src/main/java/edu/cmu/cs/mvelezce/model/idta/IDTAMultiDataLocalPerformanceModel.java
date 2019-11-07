package edu.cmu.cs.mvelezce.model.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.MultiDataLocalPerformanceModel;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IDTAMultiDataLocalPerformanceModel
    extends MultiDataLocalPerformanceModel<FeatureExpr> {

  public IDTAMultiDataLocalPerformanceModel(UUID region, Map<FeatureExpr, Set<Long>> model) {
    super(region, model);
  }
}

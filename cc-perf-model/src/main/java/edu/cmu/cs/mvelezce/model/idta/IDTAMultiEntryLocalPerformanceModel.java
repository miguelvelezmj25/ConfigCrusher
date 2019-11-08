package edu.cmu.cs.mvelezce.model.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IDTAMultiEntryLocalPerformanceModel
    extends MultiEntryLocalPerformanceModel<FeatureExpr> {

  public IDTAMultiEntryLocalPerformanceModel(UUID region, Map<FeatureExpr, Set<Long>> model) {
    super(region, model);
  }
}

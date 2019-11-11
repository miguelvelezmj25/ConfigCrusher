package edu.cmu.cs.mvelezce.analysis.region.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.BaseCountRegionsPerMethodAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;

import java.util.Map;
import java.util.Set;

public class IDTACountRegionsPerMethodAnalysis
    extends BaseCountRegionsPerMethodAnalysis<Set<FeatureExpr>> {

  public IDTACountRegionsPerMethodAnalysis(Map<JavaRegion, Set<FeatureExpr>> regionsToData) {
    super(regionsToData);
  }
}

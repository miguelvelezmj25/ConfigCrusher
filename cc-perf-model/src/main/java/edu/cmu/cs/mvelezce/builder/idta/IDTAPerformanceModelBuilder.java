package edu.cmu.cs.mvelezce.builder.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;

import java.util.Map;
import java.util.Set;

class IDTAPerformanceModelBuilder extends BasePerformanceModelBuilder<Set<FeatureExpr>> {

  IDTAPerformanceModelBuilder(
      String programName,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, regionsToData, performanceEntries);
  }
}

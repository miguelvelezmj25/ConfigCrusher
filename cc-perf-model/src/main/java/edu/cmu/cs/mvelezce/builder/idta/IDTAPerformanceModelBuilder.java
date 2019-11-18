package edu.cmu.cs.mvelezce.builder.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.List;
import java.util.Map;
import java.util.Set;

class IDTAPerformanceModelBuilder extends BaseConstraintPerformanceModelBuilder {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/model/java/idta/programs";

  IDTAPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

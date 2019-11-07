package edu.cmu.cs.mvelezce.builder.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiDataLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAMultiDataLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class IDTAPerformanceModelBuilder extends BasePerformanceModelBuilder<Set<FeatureExpr>> {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/model/java/idta/programs";

  IDTAPerformanceModelBuilder(
      String programName,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, regionsToData, performanceEntries);
  }

  @Override
  protected void buildLocalModels() {
    for (Map.Entry<JavaRegion, Set<FeatureExpr>> entry : this.getRegionsToData().entrySet()) {
      MultiDataLocalPerformanceModel<FeatureExpr> localModel =
          this.buildEmptyMultiDataLocalModel(entry);

      throw new UnsupportedOperationException(
          "loop through the execution entries, adding the execution time of the region based on the config executed");
    }

    throw new UnsupportedOperationException("What should the return value be?");
  }

  private MultiDataLocalPerformanceModel<FeatureExpr> buildEmptyMultiDataLocalModel(
      Map.Entry<JavaRegion, Set<FeatureExpr>> entry) {
    Map<FeatureExpr, Set<Long>> model = this.addConstraintEntries(entry.getValue());

    return new IDTAMultiDataLocalPerformanceModel(entry.getKey().getId(), model);
  }

  private Map<FeatureExpr, Set<Long>> addConstraintEntries(Set<FeatureExpr> constraints) {
    System.err.println("What about redundant constraints?");
    Map<FeatureExpr, Set<Long>> model = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      model.put(constraint, new HashSet<>());
    }

    return model;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

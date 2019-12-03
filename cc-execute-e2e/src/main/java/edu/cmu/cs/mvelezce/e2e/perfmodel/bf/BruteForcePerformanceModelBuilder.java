package edu.cmu.cs.mvelezce.e2e.perfmodel.bf;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.e2e.perfmodel.E2EPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteForcePerformanceModelBuilder extends E2EPerformanceModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-execute-e2e/" + Options.DIRECTORY + "/model/java/programs/bf";

  public BruteForcePerformanceModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  BruteForcePerformanceModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  protected void populateMultiEntryLocalModel(
      MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
    this.validateOneConfigCoversOneConstraint(localModel);

    super.populateMultiEntryLocalModel(localModel);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

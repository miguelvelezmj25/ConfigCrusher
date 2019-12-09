package edu.cmu.cs.mvelezce.exhaustive.model.gt;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.exhaustive.model.ExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroundTruthExhaustiveModelBuilder extends ExhaustiveModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-perf-model-exhaustive/" + Options.DIRECTORY + "/model/java/programs/gt";

  public GroundTruthExhaustiveModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  GroundTruthExhaustiveModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  protected void populateMultiEntryLocalModel(
      MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
    throw new UnsupportedOperationException(
        "Separate these steps into two executions. Otherwise, it takes too long");
    //    this.validateOneConfigCoversOneConstraint(localModel);
    //
    //    super.populateMultiEntryLocalModel(localModel);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

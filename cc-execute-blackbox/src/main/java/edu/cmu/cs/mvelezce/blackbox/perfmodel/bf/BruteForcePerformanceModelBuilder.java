package edu.cmu.cs.mvelezce.blackbox.perfmodel.bf;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.blackbox.perfmodel.BlackBoxPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteForcePerformanceModelBuilder extends BlackBoxPerformanceModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-execute-blackbox/" + Options.DIRECTORY + "/model/java/programs/bf";

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

  private void validateOneConfigCoversOneConstraint(
      MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      FeatureExpr configConstraint = this.getPerfEntryToExecConstraint().get(entry);
      Set<FeatureExpr> coveredConstraints = new HashSet<>();

      for (FeatureExpr regionConstraint : localModel.getModel().keySet()) {
        if (configConstraint.implies(regionConstraint).isTautology()) {
          coveredConstraints.add(regionConstraint);
        }
      }

      if (coveredConstraints.size() > 1) {
        throw new RuntimeException(
            "Expected that one executed configuration would cover at most one region constraint"
                + localModel.getRegion());
      }
    }
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}

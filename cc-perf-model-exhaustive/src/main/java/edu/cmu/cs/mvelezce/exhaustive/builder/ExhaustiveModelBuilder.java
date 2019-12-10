package edu.cmu.cs.mvelezce.exhaustive.builder;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.builder.E2EModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ExhaustiveModelBuilder extends E2EModelBuilder {

  public ExhaustiveModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  protected void validateOneConfigCoversOneConstraint(
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
}

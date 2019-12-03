package edu.cmu.cs.mvelezce.e2e.perfmodel;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.region.RegionsManager;

import java.util.*;

public abstract class E2EPerformanceModelBuilder extends BaseConstraintPerformanceModelBuilder {

  private static final Map<JavaRegion, Set<FeatureExpr>> REGIONS_TO_DATA = new HashMap<>();

  public E2EPerformanceModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, REGIONS_TO_DATA, performanceEntries);

    this.addProgramRegionToData();
  }

  private void addProgramRegionToData() {
    Set<FeatureExpr> constraints = new HashSet<>();
    List<String> options = this.getOptions();

    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      Set<String> config = entry.getConfiguration();
      String configConstraint = ConstraintUtils.parseAsConstraint(config, options);
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(configConstraint);
      constraints.add(constraint);
    }

    REGIONS_TO_DATA.put(RegionsManager.PROGRAM_REGION, constraints);
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

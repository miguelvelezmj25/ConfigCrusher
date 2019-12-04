package edu.cmu.cs.mvelezce.builder;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.region.RegionsManager;

import java.util.*;

public abstract class E2EModelBuilder extends BaseConstraintPerformanceModelBuilder {

  protected static final Map<JavaRegion, Set<FeatureExpr>> REGIONS_TO_DATA = new HashMap<>();

  public E2EModelBuilder(
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
}

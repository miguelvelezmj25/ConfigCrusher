package edu.cmu.cs.mvelezce.builder.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAMultiEntryLocalPerformanceModel;

import java.util.*;

public abstract class BaseConstraintPerformanceModelBuilder
    extends BasePerformanceModelBuilder<Set<FeatureExpr>, FeatureExpr> {

  private final Map<PerformanceEntry, FeatureExpr> perfEntryToExecConstraint = new HashMap<>();

  public BaseConstraintPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);
  }

  @Override
  protected Set<MultiEntryLocalPerformanceModel<FeatureExpr>> buildMultiEntryLocalModels() {
    this.mapPerfEntryToExecConstraint();

    return super.buildMultiEntryLocalModels();
  }

  private void mapPerfEntryToExecConstraint() {
    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      Set<String> config = entry.getConfiguration();
      String configConstraint = ConstraintUtils.parseAsConstraint(config, this.getOptions());
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(configConstraint);
      this.perfEntryToExecConstraint.put(entry, constraint);
    }
  }

  @Override
  protected void addExecutionTimes(MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
    Map<FeatureExpr, Set<Long>> multiEntryModel = localModel.getModel();
    this.validateOneConfigCoversOneConstraint(multiEntryModel.keySet());

    UUID region = localModel.getRegion();

    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      FeatureExpr configConstraint = this.perfEntryToExecConstraint.get(entry);

      for (Map.Entry<UUID, Long> regionToTime : entry.getRegionsToPerf().entrySet()) {
        if (!region.equals(regionToTime.getKey())) {
          continue;
        }

        for (Map.Entry<FeatureExpr, Set<Long>> constraintToTimes : multiEntryModel.entrySet()) {
          if (!configConstraint.implies(constraintToTimes.getKey()).isTautology()) {
            continue;
          }

          constraintToTimes.getValue().add(regionToTime.getValue());

          break;
        }

        break;
      }
    }
  }

  private void validateOneConfigCoversOneConstraint(Set<FeatureExpr> regionConstraints) {
    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      FeatureExpr configConstraint = this.perfEntryToExecConstraint.get(entry);

      Set<FeatureExpr> coveredConstraints = new HashSet<>();

      for (FeatureExpr regionConstraint : regionConstraints) {
        if (configConstraint.implies(regionConstraint).isTautology()) {
          coveredConstraints.add(regionConstraint);
        }
      }

      if (coveredConstraints.size() > 1) {
        throw new RuntimeException(
            "Expected that one executed configuration would cover at most one region constraint");
      }
    }
  }

  @Override
  protected MultiEntryLocalPerformanceModel<FeatureExpr> buildEmptyMultiEntryLocalModel(
      Map.Entry<JavaRegion, Set<FeatureExpr>> entry) {
    Map<FeatureExpr, Set<Long>> model = this.addConstraintEntries(entry.getValue());

    return new IDTAMultiEntryLocalPerformanceModel(entry.getKey().getId(), model);
  }

  private Map<FeatureExpr, Set<Long>> addConstraintEntries(Set<FeatureExpr> constraints) {
    System.err.println("What about redundant constraints?");
    Map<FeatureExpr, Set<Long>> model = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      model.put(constraint, new HashSet<>());
    }

    return model;
  }
}

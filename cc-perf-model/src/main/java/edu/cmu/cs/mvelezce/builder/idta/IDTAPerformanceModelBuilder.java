package edu.cmu.cs.mvelezce.builder.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.MultiDataLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAMultiDataLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

class IDTAPerformanceModelBuilder extends BasePerformanceModelBuilder<Set<FeatureExpr>> {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/model/java/idta/programs";

  private final Map<PerformanceEntry, FeatureExpr> perfEntryToExecConstraint = new HashMap<>();

  IDTAPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);
  }

  @Override
  protected void buildLocalModels() {
    this.mapPerfEntryToExeConstraint();

    for (Map.Entry<JavaRegion, Set<FeatureExpr>> entry : this.getRegionsToData().entrySet()) {
      MultiDataLocalPerformanceModel<FeatureExpr> multiDataLocalModel =
          this.buildEmptyMultiDataLocalModel(entry);

      this.addExecutionTimes(multiDataLocalModel);

      throw new UnsupportedOperationException(
          "run statistics and average the entries for the local model");
    }

    throw new UnsupportedOperationException("What should the return value be?");
  }

  private void mapPerfEntryToExeConstraint() {
    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      Set<String> config = entry.getConfiguration();
      String configConstraint = ConstraintUtils.parseAsConstraint(config, this.getOptions());
      FeatureExpr constraint = MinConfigsGenerator.parseAsFeatureExpr(configConstraint);
      this.perfEntryToExecConstraint.put(entry, constraint);
    }
  }

  private void addExecutionTimes(MultiDataLocalPerformanceModel<FeatureExpr> localModel) {
    Map<FeatureExpr, Set<Long>> multiDataModel = localModel.getModel();
    this.validateOneConfigCoversOneConstraint(multiDataModel.keySet());

    UUID region = localModel.getRegion();

    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      FeatureExpr configConstraint = this.perfEntryToExecConstraint.get(entry);

      for (Map.Entry<UUID, Long> regionToTime : entry.getRegionsToPerf().entrySet()) {
        if (!region.equals(regionToTime.getKey())) {
          continue;
        }

        for (Map.Entry<FeatureExpr, Set<Long>> constraintToTimes : multiDataModel.entrySet()) {
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

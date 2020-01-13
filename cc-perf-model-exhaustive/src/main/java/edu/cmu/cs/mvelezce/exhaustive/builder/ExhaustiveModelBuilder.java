package edu.cmu.cs.mvelezce.exhaustive.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.builder.E2EModelBuilder;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.exhaustive.model.constraint.ExhaustiveLocalPerformanceModel;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class ExhaustiveModelBuilder extends E2EModelBuilder {

  public ExhaustiveModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  public PerformanceModel<FeatureExpr> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<FeatureExpr> localModel =
          new ExhaustiveLocalPerformanceModel(
              readLocalModel.getRegion(),
              this.parseConstraintsToData(readLocalModel.getModel()),
              this.parseConstraintsToData(readLocalModel.getModelToMin()),
              this.parseConstraintsToData(readLocalModel.getModelToMax()),
              this.parseConstraintsToData(readLocalModel.getModelToDiff()),
              this.parseConstraintsToData(readLocalModel.getModelToSampleVariance()),
              this.parseConstraintsToCI(readLocalModel.getModelToConfidenceInterval()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToPerfHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMinHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMaxHumanReadable()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToDiffHumanReadable()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToSampleVarianceHumanReadble()),
              this.parseConstraintsToHumanReadableCI(
                  readLocalModel.getModelToConfidenceIntervalHumanReadable()));
      localModels.add(localModel);
    }

    return new PerformanceModel<>(localModels);
  }

  @Override
  protected void populateLocalModel(LocalPerformanceModel<FeatureExpr> localModel) {
    UUID programRegion = localModel.getRegion();

    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      FeatureExpr configConstraint = this.getPerfEntryToExecConstraint().get(entry);

      for (UUID regionUUID : entry.getRegionsToPerf().keySet()) {
        if (!programRegion.equals(regionUUID)) {
          throw new RuntimeException(
              "Expected the performance entry to have thr program region '"
                  + programRegion
                  + "', but found '"
                  + regionUUID
                  + "'");
        }

        this.addEntry(
            localModel.getModel(), configConstraint, entry.getRegionsToPerf().get(regionUUID));
        this.addEntry(
            localModel.getModelToMin(), configConstraint, entry.getRegionsToMin().get(regionUUID));
        this.addEntry(
            localModel.getModelToMax(), configConstraint, entry.getRegionsToMax().get(regionUUID));
        this.addEntry(
            localModel.getModelToDiff(),
            configConstraint,
            entry.getRegionsToDiff().get(regionUUID));
        this.addEntry(
            localModel.getModelToSampleVariance(),
            configConstraint,
            entry.getRegionsToSampleVariance().get(regionUUID));
        this.addEntry(
            localModel.getModelToConfidenceInterval(),
            configConstraint,
            entry.getRegionsToConfidenceInterval().get(regionUUID));

        this.addEntry(
            localModel.getModelToPerfHumanReadable(),
            configConstraint,
            entry.getRegionsToPerfHumanReadable().get(regionUUID));
        this.addEntry(
            localModel.getModelToMinHumanReadable(),
            configConstraint,
            entry.getRegionsToMinHumanReadable().get(regionUUID));
        this.addEntry(
            localModel.getModelToMaxHumanReadable(),
            configConstraint,
            entry.getRegionsToMaxHumanReadable().get(regionUUID));
        this.addEntry(
            localModel.getModelToDiffHumanReadable(),
            configConstraint,
            entry.getRegionsToDiffHumanReadable().get(regionUUID));
        this.addEntry(
            localModel.getModelToSampleVarianceHumanReadble(),
            configConstraint,
            entry.getRegionsToSampleVarianceHumanReadable().get(regionUUID));
        this.addEntryHuman(
            localModel.getModelToConfidenceIntervalHumanReadable(),
            configConstraint,
            entry.getRegionsToConfidenceIntervalsHumanReadable().get(regionUUID));
      }
    }
  }

  private void addEntry(
      Map<FeatureExpr, String> model, FeatureExpr configConstraint, String value) {
    if (!model.containsKey(configConstraint)) {
      throw new RuntimeException("Could not find config constraint " + configConstraint);
    }

    if (!model.get(configConstraint).equals(BaseConstraintPerformanceModelBuilder.EMPTY_HUMAN)) {
      throw new RuntimeException(
          "Expected the entry of '"
              + configConstraint
              + "' to be '"
              + BaseConstraintPerformanceModelBuilder.EMPTY_HUMAN
              + "', but was '"
              + model.get(configConstraint)
              + "' and was about to add "
              + value);
    }

    model.put(configConstraint, value);
  }

  private void addEntry(
      Map<FeatureExpr, Double> model, FeatureExpr configConstraint, double value) {
    if (!model.containsKey(configConstraint)) {
      throw new RuntimeException("Could not find config constraint " + configConstraint);
    }

    if (model.get(configConstraint) != BaseConstraintPerformanceModelBuilder.EMPTY_DOUBLE) {
      throw new RuntimeException(
          "Expected the entry of '"
              + configConstraint
              + "' to be '"
              + BaseConstraintPerformanceModelBuilder.EMPTY_DOUBLE
              + "', but was '"
              + model.get(configConstraint)
              + "' and was about to add "
              + value);
    }

    model.put(configConstraint, value);
  }

  private void addEntry(
      Map<FeatureExpr, List<Double>> model, FeatureExpr configConstraint, List<Double> values) {
    if (!model.containsKey(configConstraint)) {
      throw new RuntimeException("Could not find config constraint " + configConstraint);
    }

    List<Double> entries = model.get(configConstraint);

    if (!entries.isEmpty()) {
      throw new RuntimeException(
          "Expected the entry of '"
              + configConstraint
              + "' to be empty, but found "
              + entries
              + " and was about to add "
              + values);
    }

    model.put(configConstraint, values);
  }

  private void addEntryHuman(
      Map<FeatureExpr, List<String>> model, FeatureExpr configConstraint, List<String> values) {
    if (!model.containsKey(configConstraint)) {
      throw new RuntimeException("Could not find config constraint " + configConstraint);
    }

    List<String> entries = model.get(configConstraint);

    if (!entries.isEmpty()) {
      throw new RuntimeException(
          "Expected the entry of '"
              + configConstraint
              + "' to be empty, but found "
              + entries
              + " and was about to add "
              + values);
    }

    model.put(configConstraint, values);
  }
}

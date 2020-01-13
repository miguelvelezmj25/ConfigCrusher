package edu.cmu.cs.mvelezce.exhaustive.builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.builder.E2EModelBuilder;
import edu.cmu.cs.mvelezce.exhaustive.model.constraint.ExhaustiveLocalPerformanceModel;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
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
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToPerfHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMinHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMaxHumanReadable()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToDiffHumanReadable()));
      localModels.add(localModel);
    }

    return new PerformanceModel<>(localModels);
  }

  @Override
  protected void populateMultiEntryLocalModel(
      MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
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
            localModel.getModelToMins(), configConstraint, entry.getRegionsToMin().get(regionUUID));
        this.addEntry(
            localModel.getModelToMaxs(), configConstraint, entry.getRegionsToMax().get(regionUUID));
        this.addEntry(
            localModel.getModelToDiffs(),
            configConstraint,
            entry.getRegionsToDiff().get(regionUUID));
        this.addEntry(
            localModel.getModelToSampleVariances(),
            configConstraint,
            entry.getRegionsToSampleVariance().get(regionUUID));
        this.addEntry(
            localModel.getModelToConfidenceIntervals(),
            configConstraint,
            entry.getRegionsToConfidenceInterval().get(regionUUID));
      }
    }
  }

  private void addEntry(
      Map<FeatureExpr, Set<Double>> model, FeatureExpr configConstraint, double value) {
    if (!model.containsKey(configConstraint)) {
      throw new RuntimeException("Could not find config constraint " + configConstraint);
    }

    Set<Double> values = model.get(configConstraint);

    if (!values.isEmpty()) {
      throw new RuntimeException(
          "Expected the entry of '"
              + configConstraint
              + "' to be empty, but found "
              + values
              + " and was about to add "
              + value);
    }

    values.add(value);
  }

  private void addEntry(
      Map<FeatureExpr, Set<List<Double>>> model,
      FeatureExpr configConstraint,
      List<Double> values) {
    if (!model.containsKey(configConstraint)) {
      throw new RuntimeException("Could not find config constraint " + configConstraint);
    }

    Set<List<Double>> entries = model.get(configConstraint);

    if (!entries.isEmpty()) {
      throw new RuntimeException(
          "Expected the entry of '"
              + configConstraint
              + "' to be empty, but found "
              + entries
              + " and was about to add "
              + values);
    }

    entries.add(values);
  }
}

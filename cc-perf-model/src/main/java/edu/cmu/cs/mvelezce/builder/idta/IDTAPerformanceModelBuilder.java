package edu.cmu.cs.mvelezce.builder.idta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTALocalPerformanceModel;
import edu.cmu.cs.mvelezce.region.RegionsManager;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IDTAPerformanceModelBuilder extends BaseConstraintPerformanceModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-perf-model/" + Options.DIRECTORY + "/model/java/idta/programs";

  public IDTAPerformanceModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashMap<>(), new HashSet<>());
  }

  IDTAPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);

    Set<FeatureExpr> baseConstraints = new HashSet<>();
    baseConstraints.add(SATFeatureExprFactory.True());

    regionsToData.put(RegionsManager.PROGRAM_REGION, baseConstraints);
  }

  @Override
  protected void populateLocalModel(LocalPerformanceModel<FeatureExpr> localModel) {
    Map<FeatureExpr, List<Double>> multiEntryModel =
        this.getMultiEntryModel(localModel.getModel().keySet());

    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      Map<UUID, Double> regionsToPerfs = entry.getRegionsToPerf();
      UUID region = localModel.getRegion();

      if (!regionsToPerfs.containsKey(region)) {
        System.err.println(
            "There might be multiple regions in a method. The region "
                + region
                + " was not recorded in config "
                + entry.getConfiguration());
        continue;
      }

      double time = regionsToPerfs.get(region);
      FeatureExpr configConstraint = this.getPerfEntryToExecConstraint().get(entry);

      for (Map.Entry<FeatureExpr, Double> constraintToTimes : localModel.getModel().entrySet()) {
        if (!configConstraint.implies(constraintToTimes.getKey()).isTautology()) {
          continue;
        }

        multiEntryModel.get(constraintToTimes.getKey()).add(time);
      }
    }

    this.averageMultiEntryModel(localModel.getModel(), multiEntryModel);

    //    Set<FeatureExpr> remove = new HashSet<>();
    //    for (Map.Entry<FeatureExpr, Double> entry : localModel.getModel().entrySet()) {
    //      if (entry.getValue() == 0.0) {
    //        remove.add(entry.getKey());
    //      }
    //    }
    //
    //    for (FeatureExpr entry : remove) {
    //      localModel.getModel().remove(entry);
    //    }

    localModel.getModelToMin().clear();
    localModel.getModelToMax().clear();
    localModel.getModelToDiff().clear();
    localModel.getModelToSampleVariance().clear();
    localModel.getModelToConfidenceInterval().clear();
    localModel.getModelToPerfHumanReadable().clear();
    localModel.getModelToMinHumanReadable().clear();
    localModel.getModelToMaxHumanReadable().clear();
    localModel.getModelToDiffHumanReadable().clear();
    localModel.getModelToSampleVarianceHumanReadble().clear();
    localModel.getModelToConfidenceIntervalHumanReadable().clear();
  }

  private void averageMultiEntryModel(
      Map<FeatureExpr, Double> model, Map<FeatureExpr, List<Double>> multiEntryModel) {
    for (Map.Entry<FeatureExpr, List<Double>> entry : multiEntryModel.entrySet()) {
      List<Double> times = entry.getValue();

      if (times.isEmpty()) {
        continue;
      }

      double total = 0;

      for (Double time : times) {
        total += time;
      }

      total /= times.size();

      model.put(entry.getKey(), total);
    }
  }

  private Map<FeatureExpr, List<Double>> getMultiEntryModel(Set<FeatureExpr> constraints) {
    Map<FeatureExpr, List<Double>> multiEntryModel = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      multiEntryModel.put(constraint, new ArrayList<>());
    }

    return multiEntryModel;
  }

  //  protected void validateOneConfigCoversOneConstraint(
  //          MultiEntryLocalPerformanceModel<FeatureExpr> localModel) {
  //    for (PerformanceEntry entry : this.getPerformanceEntries()) {
  //      FeatureExpr configConstraint = this.getPerfEntryToExecConstraint().get(entry);
  //      Set<FeatureExpr> coveredConstraints = new HashSet<>();
  //
  //      for (FeatureExpr regionConstraint : localModel.getModel().keySet()) {
  //        if (configConstraint.implies(regionConstraint).isTautology()) {
  //          coveredConstraints.add(regionConstraint);
  //        }
  //      }
  //
  //      if (coveredConstraints.size() > 1) {
  //        throw new RuntimeException(
  //                "Expected that one executed configuration would cover at most one region
  // constraint"
  //                        + localModel.getRegion());
  //      }
  //    }
  //  }

  @Override
  public PerformanceModel<FeatureExpr> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<FeatureExpr> localModel =
          new IDTALocalPerformanceModel(
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
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}

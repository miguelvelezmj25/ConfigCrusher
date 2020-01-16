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
import edu.cmu.cs.mvelezce.utils.stats.SummaryStatisticsMap;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class IDTAPerformanceModelBuilder extends BaseConstraintPerformanceModelBuilder {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0000000");
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
    SummaryStatisticsMap<FeatureExpr> modelWithStats =
        this.getModelWithStats(localModel.getModel().keySet());
    this.addPerfEntries(localModel, modelWithStats);

    localModel.getModel().putAll(modelWithStats.getEntriesToData());
    Map<FeatureExpr, Double> regionsToMin = modelWithStats.getEntriesToMin();
    localModel.getModelToMin().putAll(regionsToMin);
    Map<FeatureExpr, Double> regionsToMax = modelWithStats.getEntriesToMax();
    localModel.getModelToMax().putAll(regionsToMax);
    localModel.getModelToDiff().putAll(modelWithStats.getEntriesToDiff(regionsToMin, regionsToMax));
    localModel.getModelToSampleVariance().putAll(modelWithStats.getEntriesToSampleVariance());
    localModel
        .getModelToConfidenceInterval()
        .putAll(modelWithStats.getEntriesToConfidenceInterval());

    localModel.getModelToPerfHumanReadable().putAll(toHumanReadable(localModel.getModel()));
    localModel.getModelToMinHumanReadable().putAll(toHumanReadable(localModel.getModelToMin()));
    localModel.getModelToMaxHumanReadable().putAll(toHumanReadable(localModel.getModelToMax()));
    localModel.getModelToDiffHumanReadable().putAll(toHumanReadable(localModel.getModelToDiff()));
    localModel
        .getModelToSampleVarianceHumanReadble()
        .putAll(toHumanReadableSampleVariance(localModel.getModelToSampleVariance()));
    localModel
        .getModelToConfidenceIntervalHumanReadable()
        .putAll(toHumanReadableCI(localModel.getModelToConfidenceInterval()));
  }

  private Map<FeatureExpr, String> toHumanReadableSampleVariance(
      Map<FeatureExpr, Double> constraintsToSampleVariance) {
    Map<FeatureExpr, String> constraintsToHumanReadableData = new HashMap<>();

    for (Map.Entry<FeatureExpr, Double> entry : constraintsToSampleVariance.entrySet()) {
      double data = entry.getValue();
      data = data / 1E18;
      constraintsToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return constraintsToHumanReadableData;
  }

  private Map<FeatureExpr, String> toHumanReadable(Map<FeatureExpr, Double> constraintsToData) {
    Map<FeatureExpr, String> constraintsToHumanReadableData = new HashMap<>();

    for (Map.Entry<FeatureExpr, Double> entry : constraintsToData.entrySet()) {
      double data = entry.getValue();
      data = data / 1E9;
      constraintsToHumanReadableData.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return constraintsToHumanReadableData;
  }

  private Map<FeatureExpr, List<String>> toHumanReadableCI(
      Map<FeatureExpr, List<Double>> constraintsToConfidenceIntervals) {
    Map<FeatureExpr, List<String>> constraintsToHumanReadableCI = new HashMap<>();

    for (Map.Entry<FeatureExpr, List<Double>> entry : constraintsToConfidenceIntervals.entrySet()) {
      List<Double> confidenceInterval = entry.getValue();

      if (confidenceInterval.isEmpty()) {
        continue;
      }

      double lower = confidenceInterval.get(0) / 1E9;
      double higher = confidenceInterval.get(1) / 1E9;
      List<String> confidenceIntervalHumanReadable = new ArrayList<>();
      confidenceIntervalHumanReadable.add(DECIMAL_FORMAT.format(lower));
      confidenceIntervalHumanReadable.add(DECIMAL_FORMAT.format(higher));
      constraintsToHumanReadableCI.put(entry.getKey(), confidenceIntervalHumanReadable);
    }

    return constraintsToHumanReadableCI;
  }

  private void addPerfEntries(
      LocalPerformanceModel<FeatureExpr> localModel,
      SummaryStatisticsMap<FeatureExpr> modelWithStats) {
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

        modelWithStats.get(constraintToTimes.getKey()).addValue(time);
      }
    }
  }

  private SummaryStatisticsMap<FeatureExpr> getModelWithStats(Set<FeatureExpr> constraints) {
    SummaryStatisticsMap<FeatureExpr> modelWithStats = new SummaryStatisticsMap<>();

    for (FeatureExpr constraint : constraints) {
      modelWithStats.putIfAbsent(constraint);
    }

    return modelWithStats;
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

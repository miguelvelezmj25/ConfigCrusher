package edu.cmu.cs.mvelezce.builder.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.constraint.ConstraintLocalPerformanceModel;

import java.util.*;

public abstract class BaseConstraintPerformanceModelBuilder
    extends BasePerformanceModelBuilder<Set<FeatureExpr>, FeatureExpr> {

  public static final double EMPTY_DOUBLE = 0.0;
  public static final String EMPTY_HUMAN = "";

  private static final Map<String, FeatureExpr> STRINGS_TO_CONSTRAINTS = new HashMap<>();

  private final Map<PerformanceEntry, FeatureExpr> perfEntryToExecConstraint = new HashMap<>();

  public BaseConstraintPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Set<FeatureExpr>> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);
  }

  private static FeatureExpr getConstraint(String string) {
    FeatureExpr constraint = STRINGS_TO_CONSTRAINTS.get(string);

    if (constraint == null) {
      constraint = MinConfigsGenerator.parseAsFeatureExpr(string);
      STRINGS_TO_CONSTRAINTS.put(string, constraint);
    }

    return constraint;
  }

  @Override
  protected Set<LocalPerformanceModel<FeatureExpr>> buildLocalModels() {
    this.mapPerfEntryToExecConstraint();

    return super.buildLocalModels();
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
  protected void populateLocalModel(LocalPerformanceModel<FeatureExpr> localModel) {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  protected LocalPerformanceModel<FeatureExpr> buildEmptyLocalModel(
      Map.Entry<JavaRegion, Set<FeatureExpr>> entry) {
    Map<FeatureExpr, Double> model = this.addConstraintEntries(entry.getValue());
    Map<FeatureExpr, Double> modelToMin = this.addConstraintEntries(entry.getValue());
    Map<FeatureExpr, Double> modelToMax = this.addConstraintEntries(entry.getValue());
    Map<FeatureExpr, Double> modelToDiff = this.addConstraintEntries(entry.getValue());
    Map<FeatureExpr, Double> modelToSampleVariance = this.addConstraintEntries(entry.getValue());
    Map<FeatureExpr, List<Double>> modelToConfidenceInterval =
        this.addConstraintEntriesCI(entry.getValue());

    Map<FeatureExpr, String> modelToPerfHumanReadable =
        this.addConstraintEntriesHuman(entry.getValue());
    Map<FeatureExpr, String> modelToMinHumanReadable =
        this.addConstraintEntriesHuman(entry.getValue());
    Map<FeatureExpr, String> modelToMaxHumanReadable =
        this.addConstraintEntriesHuman(entry.getValue());
    Map<FeatureExpr, String> modelToDiffHumanReadable =
        this.addConstraintEntriesHuman(entry.getValue());
    Map<FeatureExpr, String> modelToSampleVarianceHumanReadable =
        this.addConstraintEntriesHuman(entry.getValue());
    Map<FeatureExpr, List<String>> modelToConfidenceIntervalHumanReadable =
        this.addConstraintEntriesCIHuman(entry.getValue());

    return new ConstraintLocalPerformanceModel(
        entry.getKey().getId(),
        model,
        modelToMin,
        modelToMax,
        modelToDiff,
        modelToSampleVariance,
        modelToConfidenceInterval,
        modelToPerfHumanReadable,
        modelToMinHumanReadable,
        modelToMaxHumanReadable,
        modelToDiffHumanReadable,
        modelToSampleVarianceHumanReadable,
        modelToConfidenceIntervalHumanReadable);
  }

  private Map<FeatureExpr, List<String>> addConstraintEntriesCIHuman(Set<FeatureExpr> constraints) {
    Map<FeatureExpr, List<String>> model = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      model.put(constraint, new ArrayList<>());
    }

    return model;
  }

  private Map<FeatureExpr, String> addConstraintEntriesHuman(Set<FeatureExpr> constraints) {
    Map<FeatureExpr, String> model = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      model.put(constraint, EMPTY_HUMAN);
    }

    return model;
  }

  private Map<FeatureExpr, List<Double>> addConstraintEntriesCI(Set<FeatureExpr> constraints) {
    Map<FeatureExpr, List<Double>> model = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      model.put(constraint, new ArrayList<>());
    }

    return model;
  }

  private Map<FeatureExpr, Double> addConstraintEntries(Set<FeatureExpr> constraints) {
    Map<FeatureExpr, Double> model = new HashMap<>();

    for (FeatureExpr constraint : constraints) {
      model.put(constraint, EMPTY_DOUBLE);
    }

    return model;
  }

  protected Map<FeatureExpr, String> parseConstraintsToHumanReadableData(
      Map<String, String> localHumanReadableData) {
    Map<FeatureExpr, String> constraintsToHumanReadableData = new HashMap<>();

    for (Map.Entry<String, String> entry : localHumanReadableData.entrySet()) {
      FeatureExpr constraint = getConstraint(entry.getKey());
      constraintsToHumanReadableData.put(constraint, entry.getValue());
    }

    return constraintsToHumanReadableData;
  }

  protected Map<FeatureExpr, List<String>> parseConstraintsToHumanReadableCI(
      Map<String, List<String>> localHumanReadableData) {
    Map<FeatureExpr, List<String>> constraintsToHumanReadableData = new HashMap<>();

    for (Map.Entry<String, List<String>> entry : localHumanReadableData.entrySet()) {
      FeatureExpr constraint = getConstraint(entry.getKey());
      constraintsToHumanReadableData.put(constraint, entry.getValue());
    }

    return constraintsToHumanReadableData;
  }

  protected Map<FeatureExpr, Double> parseConstraintsToData(Map<String, Double> localModel) {
    Map<FeatureExpr, Double> constraintsToData = new HashMap<>();

    for (Map.Entry<String, Double> entry : localModel.entrySet()) {
      FeatureExpr constraint = getConstraint(entry.getKey());
      constraintsToData.put(constraint, entry.getValue());
    }

    return constraintsToData;
  }

  protected Map<FeatureExpr, List<Double>> parseConstraintsToCI(
      Map<String, List<Double>> localModel) {
    Map<FeatureExpr, List<Double>> constraintsToData = new HashMap<>();

    for (Map.Entry<String, List<Double>> entry : localModel.entrySet()) {
      FeatureExpr constraint = getConstraint(entry.getKey());
      constraintsToData.put(constraint, entry.getValue());
    }

    return constraintsToData;
  }

  protected Map<PerformanceEntry, FeatureExpr> getPerfEntryToExecConstraint() {
    return perfEntryToExecConstraint;
  }
}

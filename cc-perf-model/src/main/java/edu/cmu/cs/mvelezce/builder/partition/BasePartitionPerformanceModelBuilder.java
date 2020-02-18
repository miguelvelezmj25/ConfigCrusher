package edu.cmu.cs.mvelezce.builder.partition;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.partition.PartitionLocalPerformanceModel;

import java.util.*;

public abstract class BasePartitionPerformanceModelBuilder
    extends BasePerformanceModelBuilder<Partitioning, Partition> {

  public static final double EMPTY_DOUBLE = 0.0;
  public static final String EMPTY_HUMAN = "";

  private static final Map<String, Partition> STRINGS_TO_PARTITIONS = new HashMap<>();

  private final Map<PerformanceEntry, Partition> perfEntryToExecConfigPartition = new HashMap<>();

  public BasePartitionPerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, Partitioning> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName, options, regionsToData, performanceEntries);
  }

  private static Partition getPartition(String string) {
    Partition partition = STRINGS_TO_PARTITIONS.get(string);

    if (partition == null) {
      partition = new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, string));
      STRINGS_TO_PARTITIONS.put(string, partition);
    }

    return partition;
  }

  @Override
  protected Set<LocalPerformanceModel<Partition>> buildLocalModels() {
    this.mapPerfEntryToExecPartition();

    return super.buildLocalModels();
  }

  private void mapPerfEntryToExecPartition() {
    for (PerformanceEntry entry : this.getPerformanceEntries()) {
      Set<String> config = entry.getConfiguration();
      String configPartition = ConstraintUtils.parseAsConstraint(config, this.getOptions());
      Partition partition =
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, configPartition));
      this.perfEntryToExecConfigPartition.put(entry, partition);
    }
  }

  @Override
  protected LocalPerformanceModel<Partition> buildEmptyLocalModel(
      Map.Entry<JavaRegion, Partitioning> entry) {
    Map<Partition, Double> model = this.addPartitionEntries(entry.getValue());
    Map<Partition, Double> modelToMin = this.addPartitionEntries(entry.getValue());
    Map<Partition, Double> modelToMax = this.addPartitionEntries(entry.getValue());
    Map<Partition, Double> modelToDiff = this.addPartitionEntries(entry.getValue());
    Map<Partition, Double> modelToSampleVariance = this.addPartitionEntries(entry.getValue());
    Map<Partition, List<Double>> modelToConfidenceInterval =
        this.addPartitionEntriesCI(entry.getValue());
    Map<Partition, Double> modelToCoefficientOfVariation =
        this.addPartitionEntries(entry.getValue());

    Map<Partition, String> modelToPerfHumanReadable =
        this.addPartitionEntriesHuman(entry.getValue());
    Map<Partition, String> modelToMinHumanReadable =
        this.addPartitionEntriesHuman(entry.getValue());
    Map<Partition, String> modelToMaxHumanReadable =
        this.addPartitionEntriesHuman(entry.getValue());
    Map<Partition, String> modelToDiffHumanReadable =
        this.addPartitionEntriesHuman(entry.getValue());
    Map<Partition, String> modelToSampleVarianceHumanReadable =
        this.addPartitionEntriesHuman(entry.getValue());
    Map<Partition, List<String>> modelToConfidenceIntervalHumanReadable =
        this.addPartitionEntriesCIHuman(entry.getValue());
    Map<Partition, String> modelToCoefficientOfVariationHumanReadable =
        this.addPartitionEntriesHuman(entry.getValue());

    return new PartitionLocalPerformanceModel(
        entry.getKey().getId(),
        model,
        modelToMin,
        modelToMax,
        modelToDiff,
        modelToSampleVariance,
        modelToConfidenceInterval,
        modelToCoefficientOfVariation,
        modelToPerfHumanReadable,
        modelToMinHumanReadable,
        modelToMaxHumanReadable,
        modelToDiffHumanReadable,
        modelToSampleVarianceHumanReadable,
        modelToConfidenceIntervalHumanReadable,
        modelToCoefficientOfVariationHumanReadable);
  }

  private Map<Partition, List<String>> addPartitionEntriesCIHuman(Partitioning partitioning) {
    Map<Partition, List<String>> model = new HashMap<>();

    for (Partition partition : partitioning.getPartitions()) {
      model.put(partition, new ArrayList<>());
    }

    return model;
  }

  private Map<Partition, String> addPartitionEntriesHuman(Partitioning partitioning) {
    Map<Partition, String> model = new HashMap<>();

    for (Partition partition : partitioning.getPartitions()) {
      model.put(partition, EMPTY_HUMAN);
    }

    return model;
  }

  private Map<Partition, List<Double>> addPartitionEntriesCI(Partitioning partitioning) {
    Map<Partition, List<Double>> model = new HashMap<>();

    for (Partition partition : partitioning.getPartitions()) {
      model.put(partition, new ArrayList<>());
    }

    return model;
  }

  private Map<Partition, Double> addPartitionEntries(Partitioning partitioning) {
    Map<Partition, Double> model = new HashMap<>();

    for (Partition partition : partitioning.getPartitions()) {
      model.put(partition, EMPTY_DOUBLE);
    }

    return model;
  }

  protected Map<Partition, String> parsePartitionsToHumanReadableData(
      Map<String, String> localHumanReadableData) {
    Map<Partition, String> partitionsToHumanReadableData = new HashMap<>();

    for (Map.Entry<String, String> entry : localHumanReadableData.entrySet()) {
      Partition partition = getPartition(entry.getKey());
      partitionsToHumanReadableData.put(partition, entry.getValue());
    }

    return partitionsToHumanReadableData;
  }

  protected Map<Partition, List<String>> parsePartitionsToHumanReadableCI(
      Map<String, List<String>> localHumanReadableData) {
    Map<Partition, List<String>> partitionsToHumanReadableData = new HashMap<>();

    for (Map.Entry<String, List<String>> entry : localHumanReadableData.entrySet()) {
      Partition partition = getPartition(entry.getKey());
      partitionsToHumanReadableData.put(partition, entry.getValue());
    }

    return partitionsToHumanReadableData;
  }

  protected Map<Partition, Double> parsePartitionsToData(Map<String, Double> localModel) {
    Map<Partition, Double> partitionsToData = new HashMap<>();

    for (Map.Entry<String, Double> entry : localModel.entrySet()) {
      Partition partition = getPartition(entry.getKey());
      partitionsToData.put(partition, entry.getValue());
    }

    return partitionsToData;
  }

  protected Map<Partition, List<Double>> parsePartitionsToCI(Map<String, List<Double>> localModel) {
    Map<Partition, List<Double>> partitionsToData = new HashMap<>();

    for (Map.Entry<String, List<Double>> entry : localModel.entrySet()) {
      Partition partition = getPartition(entry.getKey());
      partitionsToData.put(partition, entry.getValue());
    }

    return partitionsToData;
  }

  protected Map<PerformanceEntry, Partition> getPerfEntryToExecConfigPartition() {
    return perfEntryToExecConfigPartition;
  }
}

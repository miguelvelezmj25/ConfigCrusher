package edu.cmu.cs.mvelezce.exhaustive.model.partition;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.model.partition.PartitionLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ExhaustiveLocalPerformanceModel extends PartitionLocalPerformanceModel {

  public ExhaustiveLocalPerformanceModel(
      UUID region,
      Map<Partition, Double> model,
      Map<Partition, Double> modelToMin,
      Map<Partition, Double> modelToMax,
      Map<Partition, Double> modelToDiff,
      Map<Partition, Double> modelToSampleVariance,
      Map<Partition, List<Double>> modelToConfidenceInterval,
      Map<Partition, Double> modelToCoefficientOfVariation,
      Map<Partition, String> modelToPerfHumanReadable,
      Map<Partition, String> modelToMinHumanReadable,
      Map<Partition, String> modelToMaxHumanReadable,
      Map<Partition, String> modelToDiffHumanReadable,
      Map<Partition, String> modelToSampleVarianceHumanReadable,
      Map<Partition, List<String>> modelToConfidenceIntervalHumanReadable,
      Map<Partition, String> modelToCoefficientOfVariationHumanReadable) {
    super(
        region,
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

    System.err.println(
        "If this is an exhaustive model maybe I could check for equality when evaluating a config and exclude featureExpr that have already been used");
  }

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    Partition configAsPartition = getConfigAsPartition(config, options);

    if (this.getModel().containsKey(configAsPartition)) {
      return this.getModel().get(configAsPartition);
    }

    return super.evaluate(config, options);
  }

  @Override
  public double evaluateVariance(Set<String> config, List<String> options) {
    Partition configAsPartition = getConfigAsPartition(config, options);

    if (this.getModel().containsKey(configAsPartition)) {
      return this.getModelToSampleVariance().get(configAsPartition);
    }

    return super.evaluateVariance(config, options);
  }

  @Override
  public List<Double> evaluateConfidenceInterval(Set<String> config, List<String> options) {
    Partition configAsPartition = getConfigAsPartition(config, options);

    if (this.getModel().containsKey(configAsPartition)) {
      return this.getModelToConfidenceInterval().get(configAsPartition);
    }

    return super.evaluateConfidenceInterval(config, options);
  }
}

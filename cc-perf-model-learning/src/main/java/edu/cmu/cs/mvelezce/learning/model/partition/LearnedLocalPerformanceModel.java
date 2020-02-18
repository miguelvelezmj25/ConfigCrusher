package edu.cmu.cs.mvelezce.learning.model.partition;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.model.partition.PartitionLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class LearnedLocalPerformanceModel extends PartitionLocalPerformanceModel {

  public LearnedLocalPerformanceModel(
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
  }

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    Partition configAsPartition = getConfigAsPartition(config, options);
    double time = 0;

    for (Map.Entry<Partition, Double> entry : this.getModel().entrySet()) {
      if (!configAsPartition
          .getFeatureExpr()
          .implies(entry.getKey().getFeatureExpr())
          .isTautology()) {
        continue;
      }

      time += entry.getValue();
    }

    return Math.max(time, 0.0);
  }
}

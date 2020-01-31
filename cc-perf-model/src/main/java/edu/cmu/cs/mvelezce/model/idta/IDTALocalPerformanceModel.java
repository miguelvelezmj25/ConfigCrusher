package edu.cmu.cs.mvelezce.model.idta;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.model.partition.PartitionLocalPerformanceModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IDTALocalPerformanceModel extends PartitionLocalPerformanceModel {

  public IDTALocalPerformanceModel(
      UUID region,
      Map<Partition, Double> model,
      Map<Partition, Double> modelToMin,
      Map<Partition, Double> modelToMax,
      Map<Partition, Double> modelToDiff,
      Map<Partition, Double> modelToSampleVariance,
      Map<Partition, List<Double>> modelToConfidenceInterval,
      Map<Partition, String> modelToPerfHumanReadable,
      Map<Partition, String> modelToMinHumanReadable,
      Map<Partition, String> modelToMaxHumanReadable,
      Map<Partition, String> modelToDiffHumanReadable,
      Map<Partition, String> modelToSampleVarianceHumanReadable,
      Map<Partition, List<String>> modelToConfidenceIntervalHumanReadable) {
    super(
        region,
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

  @Override
  public double evaluate(Set<String> config, List<String> options) {
    Partition configAsPartition =
        PartitionLocalPerformanceModel.getConfigAsPartition(config, options);
    double time = 0;
    int entriesCovered = 0;

    for (Map.Entry<Partition, Double> entry : this.getModel().entrySet()) {
      if (!configAsPartition
          .getFeatureExpr()
          .implies(entry.getKey().getFeatureExpr())
          .isTautology()) {
        continue;
      }

      time += entry.getValue();
      entriesCovered++;
    }

    if (entriesCovered == 0) {
      return time;
    }

    return time / entriesCovered;
  }
}

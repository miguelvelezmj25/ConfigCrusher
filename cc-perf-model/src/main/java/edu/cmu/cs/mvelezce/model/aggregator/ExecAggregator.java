package edu.cmu.cs.mvelezce.model.aggregator;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExecAggregator<T> {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0000000");

  public Set<LocalPerformanceModel<T>> process(
      Set<MultiEntryLocalPerformanceModel<T>> multiEntryLocalPerformanceModels) {
    Set<LocalPerformanceModel<T>> localPerformanceModels = new HashSet<>();

    for (MultiEntryLocalPerformanceModel<T> multiEntryLocalPerformanceModel :
        multiEntryLocalPerformanceModels) {
      LocalPerformanceModel<T> localModel = this.averageTimes(multiEntryLocalPerformanceModel);
      localPerformanceModels.add(localModel);
    }

    return localPerformanceModels;
  }

  private LocalPerformanceModel<T> averageTimes(
      MultiEntryLocalPerformanceModel<T> multiEntryLocalPerformanceModels) {
    System.out.println("Region " + multiEntryLocalPerformanceModels.getRegion());
    Map<T, SummaryStatistics> localStats = new HashMap<>();

    for (Map.Entry<T, Set<Double>> multiEntryModel :
        multiEntryLocalPerformanceModels.getModel().entrySet()) {
      SummaryStatistics stats = new SummaryStatistics();

      for (double entry : multiEntryModel.getValue()) {
        stats.addValue(entry);
      }

      localStats.put(multiEntryModel.getKey(), stats);
    }

    Map<T, Double> model = this.getModel(localStats);
    Map<T, Double> modelToMin = this.getModelToMin(localStats);
    Map<T, Double> modelToMax = this.getModelToMax(localStats);
    Map<T, Double> modelToDiff = this.getModelToDiff(modelToMin, modelToMax);
    //    Map<T, Double> modelToSampleVariance = this.getModelToSampleVariance(localStats);
    //    Map<T, List<Double>> modelToConfidenceInterval =
    // this.getModelToConfidenceInterval(localStats);

    return new LocalPerformanceModel<T>(
        multiEntryLocalPerformanceModels.getRegion(),
        model,
        modelToMin,
        modelToMax,
        modelToDiff,
        this.toHumandReadable(model),
        this.toHumandReadable(modelToMin),
        this.toHumandReadable(modelToMax),
        this.toHumandReadable(modelToDiff));
  }

  //  private Map<T, List<Double>> getModelToConfidenceInterval(Map<T, SummaryStatistics>
  // localStats) {
  //    Map<T, List<Double>> modelToConfidenceInterval = new HashMap<>();
  //
  //    for (Map.Entry<T, SummaryStatistics> entry : localStats.entrySet()) {
  //      SummaryStatistics stats = entry.getValue();
  //
  //      List<Double> confidenceInterval = new ArrayList<>();
  //
  //      if (stats.getN() <= 1) {
  //        confidenceInterval.add(-1.0);
  //        confidenceInterval.add(-1.0);
  //      } else {
  //        TDistribution tDist = new TDistribution(stats.getN() - 1);
  //        double critVal = tDist.inverseCumulativeProbability(1.0 - (1 - 0.95) / 2);
  //        double ciValue = critVal * Math.sqrt(stats.getVariance()) / Math.sqrt(stats.getN());
  //        double lowerCI = Math.max(0, stats.getMean() - ciValue);
  //        confidenceInterval.add(lowerCI);
  //        confidenceInterval.add(stats.getMean() + ciValue);
  //      }
  //
  //      modelToConfidenceInterval.put(entry.getKey(), confidenceInterval);
  //    }
  //
  //    return modelToConfidenceInterval;
  //  }
  //
  //  private Map<T, Double> getModelToSampleVariance(Map<T, SummaryStatistics> localStats) {
  //    Map<T, Double> regionsToSampleVariance = new HashMap<>();
  //
  //    for (Map.Entry<T, SummaryStatistics> entry : localStats.entrySet()) {
  //      System.out.println(entry.getValue().getVariance());
  //      regionsToSampleVariance.put(entry.getKey(), entry.getValue().getVariance());
  //    }
  //
  //    return regionsToSampleVariance;
  //  }

  private Map<T, Double> getModelToMax(Map<T, SummaryStatistics> localStats) {
    Map<T, Double> regionsToMax = new HashMap<>();

    for (Map.Entry<T, SummaryStatistics> entry : localStats.entrySet()) {
      regionsToMax.put(entry.getKey(), entry.getValue().getMean());
    }

    return regionsToMax;
  }

  private Map<T, Double> getModel(Map<T, SummaryStatistics> localStats) {
    Map<T, Double> regionsToPerf = new HashMap<>();

    for (Map.Entry<T, SummaryStatistics> entry : localStats.entrySet()) {
      regionsToPerf.put(entry.getKey(), entry.getValue().getMean());
    }

    return regionsToPerf;
  }

  private Map<T, String> toHumandReadable(Map<T, Double> model) {
    Map<T, String> modelToHumanReadable = new HashMap<>();

    for (Map.Entry<T, Double> entry : model.entrySet()) {
      modelToHumanReadable.put(entry.getKey(), entry.getValue().toString());
    }

    for (Map.Entry<T, String> entry : modelToHumanReadable.entrySet()) {
      double data = Double.parseDouble(entry.getValue());
      data = data / 1E9;
      modelToHumanReadable.put(entry.getKey(), DECIMAL_FORMAT.format(data));
    }

    return modelToHumanReadable;
  }

  private Map<T, Double> getModelToDiff(Map<T, Double> modelToMin, Map<T, Double> modelToMax) {
    Map<T, Double> modelToDiff = new HashMap<>();

    for (T header : modelToMin.keySet()) {
      modelToDiff.put(header, 0.0);
    }

    for (T header : modelToDiff.keySet()) {
      double max = modelToMax.get(header);
      double min = modelToMin.get(header);
      double diff = max - min;

      if (diff >= 1E9) {
        System.err.println(
            "The difference between the min and max executions of header "
                + header
                + " is greater than 1 sec. It is "
                + (diff / 1E9));
      }

      modelToDiff.put(header, diff);
    }

    return modelToDiff;
  }

  private Map<T, Double> getModelToMin(Map<T, SummaryStatistics> localStats) {
    Map<T, Double> regionsToMin = new HashMap<>();

    for (Map.Entry<T, SummaryStatistics> entry : localStats.entrySet()) {
      regionsToMin.put(entry.getKey(), entry.getValue().getMean());
    }

    return regionsToMin;
  }
}

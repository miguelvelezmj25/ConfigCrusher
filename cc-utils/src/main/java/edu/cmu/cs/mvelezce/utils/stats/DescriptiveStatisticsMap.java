package edu.cmu.cs.mvelezce.utils.stats;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescriptiveStatisticsMap<T> {

  private final Map<T, DescriptiveStatistics> map = new HashMap<>();

  public void putIfAbsent(T entry) {
    this.map.putIfAbsent(entry, new DescriptiveStatistics());
  }

  public DescriptiveStatistics get(T region) {
    return this.map.get(region);
  }

  public Map<T, DescriptiveStatistics> getMap() {
    return map;
  }

  public Map<T, Double> getEntriesToData() {
    Map<T, Double> regionsToData = new HashMap<>();

    for (Map.Entry<T, DescriptiveStatistics> entry : this.map.entrySet()) {
      regionsToData.put(entry.getKey(), entry.getValue().getPercentile(50.0));
    }

    return regionsToData;
  }

  public Map<T, Double> getEntriesToMin() {
    Map<T, Double> regionsToMin = new HashMap<>();

    for (Map.Entry<T, DescriptiveStatistics> entry : this.map.entrySet()) {
      regionsToMin.put(entry.getKey(), entry.getValue().getMin());
    }

    return regionsToMin;
  }

  public Map<T, Double> getEntriesToMax() {
    Map<T, Double> regionsToMax = new HashMap<>();

    for (Map.Entry<T, DescriptiveStatistics> entry : this.map.entrySet()) {
      regionsToMax.put(entry.getKey(), entry.getValue().getMax());
    }

    return regionsToMax;
  }

  public Map<T, Double> getEntriesToDiff(Map<T, Double> entriesToMin, Map<T, Double> entriesToMax) {
    Map<T, Double> entriesToDiff = new HashMap<>();

    for (T entry : entriesToMin.keySet()) {
      entriesToDiff.put(entry, 0.0);
    }

    for (T entry : entriesToDiff.keySet()) {
      double max = entriesToMax.get(entry);
      double min = entriesToMin.get(entry);
      double diff = max - min;

      if (diff >= 1E9) {
        System.err.println(
            "The difference between the min and max executions of entry "
                + entry
                + " is greater than 1 sec. It is "
                + (diff / 1E9)
                + " in ["
                + (min / 1E9)
                + " - "
                + (max / 1E9)
                + "]");
      }

      entriesToDiff.put(entry, diff);
    }

    return entriesToDiff;
  }

  public Map<T, List<Double>> getEntriesToConfidenceInterval() {
    Map<T, List<Double>> entriesToConfidenceInterval = new HashMap<>();

    for (Map.Entry<T, DescriptiveStatistics> entry : this.map.entrySet()) {
      DescriptiveStatistics stats = entry.getValue();

      if (stats.getN() == 1) {
        continue;
      }

      TDistribution tDist = new TDistribution(stats.getN() - 1);
      double critVal = tDist.inverseCumulativeProbability(1.0 - (1 - 0.95) / 2);
      double ciValue = critVal * Math.sqrt(stats.getVariance()) / Math.sqrt(stats.getN());
      double lowerCI = Math.max(0, stats.getMean() - ciValue);
      double higherCI = stats.getMean() + ciValue;

      if ((higherCI - lowerCI) >= 1E9 & stats.getN() >= 3) {
        System.err.println(
            "The difference between the lower and higher confidence interval bounds of region "
                + entry.getKey()
                + " is greater than 1 sec. It is "
                + ((higherCI - lowerCI) / 1E9)
                + " in ["
                + (stats.getMin() / 1E9)
                + " - "
                + (stats.getMax() / 1E9)
                + "]");
      }

      List<Double> confidenceInterval = new ArrayList<>();
      confidenceInterval.add(lowerCI);
      confidenceInterval.add(higherCI);
      entriesToConfidenceInterval.put(entry.getKey(), confidenceInterval);
    }

    return entriesToConfidenceInterval;
  }

  public Map<T, Double> getEntriesToSampleVariance() {
    Map<T, Double> entriesToSampleVariance = new HashMap<>();

    for (Map.Entry<T, DescriptiveStatistics> entry : this.map.entrySet()) {
      entriesToSampleVariance.put(entry.getKey(), entry.getValue().getVariance());
    }

    return entriesToSampleVariance;
  }
}

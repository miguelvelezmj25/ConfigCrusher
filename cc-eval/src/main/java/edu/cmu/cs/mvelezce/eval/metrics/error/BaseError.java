package edu.cmu.cs.mvelezce.eval.metrics.error;

import org.apache.commons.math3.stat.descriptive.rank.Median;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseError implements Error<Double> {

  private final List<Double> entries = new ArrayList<>();

  @Override
  public List<Double> getEntries() {
    return entries;
  }

  @Override
  public double getMin() {
    return Collections.min(this.entries);
  }

  @Override
  public double getMax() {
    return Collections.max(this.entries);
  }

  @Override
  public double getArithmeticMean() {
    double sum = this.getSum();

    return sum / this.entries.size();
  }

  @Override
  public double getMedian() {
    Median median = new Median();
    double[] values = this.entries.stream().mapToDouble(Double::doubleValue).toArray();

    return median.evaluate(values);
  }

  @Override
  public double getSum() {
    double sum = 0.0;

    for (Double entry : this.entries) {
      sum += entry;
    }

    return sum;
  }

  @Override
  public double getGeometricMean() {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public double getError() {
    throw new UnsupportedOperationException(
        "Metric does not have a general notion of an \"error\"");
  }
}

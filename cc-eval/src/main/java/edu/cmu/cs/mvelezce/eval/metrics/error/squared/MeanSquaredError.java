package edu.cmu.cs.mvelezce.eval.metrics.error.squared;

import edu.cmu.cs.mvelezce.eval.metrics.error.BaseError;

public final class MeanSquaredError extends BaseError {

  @Override
  public double getError() {
    double squaredSum = this.getSquaredSum();

    return squaredSum / this.getEntries().size();
  }

  private double getSquaredSum() {
    double squaredSum = 0.0;

    for (Double entry : this.getEntries()) {
      squaredSum += Math.pow(entry, 2);
    }

    return squaredSum;
  }
}

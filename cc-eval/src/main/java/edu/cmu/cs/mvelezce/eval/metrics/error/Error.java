package edu.cmu.cs.mvelezce.eval.metrics.error;

import edu.cmu.cs.mvelezce.eval.metrics.Metric;

public interface Error<T> extends Metric<T> {

  double getError();
}

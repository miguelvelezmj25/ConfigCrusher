package edu.cmu.cs.mvelezce.eval.metrics;

import java.util.List;

public interface Metric<T> {

    List<T> getEntries();

    double getMin();

    double getMax();

    double getSum();

    double getArithmeticMean();

    double getGeometricMean();

}

package edu.cmu.cs.mvelezce.model;

import java.util.Set;

public interface IPerformanceModel<T> {

  Set<LocalPerformanceModel<T>> getLocalModels();

  double evaluate(String config);
}

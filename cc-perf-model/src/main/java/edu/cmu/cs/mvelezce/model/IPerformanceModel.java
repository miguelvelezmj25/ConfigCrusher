package edu.cmu.cs.mvelezce.model;

import java.util.List;
import java.util.Set;

public interface IPerformanceModel<T> {

  Set<LocalPerformanceModel<T>> getLocalModels();

  double evaluate(Set<String> config, List<String> options);
}

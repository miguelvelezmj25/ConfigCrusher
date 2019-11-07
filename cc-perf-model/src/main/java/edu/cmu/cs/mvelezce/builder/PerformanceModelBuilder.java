package edu.cmu.cs.mvelezce.builder;

import edu.cmu.cs.mvelezce.model.PerformanceModel;

public interface PerformanceModelBuilder<T> {

  PerformanceModel createModel();
}

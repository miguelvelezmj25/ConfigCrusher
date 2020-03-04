package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class RunBenchCIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  private static final double INTERCEPT = 0;
  private static final double SLOPE = 1;
  private static final double R_SQUARED = 0;

  public RunBenchCIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> RunBenchCIDTAPerformanceModel<T> toRunBenchCIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new RunBenchCIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

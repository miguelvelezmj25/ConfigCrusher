package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class BerkeleyIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  private static final double INTERCEPT = -0.0983;
  private static final double SLOPE = 1.0677;
  private static final double R_SQUARED = 0.9988;

  public BerkeleyIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> BerkeleyIDTAPerformanceModel<T> toBerkeleyIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new BerkeleyIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

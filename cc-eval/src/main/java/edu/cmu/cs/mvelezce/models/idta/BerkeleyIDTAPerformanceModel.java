package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class BerkeleyIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  // all configs
  //  private static final double INTERCEPT = -0.0983;
  //  private static final double SLOPE = 1.0677;
  //  private static final double R_SQUARED = 0.9988;

  // 5 random configs
  //  private static final double INTERCEPT = -0.0754;
  //  private static final double SLOPE = 1.0785;
  //  private static final double R_SQUARED = 0.9997;

  //      private static final double INTERCEPT = -0.0522;
  //      private static final double SLOPE = 1.0857;
  //      private static final double R_SQUARED = 0.9992;

  //      private static final double INTERCEPT = 0.9102;
  //      private static final double SLOPE = 0.9534;
  //      private static final double R_SQUARED = 0.9928;

  //      private static final double INTERCEPT = -0.0454;
  //      private static final double SLOPE = 1.0642;
  //      private static final double R_SQUARED = 0.9991;

  private static final double INTERCEPT = 0.1364;
  private static final double SLOPE = 1.0311;
  private static final double R_SQUARED = 0.9994;

  public BerkeleyIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> BerkeleyIDTAPerformanceModel<T> toBerkeleyIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new BerkeleyIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

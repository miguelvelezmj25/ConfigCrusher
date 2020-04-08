package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class BerkeleyIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  //  private static final double INTERCEPT = 0;
  //  private static final double SLOPE = 1;
  //  private static final double R_SQUARED = 0;

  // all configs
  //  private static final double INTERCEPT = -0.0983;
  //  private static final double SLOPE = 1.0677;
  //  private static final double R_SQUARED = 0.9988;

  // 5 random configs
  //    private static final double INTERCEPT = -0.3169;
  //    private static final double SLOPE = 1.0933;
  //    private static final double R_SQUARED = 0.9995;

  //        private static final double INTERCEPT = -0.0607;
  //        private static final double SLOPE = 1.0598;
  //        private static final double R_SQUARED = 0.9979;

  //        private static final double INTERCEPT = 0.2838;
  //        private static final double SLOPE = 1.0261;
  //        private static final double R_SQUARED = 0.9985;

  //        private static final double INTERCEPT = 0.2404;
  //        private static final double SLOPE = 1.0316;
  //        private static final double R_SQUARED = 0.9996;

  private static final double INTERCEPT = -0.3195;
  private static final double SLOPE = 1.0881;
  private static final double R_SQUARED = 0.9998;

  public BerkeleyIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> BerkeleyIDTAPerformanceModel<T> toBerkeleyIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new BerkeleyIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

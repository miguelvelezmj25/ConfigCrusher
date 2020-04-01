package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class ConvertIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  // Numbers when we analyzed the program with a small number of configs
  //  private static final double INTERCEPT = 1.9437;
  //  private static final double SLOPE = 1.2649;
  //  private static final double R_SQUARED = 0.9976;

  // Numbers when we analyzed the program with all configs, but the errors increase
  //    private static final double INTERCEPT = 0.4086;
  //    private static final double SLOPE = 1.2177;
  //    private static final double R_SQUARED = 0.9920;

  // 5 random configs
  //  private static final double INTERCEPT = -0.9402;
  //  private static final double SLOPE = 1.3671;
  //  private static final double R_SQUARED = 0.9997;

  //    private static final double INTERCEPT = -1.0248;
  //    private static final double SLOPE = 1.391;
  //    private static final double R_SQUARED = 1;

  //    private static final double INTERCEPT = -0.5629;
  //    private static final double SLOPE = 1.2063;
  //    private static final double R_SQUARED = 0.9996;

  //    private static final double INTERCEPT = -1.4355;
  //    private static final double SLOPE = 1.4494;
  //    private static final double R_SQUARED = 0.9998;

  private static final double INTERCEPT = -0.9346;
  private static final double SLOPE = 1.3905;
  private static final double R_SQUARED = 1;

  public ConvertIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> ConvertIDTAPerformanceModel<T> toConvertIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new ConvertIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

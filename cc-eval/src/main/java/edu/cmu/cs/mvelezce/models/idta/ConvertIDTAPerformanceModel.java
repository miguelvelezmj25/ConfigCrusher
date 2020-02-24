package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class ConvertIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  private static final double INTERCEPT = 1.9437;
  private static final double SLOPE = 1.2649;
  private static final double R_SQUARED = 0.9976;

  public ConvertIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> ConvertIDTAPerformanceModel<T> toConvertIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new ConvertIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

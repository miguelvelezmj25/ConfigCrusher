package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class LuceneIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  private static final double INTERCEPT = 1.4621;
  private static final double SLOPE = 1.0226;
  private static final double R_SQUARED = 0.9920;

  public LuceneIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> LuceneIDTAPerformanceModel<T> toLuceneIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new LuceneIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

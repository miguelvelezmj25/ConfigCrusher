package edu.cmu.cs.mvelezce.models.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.idta.IDTAPerformanceModel;

import java.util.Set;

public class LuceneIDTAPerformanceModel<T> extends IDTAPerformanceModel<T> {

  // all configs
  //  private static final double INTERCEPT = 1.4621;
  //  private static final double SLOPE = 1.0226;
  //  private static final double R_SQUARED = 0.9920;

  // 5 random configs
  //  private static final double INTERCEPT = 1.0230;
  //  private static final double SLOPE = 1.0434;
  //  private static final double R_SQUARED = 0.9949;

  //    private static final double INTERCEPT = 2.6586;
  //    private static final double SLOPE = 0.9795;
  //    private static final double R_SQUARED = 0.9871;

  //    private static final double INTERCEPT = 0.6429;
  //    private static final double SLOPE = 1.0533;
  //    private static final double R_SQUARED = 0.9933;

  //    private static final double INTERCEPT = 2.3249;
  //    private static final double SLOPE = 0.9943;
  //    private static final double R_SQUARED = 0.9911;

  private static final double INTERCEPT = -2.5065;
  private static final double SLOPE = 1.1354;
  private static final double R_SQUARED = 0.9883;

  public LuceneIDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    super(localModels, INTERCEPT, SLOPE);
  }

  public static <T> LuceneIDTAPerformanceModel<T> toLuceneIDTAPerformanceModel(
      PerformanceModel<T> performanceModel) {
    return new LuceneIDTAPerformanceModel<>(performanceModel.getLocalModels());
  }
}

package edu.cmu.cs.mvelezce.model.idta;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.util.List;
import java.util.Set;

public class IDTAPerformanceModel<T> extends PerformanceModel<T> {

  private final double intercept;
  private final double slope;

  public IDTAPerformanceModel(
      Set<LocalPerformanceModel<T>> localModels, double intercept, double slope) {
    super(localModels);

    this.intercept = intercept * 1E9;
    this.slope = slope;
  }

  public IDTAPerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    this(localModels, 0, 1);
  }

  @Override
  public long evaluate(Set<String> config, List<String> options) {
    long time = 0;

    for (LocalPerformanceModel<T> localModel : this.getLocalModels()) {
      time += localModel.evaluate(config, options);
    }

    return (long) ((time - this.intercept) / slope);
  }
}

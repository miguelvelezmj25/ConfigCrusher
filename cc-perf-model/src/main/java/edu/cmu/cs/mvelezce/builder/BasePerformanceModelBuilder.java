package edu.cmu.cs.mvelezce.builder;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.util.Map;
import java.util.Set;

public class BasePerformanceModelBuilder<T> implements PerformanceModelBuilder<T> {

  private final String programName;
  private final Map<JavaRegion, T> regionsToData;
  private final Set<PerformanceEntry> performanceEntries;

  public BasePerformanceModelBuilder(
      String programName,
      Map<JavaRegion, T> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    this.programName = programName;
    this.regionsToData = regionsToData;
    this.performanceEntries = performanceEntries;
  }

  @Override
  public PerformanceModel createModel() {
    throw new UnsupportedOperationException("implement");
  }
}

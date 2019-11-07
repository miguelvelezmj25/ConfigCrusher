package edu.cmu.cs.mvelezce.builder;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.io.File;
import java.util.Map;
import java.util.Set;

public abstract class BasePerformanceModelBuilder<E> extends BaseAnalysis<PerformanceModel>
    implements PerformanceModelBuilder<PerformanceModel, E> {

  private final Map<JavaRegion, E> regionsToData;
  private final Set<PerformanceEntry> performanceEntries;

  public BasePerformanceModelBuilder(
      String programName,
      Map<JavaRegion, E> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName);

    this.regionsToData = regionsToData;
    this.performanceEntries = performanceEntries;
  }

  @Override
  public PerformanceModel analyze() {
    this.buildLocalModels();

    throw new UnsupportedOperationException("implement");
  }

  protected abstract void buildLocalModels();

  @Override
  public void writeToFile(PerformanceModel results) {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public PerformanceModel readFromFile(File file) {
    throw new UnsupportedOperationException("implement");
  }

  public Map<JavaRegion, E> getRegionsToData() {
    return regionsToData;
  }

  public Set<PerformanceEntry> getPerformanceEntries() {
    return performanceEntries;
  }
}

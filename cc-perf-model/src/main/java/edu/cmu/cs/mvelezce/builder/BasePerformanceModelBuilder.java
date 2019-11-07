package edu.cmu.cs.mvelezce.builder;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BasePerformanceModelBuilder<E> extends BaseAnalysis<PerformanceModel>
    implements PerformanceModelBuilder<PerformanceModel, E> {

  private final List<String> options;
  private final Map<JavaRegion, E> regionsToData;
  private final Set<PerformanceEntry> performanceEntries;

  public BasePerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, E> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName);

    this.options = options;
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

  protected List<String> getOptions() {
    return options;
  }

  protected Map<JavaRegion, E> getRegionsToData() {
    return regionsToData;
  }

  protected Set<PerformanceEntry> getPerformanceEntries() {
    return performanceEntries;
  }
}

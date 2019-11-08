package edu.cmu.cs.mvelezce.builder;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.aggregator.ExecAggregator;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BasePerformanceModelBuilder<D, RD> extends BaseAnalysis<PerformanceModel>
    implements PerformanceModelBuilder<PerformanceModel, D> {

  private final List<String> options;
  private final Map<JavaRegion, D> regionsToData;
  private final Set<PerformanceEntry> performanceEntries;
  private final ExecAggregator<RD> execAggregator = new ExecAggregator<>();

  public BasePerformanceModelBuilder(
      String programName,
      List<String> options,
      Map<JavaRegion, D> regionsToData,
      Set<PerformanceEntry> performanceEntries) {
    super(programName);

    this.options = options;
    this.regionsToData = regionsToData;
    this.performanceEntries = performanceEntries;
  }

  @Override
  public PerformanceModel analyze() {
    Set<MultiEntryLocalPerformanceModel<RD>> multiEntryLocalPerformanceModels =
        this.buildMultiEntryLocalModels();
    Set<LocalPerformanceModel<RD>> localPerformanceModels =
        this.execAggregator.process(multiEntryLocalPerformanceModels);

    return new PerformanceModel<RD>(localPerformanceModels);
  }

  protected abstract Set<MultiEntryLocalPerformanceModel<RD>> buildMultiEntryLocalModels();

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

  protected Map<JavaRegion, D> getRegionsToData() {
    return regionsToData;
  }

  protected Set<PerformanceEntry> getPerformanceEntries() {
    return performanceEntries;
  }
}

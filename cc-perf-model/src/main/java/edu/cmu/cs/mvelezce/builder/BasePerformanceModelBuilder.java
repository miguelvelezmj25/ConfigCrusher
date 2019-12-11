package edu.cmu.cs.mvelezce.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.MultiEntryLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.aggregator.ExecAggregator;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BasePerformanceModelBuilder<D, RD> extends BaseAnalysis<PerformanceModel<RD>>
    implements PerformanceModelBuilder<PerformanceModel<RD>, D> {

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
  public PerformanceModel<RD> analyze() {
    Set<MultiEntryLocalPerformanceModel<RD>> multiEntryLocalPerformanceModels =
        this.buildMultiEntryLocalModels();
    Set<LocalPerformanceModel<RD>> localPerformanceModels =
        this.execAggregator.process(multiEntryLocalPerformanceModels);

    return new PerformanceModel<>(localPerformanceModels);
  }

  protected abstract void populateMultiEntryLocalModel(
      MultiEntryLocalPerformanceModel<RD> multiEntryLocalModel);

  protected abstract MultiEntryLocalPerformanceModel<RD> buildEmptyMultiEntryLocalModel(
      Map.Entry<JavaRegion, D> entry);

  protected Set<MultiEntryLocalPerformanceModel<RD>> buildMultiEntryLocalModels() {
    Set<MultiEntryLocalPerformanceModel<RD>> localModels = new HashSet<>();

    for (Map.Entry<JavaRegion, D> entry : this.regionsToData.entrySet()) {
      MultiEntryLocalPerformanceModel<RD> multiEntryLocalModel =
          this.buildEmptyMultiEntryLocalModel(entry);
      this.populateMultiEntryLocalModel(multiEntryLocalModel);
      localModels.add(multiEntryLocalModel);
    }

    return localModels;
  }

  @Override
  public void writeToFile(PerformanceModel<RD> results) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, results);
  }

  protected List<String> getOptions() {
    return options;
  }

  protected Set<PerformanceEntry> getPerformanceEntries() {
    return performanceEntries;
  }
}

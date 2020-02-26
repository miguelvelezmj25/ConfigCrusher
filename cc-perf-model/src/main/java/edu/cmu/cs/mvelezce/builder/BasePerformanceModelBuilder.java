package edu.cmu.cs.mvelezce.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <D> The data that regions have (e.g., partitions)
 * @param <RD> The data of each entry in a region (e.g., partition)
 */
public abstract class BasePerformanceModelBuilder<D, RD> extends BaseAnalysis<PerformanceModel<RD>>
    implements PerformanceModelBuilder<PerformanceModel<RD>, D> {

  private final List<String> options;
  private final Map<JavaRegion, D> regionsToData;
  private final Set<PerformanceEntry> performanceEntries;

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
    Set<LocalPerformanceModel<RD>> localPerformanceModels = this.buildLocalModels();

    return new PerformanceModel<>(localPerformanceModels);
  }

  @Override
  public PerformanceModel<RD> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    File file = new File(this.outputDir());

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);
    }

    PerformanceModel<RD> perfModel = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(perfModel);
    }

    return perfModel;
  }

  protected abstract void populateLocalModel(LocalPerformanceModel<RD> localModel);

  protected abstract LocalPerformanceModel<RD> buildEmptyLocalModel(Map.Entry<JavaRegion, D> entry);

  protected Set<LocalPerformanceModel<RD>> buildLocalModels() {
    Set<LocalPerformanceModel<RD>> localModels = new HashSet<>();

    for (Map.Entry<JavaRegion, D> entry : this.regionsToData.entrySet()) {
      LocalPerformanceModel<RD> localModel = this.buildEmptyLocalModel(entry);
      this.populateLocalModel(localModel);
      localModels.add(localModel);
    }

    return localModels;
  }

  @Override
  public void writeToFile(PerformanceModel<RD> results) throws IOException {
    for (LocalPerformanceModel<RD> localModel : results.getLocalModels()) {
      String outputFile = this.outputDir() + "/" + localModel.getRegion() + Options.DOT_JSON;
      File file = new File(outputFile);
      file.getParentFile().mkdirs();

      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(file, localModel);
    }
  }

  protected List<String> getOptions() {
    return options;
  }

  protected Set<PerformanceEntry> getPerformanceEntries() {
    return performanceEntries;
  }
}

package edu.cmu.cs.mvelezce.pretty;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.influence.LocalPerformanceInfluenceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class BasePrettyBuilder<T> extends BaseAnalysis<PerformanceModel<Set<String>>> {

  private final Collection<String> options;
  private final PerformanceModel<T> model;

  public BasePrettyBuilder(
      String programName, Collection<String> options, PerformanceModel<T> model) {
    super(programName);

    this.options = options;
    this.model = model;
  }

  @Override
  public PerformanceModel<Set<String>> analyze() {
    Set<LocalPerformanceModel<Set<String>>> localModels = this.buildInfluenceLocalModels();

    return new PerformanceModel<>(localModels);
  }

  private Set<LocalPerformanceModel<Set<String>>> buildInfluenceLocalModels() {
    Set<LocalPerformanceModel<Set<String>>> influenceModels = new HashSet<>();

    for (LocalPerformanceModel<T> localModel : model.getLocalModels()) {
      LocalPerformanceModel<Set<String>> influenceLocalModel =
          this.buildInfluenceLocalModel(localModel);
      influenceModels.add(influenceLocalModel);
    }

    return influenceModels;
  }

  protected abstract LocalPerformanceInfluenceModel buildInfluenceLocalModel(
      LocalPerformanceModel<T> localModel);

  @Override
  public void writeToFile(PerformanceModel<Set<String>> results) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, results);
  }

  @Override
  public PerformanceModel<Set<String>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("implement");
  }

  protected Collection<String> getOptions() {
    return options;
  }
}

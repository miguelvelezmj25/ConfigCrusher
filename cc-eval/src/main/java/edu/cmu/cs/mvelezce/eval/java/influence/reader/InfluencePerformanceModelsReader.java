package edu.cmu.cs.mvelezce.eval.java.influence.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.influence.LocalPerformanceInfluenceModel;
import edu.cmu.cs.mvelezce.pretty.idta.IDTAPrettyBuilder;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class InfluencePerformanceModelsReader extends IDTAPrettyBuilder {

  private static final String MODELS_ROT =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/influence";
  private static final String PROGRAM_MODELS_DIR = "models";

  public InfluencePerformanceModelsReader(String programName) {
    super(programName, new ArrayList<>(), new PerformanceModel<>(new HashSet<>()));
  }

  public List<PerformanceModel<Set<String>>> read() throws IOException {
    System.err.println(
        "This code is very similar to comparing local performance models. Abstract!");
    String modelsDir = MODELS_ROT + "/" + this.getProgramName() + "/" + PROGRAM_MODELS_DIR;
    File modelsDirFile = new File(modelsDir);

    if (!modelsDirFile.exists()) {
      throw new RuntimeException("Could not find the models in " + modelsDir);
    }

    Collection<File> files = FileUtils.listFiles(modelsDirFile, null, true);

    if (files.size() <= 1) {
      throw new RuntimeException(
          "We expected to find at least 2 models to compare in " + modelsDir);
    }

    List<PerformanceModel<Set<String>>> models = new ArrayList<>();

    for (File file : files) {
      PerformanceModel<Set<String>> model = this.readModel(file);
      models.add(model);
    }

    return models;
  }

  private PerformanceModel<Set<String>> readModel(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<Set<String>>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      Map<String, Double> model = readLocalModel.getModel();
      LinkedHashMap<Set<String>, Double> influenceModel = new LinkedHashMap<>();

      for (Map.Entry<String, Double> entry : model.entrySet()) {
        Set<String> terms = this.getTerms(entry.getKey());
        influenceModel.put(terms, entry.getValue());
      }

      LocalPerformanceModel<Set<String>> localModel =
          new LocalPerformanceInfluenceModel(
              readLocalModel.getRegion(), influenceModel, this.toHumanReadable(influenceModel));
      localModels.add(localModel);
    }

    return new PerformanceModel<>(localModels);
  }

  private Set<String> getTerms(String entry) {
    int startOptionIndex = entry.indexOf("[") + 1;
    int endOptionIndex = entry.lastIndexOf("]");
    String optionsString = entry.substring(startOptionIndex, endOptionIndex);
    String[] features = optionsString.split(",");
    Set<String> terms = new HashSet<>();

    for (String feature : features) {
      feature = feature.trim();

      if (feature.isEmpty()) {
        continue;
      }

      terms.add(feature);
    }

    return terms;
  }
}

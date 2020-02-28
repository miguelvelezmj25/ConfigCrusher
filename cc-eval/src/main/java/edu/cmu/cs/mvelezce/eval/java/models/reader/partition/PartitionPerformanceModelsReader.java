package edu.cmu.cs.mvelezce.eval.java.models.reader.partition;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.builder.partition.BasePartitionPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.partition.PartitionLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PartitionPerformanceModelsReader extends BasePartitionPerformanceModelBuilder {

  private static final String MODELS_ROOT =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/models";
  private static final String PROGRAM_MODELS_DIR = "models";

  public PartitionPerformanceModelsReader(String programName, String measuredTime) {
    super(programName, new ArrayList<>(), new HashMap<>(), new HashSet<>(), measuredTime);
  }

  public Set<PerformanceModel<Partition>> read() throws IOException {
    System.err.println(
        "This code is very similar to comparing local performance models. Abstract!");
    String modelsDir =
        MODELS_ROOT
            + "/"
            + this.getProgramName()
            + "/"
            + this.getMeasuredTime()
            + "/"
            + PROGRAM_MODELS_DIR;
    File modelsDirFile = new File(modelsDir);

    if (!modelsDirFile.exists()) {
      throw new RuntimeException("Could not find the models in " + modelsDir);
    }

    Collection<File> files = FileUtils.listFiles(modelsDirFile, null, true);

    if (files.size() <= 1) {
      throw new RuntimeException(
          "We expected to find at least 2 models to compare in " + modelsDir);
    }

    Set<PerformanceModel<Partition>> models = new HashSet<>();

    for (File file : files) {
      PerformanceModel<Partition> model = this.readModel(file);
      models.add(model);
    }

    return models;
  }

  private PerformanceModel<Partition> readModel(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<Partition>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<Partition> localModel =
          new PartitionLocalPerformanceModel(
              readLocalModel.getRegion(),
              this.parsePartitionsToData(readLocalModel.getModel()),
              this.parsePartitionsToData(readLocalModel.getModelToMin()),
              this.parsePartitionsToData(readLocalModel.getModelToMax()),
              this.parsePartitionsToData(readLocalModel.getModelToDiff()),
              this.parsePartitionsToData(readLocalModel.getModelToSampleVariance()),
              this.parsePartitionsToCI(readLocalModel.getModelToConfidenceInterval()),
              this.parsePartitionsToData(readLocalModel.getModelToCoefficientOfVariation()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToPerfHumanReadable()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToMinHumanReadable()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToMaxHumanReadable()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToDiffHumanReadable()),
              this.parsePartitionsToHumanReadableData(
                  readLocalModel.getModelToSampleVarianceHumanReadble()),
              this.parsePartitionsToHumanReadableCI(
                  readLocalModel.getModelToConfidenceIntervalHumanReadable()),
              this.parsePartitionsToHumanReadableData(
                  readLocalModel.getModelToCoefficientOfVariationHumanReadable()));
      localModels.add(localModel);
    }

    return new PerformanceModel<>(localModels);
  }

  @Override
  public PerformanceModel<Partition> readFromFile(File file) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  public String outputDir() {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  protected void populateLocalModel(LocalPerformanceModel<Partition> localModel) {
    throw new UnsupportedOperationException("Method should not be called");
  }
}

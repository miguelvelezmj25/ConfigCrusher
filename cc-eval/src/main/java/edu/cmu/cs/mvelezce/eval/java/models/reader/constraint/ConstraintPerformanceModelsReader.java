package edu.cmu.cs.mvelezce.eval.java.models.reader.constraint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.model.constraint.ConstraintLocalPerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConstraintPerformanceModelsReader extends BaseConstraintPerformanceModelBuilder {

  private static final String MODELS_ROT =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/models";
  private static final String PROGRAM_MODELS_DIR = "models";

  public ConstraintPerformanceModelsReader(String programName) {
    super(programName, new ArrayList<>(), new HashMap<>(), new HashSet<>());
  }

  public Set<PerformanceModel<FeatureExpr>> read() throws IOException {
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

    Set<PerformanceModel<FeatureExpr>> models = new HashSet<>();

    for (File file : files) {
      PerformanceModel<FeatureExpr> model = this.readModel(file);
      models.add(model);
    }

    return models;
  }

  private PerformanceModel<FeatureExpr> readModel(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<FeatureExpr> localModel =
          new ConstraintLocalPerformanceModel(
              readLocalModel.getRegion(),
              this.parseConstraintsToData(readLocalModel.getModel()),
              this.parseConstraintsToData(readLocalModel.getModelToMin()),
              this.parseConstraintsToData(readLocalModel.getModelToMax()),
              this.parseConstraintsToData(readLocalModel.getModelToDiff()),
              this.parseConstraintsToData(readLocalModel.getModelToSampleVariance()),
              this.parseConstraintsToCI(readLocalModel.getModelToConfidenceInterval()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToPerfHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMinHumanReadable()),
              this.parseConstraintsToHumanReadableData(readLocalModel.getModelToMaxHumanReadable()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToDiffHumanReadable()),
              this.parseConstraintsToHumanReadableData(
                  readLocalModel.getModelToSampleVarianceHumanReadble()),
              this.parseConstraintsToHumanReadableCI(
                  readLocalModel.getModelToConfidenceIntervalHumanReadable()));
      localModels.add(localModel);
    }

    return new PerformanceModel<>(localModels);
  }

  @Override
  public PerformanceModel<FeatureExpr> readFromFile(File file) {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  public String outputDir() {
    throw new UnsupportedOperationException("Method should not be called");
  }

  @Override
  protected void populateLocalModel(LocalPerformanceModel<FeatureExpr> localModel) {
    throw new UnsupportedOperationException("Method should not be called");
  }
}

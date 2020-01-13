package edu.cmu.cs.mvelezce.learning.builder.model.matlab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.learning.builder.BaseLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.learning.model.constraint.LearnedLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatlabLinearLearnedModelBuilder extends BaseLinearLearnedModelBuilder {

  public MatlabLinearLearnedModelBuilder(String programName, SamplingApproach samplingApproach) {
    this(programName, new ArrayList<>(), samplingApproach);
  }

  MatlabLinearLearnedModelBuilder(
      String programName, List<String> options, SamplingApproach samplingApproach) {
    super(programName, options, samplingApproach);
  }

  @Override
  public PerformanceModel<FeatureExpr> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<FeatureExpr> localModel =
          new LearnedLocalPerformanceModel(
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
  public String outputDir() {
    return BaseLinearLearnedModelBuilder.OUTPUT_DIR
        + "/matlab/model/java/programs/"
        + this.getSamplingApproach().getName()
        + "/"
        + this.getProgramName();
  }
}

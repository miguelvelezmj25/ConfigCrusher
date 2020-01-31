package edu.cmu.cs.mvelezce.learning.builder.model.matlab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.learning.builder.BaseLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.learning.model.partition.LearnedLocalPerformanceModel;
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
  public PerformanceModel<Partition> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PerformanceModel<String> readModel =
        mapper.readValue(file, new TypeReference<PerformanceModel<String>>() {});
    Set<LocalPerformanceModel<Partition>> localModels = new HashSet<>();

    for (LocalPerformanceModel<String> readLocalModel : readModel.getLocalModels()) {
      LocalPerformanceModel<Partition> localModel =
          new LearnedLocalPerformanceModel(
              readLocalModel.getRegion(),
              this.parsePartitionsToData(readLocalModel.getModel()),
              this.parsePartitionsToData(readLocalModel.getModelToMin()),
              this.parsePartitionsToData(readLocalModel.getModelToMax()),
              this.parsePartitionsToData(readLocalModel.getModelToDiff()),
              this.parsePartitionsToData(readLocalModel.getModelToSampleVariance()),
              this.parsePartitionsToCI(readLocalModel.getModelToConfidenceInterval()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToPerfHumanReadable()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToMinHumanReadable()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToMaxHumanReadable()),
              this.parsePartitionsToHumanReadableData(readLocalModel.getModelToDiffHumanReadable()),
              this.parsePartitionsToHumanReadableData(
                  readLocalModel.getModelToSampleVarianceHumanReadble()),
              this.parsePartitionsToHumanReadableCI(
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

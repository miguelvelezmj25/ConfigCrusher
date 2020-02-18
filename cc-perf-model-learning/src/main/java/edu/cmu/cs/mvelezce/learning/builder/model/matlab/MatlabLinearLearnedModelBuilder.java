package edu.cmu.cs.mvelezce.learning.builder.model.matlab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.learning.builder.BaseLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.learning.model.partition.LearnedLocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MatlabLinearLearnedModelBuilder extends BaseLinearLearnedModelBuilder {

  public MatlabLinearLearnedModelBuilder(String programName, SamplingApproach samplingApproach) {
    this(programName, new ArrayList<>(), samplingApproach);
  }

  MatlabLinearLearnedModelBuilder(
      String programName, List<String> options, SamplingApproach samplingApproach) {
    super(programName, options, samplingApproach);
  }

  @Override
  public PerformanceModel<Partition> readFromFile(File dir) throws IOException {
    Collection<File> files = FileUtils.listFiles(dir, new String[] {"json"}, false);
    Set<LocalPerformanceModel<Partition>> localModels = new HashSet<>();

    for (File file : files) {
      ObjectMapper mapper = new ObjectMapper();
      LocalPerformanceModel<String> readLocalModel =
          mapper.readValue(file, new TypeReference<LocalPerformanceModel<String>>() {});
      LocalPerformanceModel<Partition> localModel =
          new LearnedLocalPerformanceModel(
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
  public String outputDir() {
    return BaseLinearLearnedModelBuilder.OUTPUT_DIR
        + "/matlab/model/java/programs/"
        + this.getSamplingApproach().getName()
        + "/"
        + this.getProgramName();
  }
}

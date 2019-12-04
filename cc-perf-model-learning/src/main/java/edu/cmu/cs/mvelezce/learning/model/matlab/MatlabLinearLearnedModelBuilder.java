package edu.cmu.cs.mvelezce.learning.model.matlab;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.learning.BaseLinearLearnedModelBuilder;

import java.util.List;

public class MatlabLinearLearnedModelBuilder extends BaseLinearLearnedModelBuilder {

  MatlabLinearLearnedModelBuilder(
      String programName, List<String> options, SamplingApproach samplingApproach) {
    super(programName, options, samplingApproach);
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

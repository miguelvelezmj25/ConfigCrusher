package edu.cmu.cs.mvelezce.learning.builder.generate.matlab.script;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class StepWiseLinearLearnerTest {

  @Test
  public void berkeleyDB_FW() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach);
    learner.generateLearningScript();
  }

  @Test
  public void berkeleyDB_PW() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach);
    learner.generateLearningScript();
  }

  @Test
  public void lucene_FW() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach);
    learner.generateLearningScript();
  }

  @Test
  public void lucene_PW() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach);
    learner.generateLearningScript();
  }

  @Test
  public void convert_FW() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach);
    learner.generateLearningScript();
  }

  @Test
  public void convert_PW() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach);
    learner.generateLearningScript();
  }
}

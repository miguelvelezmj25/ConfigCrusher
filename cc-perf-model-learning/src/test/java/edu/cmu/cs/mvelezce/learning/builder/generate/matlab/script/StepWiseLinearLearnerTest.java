package edu.cmu.cs.mvelezce.learning.builder.generate.matlab.script;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.approaches.sampling.random.RandomSampling;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class StepWiseLinearLearnerTest {

  @Test
  public void berkeleyDB_FW_instrument() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void berkeleyDB_PW_instrument() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void berkeleyDB_Random200_instrument() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(200);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void berkeleyDB_Random50_instrument() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(50);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void lucene_FW_instrument() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void lucene_PW_instrument() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void lucene_Random200_instrument() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(200);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void lucene_Random50_instrument() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(50);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void convert_FW_time_user() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.USER);
    learner.generateLearningScript();
  }

  @Test
  public void convert_PW_time_user() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.USER);
    learner.generateLearningScript();
  }

  @Test
  public void convert_Random200_time_user() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(200);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.USER);
    learner.generateLearningScript();
  }

  @Test
  public void convert_Random50_time_user() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(50);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.USER);
    learner.generateLearningScript();
  }

  @Test
  public void runBenchC_FW_time_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void runBenchC_PW_time_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void runBenchC_Random200_time_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(200);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }

  @Test
  public void runBenchC_Random50_time_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    SamplingApproach samplingApproach = RandomSampling.getInstance(50);

    StepWiseLinearLearner learner =
        new StepWiseLinearLearner(programName, options, samplingApproach, BaseExecutor.REAL);
    learner.generateLearningScript();
  }
}

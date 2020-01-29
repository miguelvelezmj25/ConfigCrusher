package edu.cmu.cs.mvelezce.learning.builder.model.matlab;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class MatlabLinearLearnedModelBuilderTest {

  @Test
  public void berkeleyDB_FW() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    BasePerformanceModelBuilder builder =
        new MatlabLinearLearnedModelBuilder(programName, options, samplingApproach);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void berkeleyDB_PW() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    BasePerformanceModelBuilder builder =
        new MatlabLinearLearnedModelBuilder(programName, options, samplingApproach);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void lucene_FW() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();

    BasePerformanceModelBuilder builder =
        new MatlabLinearLearnedModelBuilder(programName, options, samplingApproach);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void lucene_PW() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();

    BasePerformanceModelBuilder builder =
        new MatlabLinearLearnedModelBuilder(programName, options, samplingApproach);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }
}

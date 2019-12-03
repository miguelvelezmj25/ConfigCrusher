package edu.cmu.cs.mvelezce.learning.model.matlab;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.fw.FeatureWisePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class StepWiseLinearModelBuilderTest {

  @Test
  public void berkeleyDB_generateCSVFile_FW() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> configs = FeatureWiseSampling.getInstance().getConfigs(options);

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWisePerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    StepWiseLinearModelBuilder builder =
        new StepWiseLinearModelBuilder(
            programName, options, configs, performanceEntries, samplingApproach);
    builder.generateCSVFile();
  }
}

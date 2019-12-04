package edu.cmu.cs.mvelezce.exhaustive.model.gt;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.gt.GroundTruthPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GroundTruthExhaustiveModelBuilderTest {

  @Test
  public void berkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new GroundTruthExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }
}

package edu.cmu.cs.mvelezce.exhaustive.builder.bf;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.bf.BruteForcePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BruteForceExhaustiveModelBuilderTest {

  @Test
  public void berkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForcePerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForcePerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }
}

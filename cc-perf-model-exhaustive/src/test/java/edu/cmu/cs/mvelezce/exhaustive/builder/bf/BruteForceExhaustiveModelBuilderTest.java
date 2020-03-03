package edu.cmu.cs.mvelezce.exhaustive.builder.bf;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.bf.BruteForceInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.bf.BruteForceTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class BruteForceExhaustiveModelBuilderTest {

  @Test
  public void berkeleyDB_instrument_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceInstrumentPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(
            programName, options, performanceEntries, BaseExecutor.REAL);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void lucene_instrument_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceInstrumentPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(
            programName, options, performanceEntries, BaseExecutor.REAL);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void convert_time_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceTimePerfAggregatorProcessor(programName, BaseExecutor.USER);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(
            programName, options, performanceEntries, BaseExecutor.USER);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void runBenchC_time_real() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceTimePerfAggregatorProcessor(programName, BaseExecutor.REAL);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(
            programName, options, performanceEntries, BaseExecutor.REAL);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void runBenchC_time_user() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceTimePerfAggregatorProcessor(programName, BaseExecutor.USER);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new BruteForceExhaustiveModelBuilder(
            programName, options, performanceEntries, BaseExecutor.USER);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }
}

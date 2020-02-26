package edu.cmu.cs.mvelezce.exhaustive.builder.gt;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.gt.GroundTruthInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.gt.GroundTruthTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GroundTruthExhaustiveModelBuilderTest {

  @Test
  public void berkeleyDB_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthInstrumentPerfAggregatorProcessor(programName);

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

  @Test
  public void berkeleyDB_diffGT_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthInstrumentPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);
    Iterator<PerformanceEntry> performanceEntriesIter = performanceEntries.iterator();
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    for (int i = 0; i < 4; i++) {
      Set<PerformanceEntry> thisPerfEntries = new HashSet<>();

      for (int j = 0; j < 500; j++) {
        thisPerfEntries.add(performanceEntriesIter.next());
      }

      BasePerformanceModelBuilder builder =
          new GroundTruthExhaustiveModelBuilder(programName, options, thisPerfEntries, i);

      args = new String[2];
      args[0] = "-delres";
      args[1] = "-saveres";
      builder.analyze(args);
    }
  }

  @Test
  public void lucene_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthInstrumentPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new GroundTruthExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void density_instrument() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthInstrumentPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new GroundTruthExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void multithread_time() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthTimePerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseMultithreadAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new GroundTruthExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void convert_time() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthTimePerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new GroundTruthExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }

  @Test
  public void runBenchC_time() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthTimePerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    BasePerformanceModelBuilder builder =
        new GroundTruthExhaustiveModelBuilder(programName, options, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }
}

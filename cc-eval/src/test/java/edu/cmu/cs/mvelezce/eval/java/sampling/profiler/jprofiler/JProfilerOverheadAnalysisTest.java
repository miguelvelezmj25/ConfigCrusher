package edu.cmu.cs.mvelezce.eval.java.sampling.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.idta.IDTAE2EInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.idta.IDTAE2ETimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta.IDTAPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class JProfilerOverheadAnalysisTest {

  @Test
  public void berkeleyDB_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> idtaPerfEntries = perfAggregatorProcessor.analyze(args);

    perfAggregatorProcessor = new IDTAE2EInstrumentPerfAggregatorProcessor(programName);
    args = new String[0];
    Set<PerformanceEntry> e2ePerfEntries = perfAggregatorProcessor.analyze(args);

    JProfilerOverheadAnalysis analysis =
        new JProfilerOverheadAnalysis(programName, BaseExecutor.REAL);
    analysis.analyze(idtaPerfEntries, e2ePerfEntries);
  }

  @Test
  public void lucene_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> idtaPerfEntries = perfAggregatorProcessor.analyze(args);

    perfAggregatorProcessor = new IDTAE2EInstrumentPerfAggregatorProcessor(programName);
    args = new String[0];
    Set<PerformanceEntry> e2ePerfEntries = perfAggregatorProcessor.analyze(args);

    JProfilerOverheadAnalysis analysis =
        new JProfilerOverheadAnalysis(programName, BaseExecutor.REAL);
    analysis.analyze(idtaPerfEntries, e2ePerfEntries);
  }

  @Test
  public void convert_time_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, BaseExecutor.USER);
    String[] args = new String[0];
    Set<PerformanceEntry> idtaPerfEntries = perfAggregatorProcessor.analyze(args);

    perfAggregatorProcessor =
        new IDTAE2ETimePerfAggregatorProcessor(programName, BaseExecutor.USER);
    args = new String[0];
    Set<PerformanceEntry> e2ePerfEntries = perfAggregatorProcessor.analyze(args);

    JProfilerOverheadAnalysis analysis =
        new JProfilerOverheadAnalysis(programName, BaseExecutor.USER);
    analysis.analyze(idtaPerfEntries, e2ePerfEntries);
  }
}

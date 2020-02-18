package edu.cmu.cs.mvelezce.eval.java.sampling.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.idta.IDTAE2EPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta.IDTAPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class JProfilerOverheadAnalysisTest {

  @Test
  public void berkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> idtaPerfEntries = perfAggregatorProcessor.analyze(args);

    perfAggregatorProcessor = new IDTAE2EPerfAggregatorProcessor(programName);
    args = new String[0];
    Set<PerformanceEntry> e2ePerfEntries = perfAggregatorProcessor.analyze(args);

    JProfilerOverheadAnalysis analysis = new JProfilerOverheadAnalysis(programName);
    analysis.analyze(idtaPerfEntries, e2ePerfEntries);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> idtaPerfEntries = perfAggregatorProcessor.analyze(args);

    perfAggregatorProcessor = new IDTAE2EPerfAggregatorProcessor(programName);
    args = new String[0];
    Set<PerformanceEntry> e2ePerfEntries = perfAggregatorProcessor.analyze(args);

    JProfilerOverheadAnalysis analysis = new JProfilerOverheadAnalysis(programName);
    analysis.analyze(idtaPerfEntries, e2ePerfEntries);
  }
}

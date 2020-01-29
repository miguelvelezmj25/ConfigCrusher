package edu.cmu.cs.mvelezce.eval.java.time;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.bf.BruteForcePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.fw.FeatureWisePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.gt.GroundTruthPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.pw.PairWisePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class TimeEvaluationTest {

  @Test
  public void berkeleyDB_GT_MeasuredTime() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.GT, performanceEntries);
  }

  @Test
  public void berkeleyDB_BF_MeasuredTime() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForcePerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.BF, performanceEntries);
  }

  @Test
  public void berkeleyDB_FW_MeasuredTime() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWisePerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.FW, performanceEntries);
  }

  @Test
  public void berkeleyDB_PW_MeasuredTime() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWisePerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.PW, performanceEntries);
  }
}

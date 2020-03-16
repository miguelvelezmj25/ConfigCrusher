package edu.cmu.cs.mvelezce.eval.java.time;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.bf.BruteForceInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.fw.FeatureWiseInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.gt.GroundTruthInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.pw.PairWiseInstrumentPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.bf.BruteForceTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.fw.FeatureWiseTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.gt.GroundTruthTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.pw.PairWiseTimePerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler.RawJProfilerSamplingExecutionParser;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta.IDTAPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawJProfilerSamplingPerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class TimeEvaluationTest {

  @Test
  public void berkeleyDB_GT_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.GT, performanceEntries);
  }

  @Test
  public void berkeleyDB_BF_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.BF, performanceEntries);
  }

  @Test
  public void berkeleyDB_FW_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.FW, performanceEntries);
  }

  @Test
  public void berkeleyDB_PW_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.PW, performanceEntries);
  }

  @Test
  public void berkeleyDB_IDTA_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getMeasuredTime(Evaluation.IDTA, performanceEntries);
  }

  @Test
  public void lucene_GT_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.GT, performanceEntries);
  }

  @Test
  public void lucene_BF_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.BF, performanceEntries);
  }

  @Test
  public void lucene_FW_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.FW, performanceEntries);
  }

  @Test
  public void lucene_PW_MeasuredTime_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWiseInstrumentPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.PW, performanceEntries);
  }

  @Test
  public void lucene_IDTA_MeasuredTime() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getMeasuredTime(Evaluation.IDTA, performanceEntries);
  }

  @Test
  public void convert_BF_MeasuredTime_time_real() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new BruteForceTimePerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.BF, performanceEntries);
  }

  @Test
  public void convert_FW_MeasuredTime_time_real() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new FeatureWiseTimePerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.FW, performanceEntries);
  }

  @Test
  public void convert_PW_MeasuredTime_time_real() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new PairWiseTimePerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.PW, performanceEntries);
  }

  @Test
  public void convert_IDTA_MeasuredTime() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    TimeEvaluation.getIDTAMeasuredTime(itersToRawPerfExecs, PerfAggregatorProcessor.ADDED_TIME);
  }

  @Test
  public void convert_GT_MeasuredTime_time_real() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new GroundTruthTimePerfAggregatorProcessor(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    TimeEvaluation.getE2EMeasuredTime(Evaluation.GT, performanceEntries);
  }
}

package edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.idta.IDTAJProfilerSamplingExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAPerfAggregatorProcessorTest {

  @Test
  public void trivial_real() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.REAL);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName, itersToProcessedPerfExecution, BaseExecutor.REAL, 0);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void berkeley_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.REAL);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName, itersToProcessedPerfExecution, BaseExecutor.REAL, 0);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void lucene_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.REAL);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName, itersToProcessedPerfExecution, BaseExecutor.REAL, 0);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void performance_real() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.REAL);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName, itersToProcessedPerfExecution, BaseExecutor.REAL, 0);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void multithread_user() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.USER);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName,
            itersToProcessedPerfExecution,
            BaseExecutor.USER,
            PerfAggregatorProcessor.ADDED_TIME);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void convert_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.USER);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName,
            itersToProcessedPerfExecution,
            BaseExecutor.USER,
            PerfAggregatorProcessor.ADDED_TIME);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void runBenchC_user() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<PerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName, BaseExecutor.USER);

    String[] args = new String[0];
    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution = processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(
            programName,
            itersToProcessedPerfExecution,
            BaseExecutor.USER,
            PerfAggregatorProcessor.ADDED_TIME);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }
}

package edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.idta.IDTAJProfilerSamplingExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAPerfAggregatorProcessorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<ProcessedPerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName);

    String[] args = new String[0];
    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution =
        processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, itersToProcessedPerfExecution);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void berkeley() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<ProcessedPerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName);

    String[] args = new String[0];
    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution =
        processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, itersToProcessedPerfExecution);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<ProcessedPerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName);

    String[] args = new String[0];
    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution =
        processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, itersToProcessedPerfExecution);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void performance() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<ProcessedPerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName);

    String[] args = new String[0];
    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution =
        processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, itersToProcessedPerfExecution);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void multithread() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    Analysis<Map<Integer, Set<ProcessedPerfExecution>>> processor =
        new IDTAJProfilerSamplingExecutionProcessor(programName);

    String[] args = new String[0];
    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecution =
        processor.analyze(args);
    Analysis perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName, itersToProcessedPerfExecution);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    perfAggregatorProcessor.analyze(args);
  }
}

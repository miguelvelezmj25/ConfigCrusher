package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.idta.IDTATimerInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler.RawJProfilerSamplingExecutionParser;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawJProfilerSamplingPerfExecution;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAJProfilerSamplingExecutionProcessorTest {

  @Test
  public void trivial_real() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void berkeleyDB_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void lucene_real() throws IOException, InterruptedException {
    System.err.println(
        "Regions a2500b3a-d6e0-45dd-bc6f-dcca4ea72038 and 83847d46-8d61-4ecf-8b7d-6eaca01d469e are in the same method. "
            + "We manually removed one of them. Check if this method is relevant for performance");
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void performance_real() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void multithread_user() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void convert_user() throws IOException, InterruptedException {
    System.err.println(
        "Regions 6a33e604-871b-4824-842d-51974725f7df and 73ba70ff-f285-4706-ae9e-23b2c93bc6df are in the same method. "
            + "Regions 1e205cdb-0daa-40a9-9598-d381a8b293fc and c5f3564f-f58e-4e36-8def-8ebb220873be are in the same method. "
            + "We manually removed one of them. Check if these methods are relevant for performance.");

    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void runBenchC_user() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(
            programName, RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName,
            itersToRawPerfExecs,
            regionsToPartitions.keySet(),
            IDTAJProfilerSamplingExecutor.getMeasuredTimeFromThreadStatus(
                RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS));

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }
}

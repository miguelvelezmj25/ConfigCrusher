package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.idta.IDTATimerInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawJProfilerSamplingPerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAJProfilerSamplingExecutionProcessorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(programName);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName, itersToRawPerfExecs, regionsToPartitions.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void berkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(programName);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName, itersToRawPerfExecs, regionsToPartitions.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(programName);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName, itersToRawPerfExecs, regionsToPartitions.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void performance() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(programName);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName, itersToRawPerfExecs, regionsToPartitions.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void multithread() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(programName);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName, itersToRawPerfExecs, regionsToPartitions.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void convert() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseExecutor<RawJProfilerSamplingPerfExecution> executor =
        new IDTAJProfilerSamplingExecutor(programName);
    Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAJProfilerSamplingExecutionProcessor(
            programName, itersToRawPerfExecs, regionsToPartitions.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }
}

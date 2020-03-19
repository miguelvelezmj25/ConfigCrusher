package edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler.RawJProfilerSamplingExecutionParser;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IDTAJProfilerSamplingExecutorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName, configurations, 0, RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName,
            configurations,
            30000,
            RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    executor.execute(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    System.err.println(
        "Ignoring hotspot 'org.apache.lucene.core.util.BytesRefHash.add(org.apache.lucene.core.util.BytesRef)' "
            + "since it was taking different amounts of time in config "
            + "[MAX_BUFFERED_DOCS,USE_COMPOUND_FILE,CHECK_PENDING_FLUSH_UPDATE,MAX_TOKEN_LENGTH,COMMIT_ON_CLOSE]");
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName,
            configurations,
            30000,
            RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    executor.execute(args);
  }

  @Test
  public void performance() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName, configurations, 0, RawJProfilerSamplingExecutionParser.ALL_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }

  @Test
  public void multithread() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName,
            configurations,
            30000,
            RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    executor.execute(args);
  }

  @Test
  public void convert() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName,
            configurations,
            30000,
            RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    executor.execute(args);
  }

  @Test
  public void runBenchC() throws IOException, InterruptedException {
    System.err.println(
        "Ignoring hotspot 'org.h2.util.MathUtils$1.run()' since it was executing for the majority of the time in "
            + "most configurations. This time was being attributed to the base time, which was wrong");
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    configurations.clear();

    Set<String> c = new HashSet<>();
    c.add("ANALYZE_SAMPLE");
    c.add("CIPHER");
    c.add("CACHE_SIZE");
    c.add("PAGE_SIZE");
    c.add("ACCESS_MODE_DATA");
    c.add("COMPRESS");

    configurations.add(c);

    Executor executor =
        new IDTAJProfilerSamplingExecutor(
            programName,
            configurations,
            0,
            RawJProfilerSamplingExecutionParser.RUNNABLE_THREAD_STATUS);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }
}

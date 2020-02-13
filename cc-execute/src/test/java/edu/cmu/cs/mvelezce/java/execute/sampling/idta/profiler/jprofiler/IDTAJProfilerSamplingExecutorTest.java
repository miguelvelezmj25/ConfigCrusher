package edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import edu.cmu.cs.mvelezce.java.execute.Executor;
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

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations, 0);

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

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations, 30000);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    executor.execute(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);
    configurations.clear();

    Set<String> config = new HashSet<>();
    config.add("MAX_BUFFERED_DOCS");
    config.add("USE_COMPOUND_FILE");
    config.add("CHECK_PENDING_FLUSH_UPDATE");
    config.add("MAX_TOKEN_LENGTH");
    config.add("COMMIT_ON_CLOSE");
    configurations.add(config);

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations, 0);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }

  @Test
  public void performance() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations, 0);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }
}

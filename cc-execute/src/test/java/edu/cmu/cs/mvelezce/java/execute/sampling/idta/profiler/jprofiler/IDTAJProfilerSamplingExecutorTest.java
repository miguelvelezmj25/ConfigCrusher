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

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations);

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
    configurations.clear();

    Set<String> config = new HashSet<>();
    config.add("CACHE_MODE");
    config.add("MAX_MEMORY");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ADLER32_CHUNK_SIZE");
    config.add("ENV_BACKGROUND_READ_LIMIT");
    config.add("MAX_MEMORY");
    configurations.add(config);

    config = new HashSet<>();
    config.add("TEMPORARY");
    config.add("DUPLICATES");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_SHARED_CACHE");
    config.add("TEMPORARY");
    config.add("ENV_IS_LOCKING");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_SHARED_CACHE");
    config.add("TEMPORARY");
    configurations.add(config);

    config = new HashSet<>();
    config.add("CACHE_MODE");
    config.add("TEMPORARY");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_BACKGROUND_READ_LIMIT");
    config.add("ENV_SHARED_CACHE");
    config.add("CHECKPOINTER_BYTES_INTERVAL");
    config.add("ENV_IS_LOCKING");
    configurations.add(config);

    config = new HashSet<>();
    config.add("LOCK_DEADLOCK_DETECT_DELAY");
    config.add("CHECKPOINTER_BYTES_INTERVAL");
    config.add("JE_FILE_LEVEL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ADLER32_CHUNK_SIZE");
    config.add("TXN_SERIALIZABLE_ISOLATION");
    configurations.add(config);

    config = new HashSet<>();
    config.add("TXN_SERIALIZABLE_ISOLATION");
    config.add("JE_FILE_LEVEL");
    config.add("ENV_IS_LOCKING");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_SHARED_CACHE");
    config.add("MAX_MEMORY");
    config.add("SEQUENTIAL");
    config.add("JE_FILE_LEVEL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_SHARED_CACHE");
    config.add("TXN_SERIALIZABLE_ISOLATION");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ADLER32_CHUNK_SIZE");
    config.add("MAX_MEMORY");
    config.add("CHECKPOINTER_BYTES_INTERVAL");
    config.add("TXN_SERIALIZABLE_ISOLATION");
    config.add("SEQUENTIAL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("CACHE_MODE");
    config.add("TXN_SERIALIZABLE_ISOLATION");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ADLER32_CHUNK_SIZE");
    config.add("CACHE_MODE");
    config.add("ENV_SHARED_CACHE");
    config.add("CHECKPOINTER_BYTES_INTERVAL");
    config.add("SEQUENTIAL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("CACHE_MODE");
    configurations.add(config);

    config = new HashSet<>();
    config.add("CACHE_MODE");
    config.add("LOCK_DEADLOCK_DETECT_DELAY");
    config.add("DUPLICATES");
    configurations.add(config);

    config = new HashSet<>();
    config.add("DUPLICATES");
    config.add("TXN_SERIALIZABLE_ISOLATION");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_SHARED_CACHE");
    config.add("CHECKPOINTER_BYTES_INTERVAL");
    config.add("SEQUENTIAL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("MAX_MEMORY");
    config.add("DUPLICATES");
    config.add("TXN_SERIALIZABLE_ISOLATION");
    config.add("SEQUENTIAL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("SEQUENTIAL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("ENV_SHARED_CACHE");
    config.add("ENV_BACKGROUND_READ_LIMIT");
    config.add("MAX_MEMORY");
    config.add("TEMPORARY");
    config.add("DUPLICATES");
    config.add("SEQUENTIAL");
    config.add("JE_FILE_LEVEL");
    configurations.add(config);

    config = new HashSet<>();
    config.add("MAX_MEMORY");
    config.add("TEMPORARY");
    config.add("CHECKPOINTER_BYTES_INTERVAL");
    config.add("DUPLICATES");
    config.add("SEQUENTIAL");
    configurations.add(config);

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configurations = compression.analyze(args);

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations);

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

    Executor executor = new IDTAJProfilerSamplingExecutor(programName, configurations);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    executor.execute(args);
  }
}

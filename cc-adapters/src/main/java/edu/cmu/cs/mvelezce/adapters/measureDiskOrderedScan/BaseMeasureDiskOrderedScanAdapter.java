package edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Berkeley DB */
public class BaseMeasureDiskOrderedScanAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "MeasureDiskOrderedScan";
  public static final String MAIN_CLASS = "com.sleepycat.analysis.MeasureDiskOrderedScan";
  public static final String ROOT_PACKAGE = "com.sleepycat";
  public static final String ORIGINAL_ROOT_DIR =
      "../performance-mapper-evaluation/original/berkeley-db";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/berkeley-db";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/berkeley-db/target/classes";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/berkeley-db/target/classes";
  private static final String HOME_DIR = "tmp";
  private static final String[] OPTIONS = {"DUPLICATES", "SEQUENTIAL", "MAX_MEMORY"
    // ,    "ENV_SHARED_CACHE",
    //    "REPLICATED",
    //    "ENV_IS_LOCKING",
    //    "CACHE_MODE",
    //    "TEMPORARY",
    //    "JE_FILE_LEVEL",
    //    "ENV_BACKGROUND_READ_LIMIT",
    //    "LOCK_DEADLOCK_DETECT",
    //    "TXN_SERIALIZABLE_ISOLATION",
    //    "JE_DURABILITY",
    //    "ADLER32_CHUNK_SIZE",
    //    "CHECKPOINTER_BYTES_INTERVAL",
    //    "LOCK_DEADLOCK_DETECT_DELAY"
  };

  public BaseMeasureDiskOrderedScanAdapter() {
    // TODO check that we are passing empty string
    super(
        BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME,
        BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS,
        BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseMeasureDiskOrderedScanAdapter.OPTIONS);
  }

  public void preProcess(String dir) {
    try {
      this.removeDir();
      this.makeDir();
      this.clean(dir);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Could not create the tmp folder to run " + PROGRAM_NAME, e);
    }
  }

  private void clean(String dir) throws IOException, InterruptedException {
    //    ProcessBuilder builder = new ProcessBuilder();
    //
    //    List<String> commandList = new ArrayList<>();
    //    commandList.add("sudo");
    //    commandList.add("./clean.sh");
    //    builder.command(commandList);
    //    builder.directory(new File(dir));
    //
    //    Process process = builder.start();
    //
    //    Executor.processOutput(process);
    //    Executor.processError(process);
    //
    //    process.waitFor();
  }

  private void makeDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("mkdir");
    commandList.add(HOME_DIR);

    runCommand(commandList);
  }

  private void removeDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("rm");
    commandList.add("-rf");
    commandList.add(HOME_DIR);

    runCommand(commandList);
  }

  private void runCommand(List<String> commandList) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(commandList);

    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }
}

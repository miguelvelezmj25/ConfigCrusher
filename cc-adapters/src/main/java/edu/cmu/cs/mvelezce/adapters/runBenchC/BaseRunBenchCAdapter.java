package edu.cmu.cs.mvelezce.adapters.runBenchC;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseRunBenchCAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "RunBenchC";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.bench.RunBenchC";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.bench";
  public static final String ORIGINAL_DIR_PATH =
      "../performance-mapper-evaluation/original/h2database";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/h2database/target/classes";
  private static final String DATA_DIR = "data";

  private static final String[] OPTIONS = {
    "FILE_LOCK", "AUTOCOMMIT", "ACCESS_MODE_DATA", "CIPHER", "CACHE_TYPE", "CACHE_SIZE",
  };

  public BaseRunBenchCAdapter() {
    super(
        BaseRunBenchCAdapter.PROGRAM_NAME,
        BaseRunBenchCAdapter.MAIN_CLASS,
        BaseRunBenchCAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseRunBenchCAdapter.OPTIONS);
  }

  public void preProcess() {
    try {
      this.removeDir();
      this.makeDir();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Could not create the tmp folder to run " + PROGRAM_NAME, e);
    }
  }

  private void removeDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("rm");
    commandList.add("-rf");
    commandList.add(DATA_DIR);

    Executor.executeCommand(commandList);
  }

  private void makeDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("mkdir");
    commandList.add(DATA_DIR);

    Executor.executeCommand(commandList);
  }
}

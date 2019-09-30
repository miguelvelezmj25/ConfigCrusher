package edu.cmu.cs.mvelezce.adapter.adapters.indexFiles;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;
import edu.cmu.cs.mvelezce.adapter.utils.Executor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

// https://cwiki.apache.org/confluence/display/LUCENE/ImproveIndexingSpeed
public class BaseIndexFilesAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "indexFiles";
  public static final String MAIN_CLASS = "org.apache.lucene.demo.IndexFiles";
  //  public static final String ROOT_PACKAGE = "org.apache.lucene";
  //  public static final String ORIGINAL_CLASS_PATH =
  // "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/lucene/lucene";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/lucene/lucene/target/classes";

  private static final String INDEX_DIR = "index";
  private static final String[] OPTIONS = {
    //    "MAX_BUFFERED_DOCS", "MERGE_SCHEDULER", "MERGE_POLICY", "COMMIT_ON_CLOSE"
    "CHECK_PENDING_FLUSH_UPDATE", "MERGE_SCHEDULER", "READER_POOLING", "COMMIT_ON_CLOSE"
  };

  public BaseIndexFilesAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseIndexFilesAdapter.PROGRAM_NAME,
        BaseIndexFilesAdapter.MAIN_CLASS,
        "",
        BaseIndexFilesAdapter.getListOfOptions());
  }

  public BaseIndexFilesAdapter(String programName, String entryPoint, String classDir) {
    super(programName, entryPoint, classDir, BaseIndexFilesAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseIndexFilesAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Implement");
  }

  public void preProcess() {
    try {
      this.removeIndexDir();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Could not create the tmp folder to run " + PROGRAM_NAME);
    }
  }

  private void removeIndexDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("rm");
    commandList.add("-rf");
    commandList.add(INDEX_DIR);

    runCommand(commandList);
  }

  private void runCommand(List<String> commandList) throws IOException, InterruptedException {
    System.err.println("Maybe can reuse this logic to run a command");
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(commandList);

    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }
}

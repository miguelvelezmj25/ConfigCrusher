package edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

// https://cwiki.apache.org/confluence/display/LUCENE/ImproveIndexingSpeed
public class IndexFilesAdapter extends BaseAdapter {

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
    "MAX_BUFFERED_DOCS", "MERGE_SCHEDULER", "MERGE_POLICY", "COMMIT_ON_CLOSE"
  };

  public IndexFilesAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        IndexFilesAdapter.PROGRAM_NAME,
        IndexFilesAdapter.MAIN_CLASS,
        "",
        IndexFilesAdapter.getListOfOptions());
  }

  public IndexFilesAdapter(String programName, String entryPoint, String classDir) {
    super(programName, entryPoint, classDir, IndexFilesAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(IndexFilesAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(IndexFilesMain.INDEX_FILES_MAIN, newArgs);
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

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }
}

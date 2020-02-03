package edu.cmu.cs.mvelezce.adapters.indexFiles;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://cwiki.apache.org/confluence/display/LUCENE/ImproveIndexingSpeed
public class BaseIndexFilesAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "IndexFiles";
  public static final String MAIN_CLASS = "org.apache.lucene.demo.IndexFiles";
  public static final String ROOT_PACKAGE = "org.apache.lucene";
  public static final String ORIGINAL_ROOT_DIR =
      "../performance-mapper-evaluation/original/lucene/lucene";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/lucene/lucene";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/lucene/lucene/target/classes";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/lucene/lucene/target/classes";

  private static final String INDEX_DIR = "index";
  private static final String[] OPTIONS = {
    "RAM_BUFFER_SIZE_MB",
    "MERGE_POLICY",
    "MERGE_SCHEDULER",
    "COMMIT_ON_CLOSE",
    "CHECK_PENDING_FLUSH_UPDATE",
    "READER_POOLING",
    "MAX_BUFFERED_DOCS",
    "CODEC",
    "USE_COMPOUND_FILE",
    "INDEX_DELETION_POLICY",
    "MAX_TOKEN_LENGTH",
    "MAX_CFS_SEGMENT_SIZE_MB",
    "NO_CFS_RATIO",
    "INDEX_COMMIT",
    "MERGED_SEGMENT_WARMER",
    "RAM_PER_THREAD_HARD_LIMIT",
    "SIMILARITY"
  };

  public BaseIndexFilesAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseIndexFilesAdapter.PROGRAM_NAME,
        BaseIndexFilesAdapter.MAIN_CLASS,
        BaseIndexFilesAdapter.getListOfOptions());

    System.err.println("Try other analyzer");
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseIndexFilesAdapter.OPTIONS);
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

    Executor.executeCommand(commandList);
  }
}

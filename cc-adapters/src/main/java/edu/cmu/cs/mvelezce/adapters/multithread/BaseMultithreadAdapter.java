package edu.cmu.cs.mvelezce.adapters.multithread;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseMultithreadAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "multithread";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.Multithread";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/multithread-taint/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/multithread-taint";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/multithread-taint/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public BaseMultithreadAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseMultithreadAdapter.PROGRAM_NAME,
        BaseMultithreadAdapter.MAIN_CLASS,
        BaseMultithreadAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseMultithreadAdapter.OPTIONS);
  }
}

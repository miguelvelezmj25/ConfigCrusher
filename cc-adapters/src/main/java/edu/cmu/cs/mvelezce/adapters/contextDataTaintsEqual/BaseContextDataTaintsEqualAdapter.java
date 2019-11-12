package edu.cmu.cs.mvelezce.adapters.contextDataTaintsEqual;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseContextDataTaintsEqualAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "contextDataTaintsEqual";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.ContextDataTaintsEqual";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public BaseContextDataTaintsEqualAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseContextDataTaintsEqualAdapter.PROGRAM_NAME,
        BaseContextDataTaintsEqualAdapter.MAIN_CLASS,
        BaseContextDataTaintsEqualAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseContextDataTaintsEqualAdapter.OPTIONS);
  }
}

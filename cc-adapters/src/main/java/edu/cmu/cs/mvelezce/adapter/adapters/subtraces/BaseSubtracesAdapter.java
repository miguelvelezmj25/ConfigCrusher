package edu.cmu.cs.mvelezce.adapter.adapters.subtraces;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseSubtracesAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "subtraces";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Subtraces";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public BaseSubtracesAdapter() {
    // TODO check that we are passing empty string
    super(
        BaseSubtracesAdapter.PROGRAM_NAME,
        BaseSubtracesAdapter.MAIN_CLASS,
        BaseSubtracesAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseSubtracesAdapter.OPTIONS);
  }
}

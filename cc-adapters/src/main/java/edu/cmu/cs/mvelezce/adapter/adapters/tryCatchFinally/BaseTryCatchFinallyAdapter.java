package edu.cmu.cs.mvelezce.adapter.adapters.tryCatchFinally;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseTryCatchFinallyAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "tryCatchFinally";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.TryCatchFinally";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public BaseTryCatchFinallyAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseTryCatchFinallyAdapter.PROGRAM_NAME,
        BaseTryCatchFinallyAdapter.MAIN_CLASS,
        BaseTryCatchFinallyAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseTryCatchFinallyAdapter.OPTIONS);
  }
}

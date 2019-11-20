package edu.cmu.cs.mvelezce.adapters.cleanConstraints;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseCleanConstraintsAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "cleanConstraints";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.CleanConstraints";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public BaseCleanConstraintsAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseCleanConstraintsAdapter.PROGRAM_NAME,
        BaseCleanConstraintsAdapter.MAIN_CLASS,
        BaseCleanConstraintsAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseCleanConstraintsAdapter.OPTIONS);
  }
}

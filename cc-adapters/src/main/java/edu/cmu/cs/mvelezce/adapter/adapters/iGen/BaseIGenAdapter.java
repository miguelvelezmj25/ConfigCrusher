package edu.cmu.cs.mvelezce.adapter.adapters.iGen;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseIGenAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "IGenExample";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.IGenExample";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C", "D", "E", "F", "G"};

  public BaseIGenAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseIGenAdapter.PROGRAM_NAME,
        BaseIGenAdapter.MAIN_CLASS,
        BaseIGenAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseIGenAdapter.OPTIONS);
  }
}

package edu.cmu.cs.mvelezce.adapters.overrideJREMethod;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseOverrideJREMethodAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "overrideJREMethod";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.OverrideJREMethod";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public BaseOverrideJREMethodAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BaseOverrideJREMethodAdapter.PROGRAM_NAME,
        BaseOverrideJREMethodAdapter.MAIN_CLASS,
        BaseOverrideJREMethodAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BaseOverrideJREMethodAdapter.OPTIONS);
  }
}

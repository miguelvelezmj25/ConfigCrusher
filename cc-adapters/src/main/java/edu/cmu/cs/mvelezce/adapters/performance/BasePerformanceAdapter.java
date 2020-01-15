package edu.cmu.cs.mvelezce.adapters.performance;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BasePerformanceAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Performance";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Performance";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String ORIGINAL_DIR_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public BasePerformanceAdapter() {
    // TODO check that we are passing empty string
    super(
        BasePerformanceAdapter.PROGRAM_NAME,
        BasePerformanceAdapter.MAIN_CLASS,
        BasePerformanceAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BasePerformanceAdapter.OPTIONS);
  }
}

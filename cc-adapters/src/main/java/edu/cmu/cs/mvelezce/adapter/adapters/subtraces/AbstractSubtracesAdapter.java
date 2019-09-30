package edu.cmu.cs.mvelezce.adapter.adapters.subtraces;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSubtracesAdapter extends BaseAdapter {

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

  public AbstractSubtracesAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSubtracesAdapter.PROGRAM_NAME,
        AbstractSubtracesAdapter.MAIN_CLASS,
        "",
        AbstractSubtracesAdapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSubtracesAdapter.OPTIONS);
  }
}

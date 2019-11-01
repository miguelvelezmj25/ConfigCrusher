package edu.cmu.cs.mvelezce.adapter.adapters.nesting;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractNestingAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "nesting";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Nesting";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractNestingAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        AbstractNestingAdapter.PROGRAM_NAME,
        AbstractNestingAdapter.MAIN_CLASS,
        AbstractNestingAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractNestingAdapter.OPTIONS);
  }
}

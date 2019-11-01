package edu.cmu.cs.mvelezce.adapter.adapters.ifor;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractIfOrAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "IfOr";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.IfOr";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractIfOrAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractIfOrAdapter.PROGRAM_NAME,
        AbstractIfOrAdapter.MAIN_CLASS,
        AbstractIfOrAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractIfOrAdapter.OPTIONS);
  }
}

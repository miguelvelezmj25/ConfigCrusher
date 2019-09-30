package edu.cmu.cs.mvelezce.adapter.adapters.constructor;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractConstructorAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Constructor";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Constructor";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractConstructorAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractConstructorAdapter.PROGRAM_NAME,
        AbstractConstructorAdapter.MAIN_CLASS,
        "",
        AbstractConstructorAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractConstructorAdapter.OPTIONS);
  }
}

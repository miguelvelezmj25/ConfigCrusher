package edu.cmu.cs.mvelezce.adapter.adapters.ifOr2;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractIfOr2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "IfOr2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.IfOr2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractIfOr2Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractIfOr2Adapter.PROGRAM_NAME,
        AbstractIfOr2Adapter.MAIN_CLASS,
        "",
        AbstractIfOr2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractIfOr2Adapter.OPTIONS);
  }
}

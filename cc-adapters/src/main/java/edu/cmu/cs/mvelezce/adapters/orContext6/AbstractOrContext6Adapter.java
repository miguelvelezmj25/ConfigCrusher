package edu.cmu.cs.mvelezce.adapters.orContext6;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractOrContext6Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "OrContext6";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.OrContext6";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractOrContext6Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractOrContext6Adapter.PROGRAM_NAME,
        AbstractOrContext6Adapter.MAIN_CLASS,
        AbstractOrContext6Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractOrContext6Adapter.OPTIONS);
  }
}

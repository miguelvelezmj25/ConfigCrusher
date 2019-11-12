package edu.cmu.cs.mvelezce.adapters.simpleForExample6;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleForExample6Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample6";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample6";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractSimpleForExample6Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleForExample6Adapter.PROGRAM_NAME,
        AbstractSimpleForExample6Adapter.MAIN_CLASS,
        AbstractSimpleForExample6Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleForExample6Adapter.OPTIONS);
  }
}

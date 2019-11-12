package edu.cmu.cs.mvelezce.adapters.simpleForExample3;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleForExample3Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample3";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample3";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AbstractSimpleForExample3Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleForExample3Adapter.PROGRAM_NAME,
        AbstractSimpleForExample3Adapter.MAIN_CLASS,
        AbstractSimpleForExample3Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleForExample3Adapter.OPTIONS);
  }
}

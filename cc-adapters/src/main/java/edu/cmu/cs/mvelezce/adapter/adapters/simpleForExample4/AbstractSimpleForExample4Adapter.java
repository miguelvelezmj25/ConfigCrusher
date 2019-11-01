package edu.cmu.cs.mvelezce.adapter.adapters.simpleForExample4;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleForExample4Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample4";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample4";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractSimpleForExample4Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleForExample4Adapter.PROGRAM_NAME,
        AbstractSimpleForExample4Adapter.MAIN_CLASS,
        AbstractSimpleForExample4Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleForExample4Adapter.OPTIONS);
  }
}

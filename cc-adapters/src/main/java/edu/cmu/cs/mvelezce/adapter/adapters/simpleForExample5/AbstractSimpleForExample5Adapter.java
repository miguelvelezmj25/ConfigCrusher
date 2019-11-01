package edu.cmu.cs.mvelezce.adapter.adapters.simpleForExample5;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleForExample5Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample5";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample5";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractSimpleForExample5Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleForExample5Adapter.PROGRAM_NAME,
        AbstractSimpleForExample5Adapter.MAIN_CLASS,
        AbstractSimpleForExample5Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleForExample5Adapter.OPTIONS);
  }
}

package edu.cmu.cs.mvelezce.adapter.adapters.simpleForExample2;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleForExample2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AbstractSimpleForExample2Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleForExample2Adapter.PROGRAM_NAME,
        AbstractSimpleForExample2Adapter.MAIN_CLASS,
        AbstractSimpleForExample2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleForExample2Adapter.OPTIONS);
  }
}

package edu.cmu.cs.mvelezce.adapters.simpleexample1;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleExample1Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleExample1";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleExample1";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractSimpleExample1Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleExample1Adapter.PROGRAM_NAME,
        AbstractSimpleExample1Adapter.MAIN_CLASS,
        AbstractSimpleExample1Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleExample1Adapter.OPTIONS);
  }
}

package edu.cmu.cs.mvelezce.adapters.subtraces4;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSubtraces4Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "subtraces4";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Subtraces4";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractSubtraces4Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSubtraces4Adapter.PROGRAM_NAME,
        AbstractSubtraces4Adapter.MAIN_CLASS,
        AbstractSubtraces4Adapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSubtraces4Adapter.OPTIONS);
  }
}

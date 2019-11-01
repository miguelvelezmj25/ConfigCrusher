package edu.cmu.cs.mvelezce.adapter.adapters.subtraces5;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSubtraces5Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "subtraces5";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Subtraces5";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractSubtraces5Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSubtraces5Adapter.PROGRAM_NAME,
        AbstractSubtraces5Adapter.MAIN_CLASS,
        AbstractSubtraces5Adapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSubtraces5Adapter.OPTIONS);
  }
}

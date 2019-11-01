package edu.cmu.cs.mvelezce.adapter.adapters.subtraces6;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSubtraces6Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "subtraces6";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Subtraces6";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractSubtraces6Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSubtraces6Adapter.PROGRAM_NAME,
        AbstractSubtraces6Adapter.MAIN_CLASS,
        AbstractSubtraces6Adapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSubtraces6Adapter.OPTIONS);
  }
}

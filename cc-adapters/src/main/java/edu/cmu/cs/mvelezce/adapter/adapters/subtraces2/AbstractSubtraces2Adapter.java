package edu.cmu.cs.mvelezce.adapter.adapters.subtraces2;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSubtraces2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "subtraces2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Subtraces2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractSubtraces2Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSubtraces2Adapter.PROGRAM_NAME,
        AbstractSubtraces2Adapter.MAIN_CLASS,
        AbstractSubtraces2Adapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSubtraces2Adapter.OPTIONS);
  }
}

package edu.cmu.cs.mvelezce.adapter.adapters.example4;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractExample4Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "example4";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example4";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractExample4Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractExample4Adapter.PROGRAM_NAME,
        AbstractExample4Adapter.MAIN_CLASS,
        "",
        AbstractExample4Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractExample4Adapter.OPTIONS);
  }
}

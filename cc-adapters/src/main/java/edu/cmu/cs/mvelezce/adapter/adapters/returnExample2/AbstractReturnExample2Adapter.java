package edu.cmu.cs.mvelezce.adapter.adapters.returnExample2;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractReturnExample2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "returnExample2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.ReturnExample2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractReturnExample2Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractReturnExample2Adapter.PROGRAM_NAME,
        AbstractReturnExample2Adapter.MAIN_CLASS,
        "",
        AbstractReturnExample2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractReturnExample2Adapter.OPTIONS);
  }
}

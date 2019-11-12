package edu.cmu.cs.mvelezce.adapters.phosphorExample2;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractPhosphorExample2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Example2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractPhosphorExample2Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractPhosphorExample2Adapter.PROGRAM_NAME,
        AbstractPhosphorExample2Adapter.MAIN_CLASS,
        AbstractPhosphorExample2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractPhosphorExample2Adapter.OPTIONS);
  }
}

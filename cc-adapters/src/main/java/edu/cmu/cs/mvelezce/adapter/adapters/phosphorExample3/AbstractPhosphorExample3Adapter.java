package edu.cmu.cs.mvelezce.adapter.adapters.phosphorExample3;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractPhosphorExample3Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Example3";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example3";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractPhosphorExample3Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractPhosphorExample3Adapter.PROGRAM_NAME,
        AbstractPhosphorExample3Adapter.MAIN_CLASS,
        AbstractPhosphorExample3Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractPhosphorExample3Adapter.OPTIONS);
  }
}

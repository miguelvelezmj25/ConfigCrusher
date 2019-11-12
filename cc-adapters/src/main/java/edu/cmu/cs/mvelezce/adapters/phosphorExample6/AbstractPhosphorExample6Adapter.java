package edu.cmu.cs.mvelezce.adapters.phosphorExample6;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractPhosphorExample6Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Example6";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example6";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractPhosphorExample6Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractPhosphorExample6Adapter.PROGRAM_NAME,
        AbstractPhosphorExample6Adapter.MAIN_CLASS,
        AbstractPhosphorExample6Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractPhosphorExample6Adapter.OPTIONS);
  }
}

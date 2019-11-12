package edu.cmu.cs.mvelezce.adapters.implicit2;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractImplicit2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "implicit2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Implicit2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractImplicit2Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractImplicit2Adapter.PROGRAM_NAME,
        AbstractImplicit2Adapter.MAIN_CLASS,
        AbstractImplicit2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractImplicit2Adapter.OPTIONS);
  }
}

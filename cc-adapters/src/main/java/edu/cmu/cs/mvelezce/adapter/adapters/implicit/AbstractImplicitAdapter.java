package edu.cmu.cs.mvelezce.adapter.adapters.implicit;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractImplicitAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "implicit";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Implicit";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractImplicitAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractImplicitAdapter.PROGRAM_NAME,
        AbstractImplicitAdapter.MAIN_CLASS,
        "",
        AbstractImplicitAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractImplicitAdapter.OPTIONS);
  }
}

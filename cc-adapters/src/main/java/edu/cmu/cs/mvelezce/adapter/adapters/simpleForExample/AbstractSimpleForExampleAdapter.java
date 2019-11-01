package edu.cmu.cs.mvelezce.adapter.adapters.simpleForExample;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSimpleForExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractSimpleForExampleAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSimpleForExampleAdapter.PROGRAM_NAME,
        AbstractSimpleForExampleAdapter.MAIN_CLASS,
        AbstractSimpleForExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSimpleForExampleAdapter.OPTIONS);
  }
}

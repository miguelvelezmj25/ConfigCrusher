package edu.cmu.cs.mvelezce.adapters.dynamicrunningexample;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractDynamicRunningExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "RunningExample";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.RunningExample";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractDynamicRunningExampleAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractDynamicRunningExampleAdapter.PROGRAM_NAME,
        AbstractDynamicRunningExampleAdapter.MAIN_CLASS,
        AbstractDynamicRunningExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractDynamicRunningExampleAdapter.OPTIONS);
  }
}

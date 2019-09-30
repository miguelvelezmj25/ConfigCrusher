package edu.cmu.cs.mvelezce.adapter.adapters.return2Example;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractReturn2ExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "return2Example";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Return2Example";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AbstractReturn2ExampleAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractReturn2ExampleAdapter.PROGRAM_NAME,
        AbstractReturn2ExampleAdapter.MAIN_CLASS,
        "",
        AbstractReturn2ExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractReturn2ExampleAdapter.OPTIONS);
  }
}

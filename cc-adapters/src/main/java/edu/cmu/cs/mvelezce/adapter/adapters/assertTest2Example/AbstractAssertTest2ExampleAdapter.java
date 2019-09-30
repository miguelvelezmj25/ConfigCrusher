package edu.cmu.cs.mvelezce.adapter.adapters.assertTest2Example;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractAssertTest2ExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "AssertTest2Example";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.AssertTest2Example";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AbstractAssertTest2ExampleAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractAssertTest2ExampleAdapter.PROGRAM_NAME,
        AbstractAssertTest2ExampleAdapter.MAIN_CLASS,
        "",
        AbstractAssertTest2ExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractAssertTest2ExampleAdapter.OPTIONS);
  }
}

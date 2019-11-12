package edu.cmu.cs.mvelezce.adapters.ifand;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractIfAndAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "IfAnd";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.IfAnd";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractIfAndAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractIfAndAdapter.PROGRAM_NAME,
        AbstractIfAndAdapter.MAIN_CLASS,
        AbstractIfAndAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractIfAndAdapter.OPTIONS);
  }
}

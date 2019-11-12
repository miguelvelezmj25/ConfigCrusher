package edu.cmu.cs.mvelezce.adapters.throwIf;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractThrowIfAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "throwIf";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.ThrowIf";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractThrowIfAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractThrowIfAdapter.PROGRAM_NAME,
        AbstractThrowIfAdapter.MAIN_CLASS,
        AbstractThrowIfAdapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractThrowIfAdapter.OPTIONS);
  }
}

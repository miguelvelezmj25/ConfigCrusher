package edu.cmu.cs.mvelezce.adapters.andContext;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractAndContextAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "andContext";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.AndContext";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C", "D"};

  public AbstractAndContextAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractAndContextAdapter.PROGRAM_NAME,
        AbstractAndContextAdapter.MAIN_CLASS,
        AbstractAndContextAdapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractAndContextAdapter.OPTIONS);
  }
}

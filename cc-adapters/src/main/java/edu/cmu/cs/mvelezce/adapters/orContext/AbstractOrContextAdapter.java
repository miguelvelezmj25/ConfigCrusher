package edu.cmu.cs.mvelezce.adapters.orContext;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractOrContextAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "OrContext";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.OrContext";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractOrContextAdapter() {
    // TODO check why we are passing empty string
    super(
        AbstractOrContextAdapter.PROGRAM_NAME,
        AbstractOrContextAdapter.MAIN_CLASS,
        AbstractOrContextAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractOrContextAdapter.OPTIONS);
  }
}

package edu.cmu.cs.mvelezce.adapters.alldynamic;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractAllDynamicAdapter extends BaseAdapter {

  public static final String MAIN_PACKAGE = "edu.cmu.cs.mvelezce.analysis";

  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C", "D", "E"};

  public AbstractAllDynamicAdapter(String programName, String mainClass) {
    // TODO check why we are passing empty string
    super(programName, mainClass, AbstractAllDynamicAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractAllDynamicAdapter.OPTIONS);
  }
}

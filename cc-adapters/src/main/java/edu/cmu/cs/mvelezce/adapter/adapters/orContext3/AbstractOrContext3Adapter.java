package edu.cmu.cs.mvelezce.adapter.adapters.orContext3;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractOrContext3Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "OrContext3";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.OrContext3";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractOrContext3Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractOrContext3Adapter.PROGRAM_NAME,
        AbstractOrContext3Adapter.MAIN_CLASS,
        AbstractOrContext3Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractOrContext3Adapter.OPTIONS);
  }
}

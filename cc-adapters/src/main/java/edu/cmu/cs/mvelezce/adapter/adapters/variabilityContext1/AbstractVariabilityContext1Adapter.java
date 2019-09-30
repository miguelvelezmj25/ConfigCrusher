package edu.cmu.cs.mvelezce.adapter.adapters.variabilityContext1;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractVariabilityContext1Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "variabilityContext1";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.VariabilityContext1";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AbstractVariabilityContext1Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractVariabilityContext1Adapter.PROGRAM_NAME,
        AbstractVariabilityContext1Adapter.MAIN_CLASS,
        "",
        AbstractVariabilityContext1Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractVariabilityContext1Adapter.OPTIONS);
  }
}

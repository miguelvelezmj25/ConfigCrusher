package edu.cmu.cs.mvelezce.adapter.adapters.variabilityContext2;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractVariabilityContext2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "variabilityContext2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.VariabilityContext2";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractVariabilityContext2Adapter() {
    // TODO check that we are passing empty string
    super(
        AbstractVariabilityContext2Adapter.PROGRAM_NAME,
        AbstractVariabilityContext2Adapter.MAIN_CLASS,
        AbstractVariabilityContext2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractVariabilityContext2Adapter.OPTIONS);
  }
}

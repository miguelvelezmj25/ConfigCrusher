package edu.cmu.cs.mvelezce.adapter.adapters.example1;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractExample1Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "example1";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example1";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractExample1Adapter() {
    // TODO check why we are passing empty string
    super(
        AbstractExample1Adapter.PROGRAM_NAME,
        AbstractExample1Adapter.MAIN_CLASS,
        AbstractExample1Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractExample1Adapter.OPTIONS);
  }
}

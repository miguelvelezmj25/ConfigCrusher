package edu.cmu.cs.mvelezce.adapters.dummy;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseDummyAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "dummy";
  private static final String MAIN_CLASS = "";
  private static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  private static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  public BaseDummyAdapter() {
    // TODO check that we are passing empty string
    super(BaseDummyAdapter.PROGRAM_NAME, BaseDummyAdapter.MAIN_CLASS, new ArrayList<>());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    throw new UnsupportedOperationException("Should not execute");
  }
}

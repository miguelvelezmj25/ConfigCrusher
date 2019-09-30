package edu.cmu.cs.mvelezce.tool.execute.java.adapter.dummy;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DummyAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "dummy";
  private static final String MAIN_CLASS = "";
  private static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  private static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  public DummyAdapter() {
    // TODO check that we are passing empty string
    super(DummyAdapter.PROGRAM_NAME, DummyAdapter.MAIN_CLASS, "", new ArrayList<>());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    throw new UnsupportedOperationException("Should not execute");
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Should not execute");
  }
}

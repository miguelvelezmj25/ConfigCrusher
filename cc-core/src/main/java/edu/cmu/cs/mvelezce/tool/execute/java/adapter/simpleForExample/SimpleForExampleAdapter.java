package edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SimpleForExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public SimpleForExampleAdapter() {
    // TODO check that we are passing empty string
    super(SimpleForExampleAdapter.PROGRAM_NAME, SimpleForExampleAdapter.MAIN_CLASS, "",
        SimpleForExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(SimpleForExampleAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(SimpleForExampleMain.SIMPLE_FOR_EXAMPLE_MAIN, newArgs);
  }
}

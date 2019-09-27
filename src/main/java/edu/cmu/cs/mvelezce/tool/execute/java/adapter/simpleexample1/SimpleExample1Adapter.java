package edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SimpleExample1Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleExample1";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleExample1";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public SimpleExample1Adapter() {
    // TODO check that we are passing empty string
    super(SimpleExample1Adapter.PROGRAM_NAME, SimpleExample1Adapter.MAIN_CLASS, "",
        SimpleExample1Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(SimpleExample1Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(SimpleExample1Main.SIMPLE_EXAMPLE_1_MAIN, newArgs);
  }
}

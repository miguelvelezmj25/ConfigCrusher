package edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample6;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SimpleForExample6Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample6";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample6";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public SimpleForExample6Adapter() {
    // TODO check that we are passing empty string
    super(SimpleForExample6Adapter.PROGRAM_NAME, SimpleForExample6Adapter.MAIN_CLASS, "",
        SimpleForExample6Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(SimpleForExample6Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(SimpleForExample6Main.SIMPLE_FOR_EXAMPLE_6_MAIN, newArgs);
  }
}

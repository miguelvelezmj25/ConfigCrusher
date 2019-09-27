package edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample4;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SimpleForExample4Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "simpleForExample4";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.SimpleForExample4";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public SimpleForExample4Adapter() {
    // TODO check that we are passing empty string
    super(SimpleForExample4Adapter.PROGRAM_NAME, SimpleForExample4Adapter.MAIN_CLASS, "",
        SimpleForExample4Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(SimpleForExample4Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(SimpleForExample4Main.SIMPLE_FOR_EXAMPLE_4_MAIN, newArgs);
  }
}

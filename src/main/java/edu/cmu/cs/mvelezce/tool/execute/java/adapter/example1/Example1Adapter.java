package edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Example1Adapter extends BaseAdapter {

  // TODO change name
  public static final String PROGRAM_NAME = "example1";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example1";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public Example1Adapter() {
    // TODO check that we are passing empty string
    super(Example1Adapter.PROGRAM_NAME, Example1Adapter.MAIN_CLASS, "",
        Example1Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(Example1Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(Example1Main.EXAMPLE_1_MAIN, newArgs);
  }
}

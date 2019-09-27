package edu.cmu.cs.mvelezce.tool.execute.java.adapter.example4;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Example4Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "example4";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example4";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public Example4Adapter() {
    // TODO check why we are passing empty string
    super(Example4Adapter.PROGRAM_NAME, Example4Adapter.MAIN_CLASS, "",
        Example4Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(Example4Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(Example4Main.EXAMPLE_4_MAIN, newArgs);
  }
}

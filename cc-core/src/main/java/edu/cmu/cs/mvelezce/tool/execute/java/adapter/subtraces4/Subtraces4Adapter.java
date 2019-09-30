package edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces4;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Subtraces4Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "subtraces4";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Subtraces4";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public Subtraces4Adapter() {
    // TODO check that we are passing empty string
    super(Subtraces4Adapter.PROGRAM_NAME, Subtraces4Adapter.MAIN_CLASS, "",
        Subtraces4Adapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(Subtraces4Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(Subtraces4Main.SUBTRACES_4_MAIN, newArgs);
  }
}

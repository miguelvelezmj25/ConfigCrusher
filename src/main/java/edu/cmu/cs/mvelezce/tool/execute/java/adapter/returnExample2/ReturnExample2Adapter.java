package edu.cmu.cs.mvelezce.tool.execute.java.adapter.returnExample2;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ReturnExample2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "returnExample2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.ReturnExample2";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public ReturnExample2Adapter() {
    // TODO check that we are passing empty string
    super(ReturnExample2Adapter.PROGRAM_NAME, ReturnExample2Adapter.MAIN_CLASS, "",
        ReturnExample2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(ReturnExample2Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(ReturnExample2Main.RETURN_EXAMPLE_2_MAIN, newArgs);
  }
}

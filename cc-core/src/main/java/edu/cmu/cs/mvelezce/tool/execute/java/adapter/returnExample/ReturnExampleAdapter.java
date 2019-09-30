package edu.cmu.cs.mvelezce.tool.execute.java.adapter.returnExample;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ReturnExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "returnExample";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.ReturnExample";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public ReturnExampleAdapter() {
    // TODO check that we are passing empty string
    super(ReturnExampleAdapter.PROGRAM_NAME, ReturnExampleAdapter.MAIN_CLASS, "",
        ReturnExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(ReturnExampleAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(ReturnExampleMain.RETURN_EXAMPLE_MAIN, newArgs);
  }
}

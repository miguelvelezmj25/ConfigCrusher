package edu.cmu.cs.mvelezce.tool.execute.java.adapter.andContext;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AndContextAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "andContext";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.AndContext";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C", "D"};

  public AndContextAdapter() {
    // TODO check that we are passing empty string
    super(AndContextAdapter.PROGRAM_NAME, AndContextAdapter.MAIN_CLASS, "",
        AndContextAdapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AndContextAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(AndContextMain.AND_CONTEXT_MAIN, newArgs);
  }
}

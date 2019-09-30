package edu.cmu.cs.mvelezce.tool.execute.java.adapter.constructor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ConstructorAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Constructor";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Constructor";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public ConstructorAdapter() {
    // TODO check that we are passing empty string
    super(ConstructorAdapter.PROGRAM_NAME, ConstructorAdapter.MAIN_CLASS, "",
        ConstructorAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(ConstructorAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(ConstructorMain.CONSTRUCTOR_MAIN, newArgs);
  }
}

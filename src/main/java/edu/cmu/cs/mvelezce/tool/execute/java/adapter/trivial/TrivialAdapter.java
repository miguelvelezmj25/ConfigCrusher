package edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TrivialAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "Trivial";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Trivial";
  public static final String ROOT_PACKAGE = "edu.cmu.cs.mvelezce.analysis";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_DIR_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public TrivialAdapter() {
    // TODO check that we are passing empty string
    super(TrivialAdapter.PROGRAM_NAME, TrivialAdapter.MAIN_CLASS, "",
        TrivialAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(TrivialAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(TrivialMain.TRIVIAL_MAIN, newArgs);
  }
}

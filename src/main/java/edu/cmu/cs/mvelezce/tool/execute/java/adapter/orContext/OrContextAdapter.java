package edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OrContextAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "OrContext";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.OrContext";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";


  private static final String[] OPTIONS = {"A", "B", "C"};

  public OrContextAdapter() {
    // TODO check why we are passing empty string
    super(OrContextAdapter.PROGRAM_NAME, OrContextAdapter.MAIN_CLASS, "",
        OrContextAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(OrContextAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(OrContextMain.OR_CONTEXT_MAIN, newArgs);
  }
}

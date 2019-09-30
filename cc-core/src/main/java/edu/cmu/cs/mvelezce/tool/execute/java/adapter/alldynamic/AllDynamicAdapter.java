package edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AllDynamicAdapter extends BaseAdapter {

  public static final String MAIN_PACKAGE = "edu.cmu.cs.mvelezce.analysis";

  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C", "D", "E"};

  public AllDynamicAdapter(String programName, String mainClass) {
    // TODO check why we are passing empty string
    super(programName, mainClass, "", AllDynamicAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AllDynamicAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Is this called?");
//    String[] args = this.configurationAsMainArguments(configuration);
//    String[] newArgs = new String[args.length + 1];
//
//    newArgs[0] = iteration + "";
//    System.arraycopy(args, 0, newArgs, 1, args.length);
//
//    this.execute(AllDynamicMain.ALL_DYNAMIC_MAIN, newArgs);
  }
}

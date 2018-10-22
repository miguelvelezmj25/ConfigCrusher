package edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DynamicRunningExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "phosphor-examples";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.RunningExample";

  private static final String[] OPTIONS = {"A", "B"};

  public DynamicRunningExampleAdapter() {
    this("");
  }

  private DynamicRunningExampleAdapter(String dir) {
    super(DynamicRunningExampleAdapter.PROGRAM_NAME, DynamicRunningExampleAdapter.MAIN_CLASS, dir,
        DynamicRunningExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(DynamicRunningExampleAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(DynamicRunningExampleMain.DYNAMIC_RUNNING_EXAMPLE_MAIN, newArgs);
  }
}

package edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DynamicRunningExampleAdapter extends BaseAdapter {

  private static final String[] OPTIONS = {"A", "B"};

  private DynamicRunningExampleAdapter(Builder builder) {
    super(builder.programName, builder.mainClass, builder.directory, builder.options);
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

  public static class Builder {

    private final List<String> options;

    private String programName;
    private String mainClass;
    private String directory;

    public Builder() {
      this.options = DynamicRunningExampleAdapter.getListOfOptions();
    }

    public Builder programName(String programName) {
      this.programName = programName;
      return this;
    }

    public Builder mainClass(String mainClass) {
      this.mainClass = mainClass;
      return this;
    }

    public Builder directory(String directory) {
      this.directory = directory;
      return this;
    }

    public DynamicRunningExampleAdapter build() {
      return new DynamicRunningExampleAdapter(this);
    }
  }
}

package edu.cmu.cs.mvelezce.tool.execute.java.adapter.nesting;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NestingAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "nesting";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Nesting";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public NestingAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        NestingAdapter.PROGRAM_NAME,
        NestingAdapter.MAIN_CLASS,
        "",
        NestingAdapter.getListOfOptions());
  }

  public NestingAdapter(String programName, String entryPoint, String classDir) {
    super(programName, entryPoint, classDir, NestingAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(NestingAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(NestingMain.NESTING_MAIN, newArgs);
  }
}

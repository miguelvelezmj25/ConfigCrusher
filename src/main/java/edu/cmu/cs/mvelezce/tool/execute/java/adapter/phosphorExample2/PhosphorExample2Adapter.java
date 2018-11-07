package edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PhosphorExample2Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "phosphorExample2";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Example2";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";


  private static final String[] OPTIONS = {"A", "B"};

  public PhosphorExample2Adapter() {
    // TODO check why we are passing empty string
    super(PhosphorExample2Adapter.PROGRAM_NAME, PhosphorExample2Adapter.MAIN_CLASS, "",
        PhosphorExample2Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(PhosphorExample2Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(PhosphorExample2Main.PHOSPHOR_EXAMPLE_2_MAIN, newArgs);
  }
}

package edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext3;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OrContext3Adapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "OrContext3";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.OrContext3";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";


  private static final String[] OPTIONS = {"A", "B", "C"};

  public OrContext3Adapter() {
    // TODO check why we are passing empty string
    super(OrContext3Adapter.PROGRAM_NAME, OrContext3Adapter.MAIN_CLASS, "",
        OrContext3Adapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(OrContext3Adapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(OrContext3Main.OR_CONTEXT_3_MAIN, newArgs);
  }
}

package edu.cmu.cs.mvelezce.tool.execute.java.adapter.assertTest2Example;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AssertTest2ExampleAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "AssertTest2Example";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.AssertTest2Example";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A"};

  public AssertTest2ExampleAdapter() {
    // TODO check that we are passing empty string
    super(AssertTest2ExampleAdapter.PROGRAM_NAME, AssertTest2ExampleAdapter.MAIN_CLASS, "",
        AssertTest2ExampleAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AssertTest2ExampleAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Implement");
//    String[] args = this.configurationAsMainArguments(configuration);
//    String[] newArgs = new String[args.length + 1];
//
//    newArgs[0] = iteration + "";
//    System.arraycopy(args, 0, newArgs, 1, args.length);
//
//    this.execute(AssertTest2ExampleMain.IF_AND_MAIN, newArgs);
  }
}

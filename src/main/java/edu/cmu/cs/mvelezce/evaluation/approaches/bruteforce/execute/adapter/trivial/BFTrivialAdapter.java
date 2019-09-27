package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.trivial;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.Set;

public class BFTrivialAdapter extends TrivialAdapter {

  public BFTrivialAdapter(String programName, String entryPoint, String classDir) {
    super(programName, entryPoint, classDir);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(BFTrivialMain.BF_TRIVIAL_MAIN, newArgs);
  }
}

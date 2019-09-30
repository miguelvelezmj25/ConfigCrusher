package edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep;

import edu.cmu.cs.mvelezce.adapter.adapters.grep.AbstractGrepAdapter;

import java.io.IOException;
import java.util.Set;

public class GrepAdapter extends AbstractGrepAdapter {

  public GrepAdapter() {
    this(null, null, null);
  }

  public GrepAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep.GrepMain.GREP_MAIN, newArgs);
  }
}

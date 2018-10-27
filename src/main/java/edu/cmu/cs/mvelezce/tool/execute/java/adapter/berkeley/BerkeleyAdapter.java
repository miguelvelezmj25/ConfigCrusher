package edu.cmu.cs.mvelezce.tool.execute.java.adapter.berkeley;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BerkeleyAdapter extends BaseAdapter {

  public BerkeleyAdapter() {
    this(null, null, null);
  }

  public BerkeleyAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, BerkeleyAdapter.getBerkeleyOptions());
  }

  public static List<String> getBerkeleyOptions() {
    String[] options = {"RECORDS", "DATA", "DUPLICATES", "KEYSIZE", "SEQUENTIAL"};

    return Arrays.asList(options);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(BerkeleyMain.BERKELEY_MAIN, newArgs);
  }
}

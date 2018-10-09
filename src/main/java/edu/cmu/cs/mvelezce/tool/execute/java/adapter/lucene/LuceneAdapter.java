package edu.cmu.cs.mvelezce.tool.execute.java.adapter.lucene;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LuceneAdapter extends BaseAdapter {

  public LuceneAdapter() {
    this(null, null, null);
  }

  public LuceneAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, LuceneAdapter
        .getLuceneOptions());
  }

  public static List<String> getLuceneOptions() {
    String[] options = {"CREATE", "RAM_BUFFER_SIZE", "MERGE_POLICY", "CODEC", "MAX_BUFFERED_DOCS",
        "USE_COMPOUND_FILE", "MAX_TOKEN_LENGTH"};

    return Arrays.asList(options);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(LuceneMain.LUCENE_MAIN, newArgs);
  }
}

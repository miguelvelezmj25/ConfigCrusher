package edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles;

import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class IndexFilesMain extends BaseMain {

  static final String INDEX_FILES_MAIN = IndexFilesMain.class.getCanonicalName();

  private IndexFilesMain(String programName, String iteration, String[] args) {
    super(programName, iteration, args);
  }

  public static void main(String[] args) throws IOException {
    String programName = args[0];
    String mainClass = args[1];
    String iteration = args[2];
    String[] trivialArgs = Arrays.copyOfRange(args, 3, args.length);

    Main main = new IndexFilesMain(programName, iteration, trivialArgs);
    main.execute(mainClass, trivialArgs);
    main.logExecution();
  }

  @Override
  public void logExecution() throws IOException {
    Adapter adapter = new IndexFilesAdapter();
    Set<String> configuration = adapter.configurationAsSet(this.getArgs());

    ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
    Map<String, Long> results = executor.getResults();
    executor.writeToFile(this.getIteration(), configuration, results);
  }

  @Override
  public void execute(String mainClass, String[] args) {
    throw new UnsupportedOperationException("Implement this logic");
  }
}

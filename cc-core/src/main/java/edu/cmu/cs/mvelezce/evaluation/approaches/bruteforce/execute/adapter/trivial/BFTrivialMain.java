package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.trivial;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialMain;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class BFTrivialMain extends TrivialMain {

  static final String BF_TRIVIAL_MAIN = BFTrivialMain.class.getCanonicalName();

  private BFTrivialMain(String programName, String iteration, String[] args) {
    super(programName, iteration, args);
  }

  public static void main(String[] args) throws IOException {
    String programName = args[0];
    String mainClass = args[1];
    String iteration = args[2];
    String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

    Main main = new BFTrivialMain(programName, iteration, sleepArgs);
    main.execute(mainClass, sleepArgs);
    main.logExecution();
  }

  @Override
  public void logExecution() throws IOException {
    Adapter adapter = new TrivialAdapter();
    Set<String> configuration = adapter.configurationAsSet(this.getArgs());

    BruteForceExecutor executor = new BruteForceExecutor(this.getProgramName());
    Map<String, Long> results = executor.getResults();
    executor.writeToFile(this.getIteration(), configuration, results);
  }
}

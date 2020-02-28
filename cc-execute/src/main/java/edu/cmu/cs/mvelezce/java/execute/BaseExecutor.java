package edu.cmu.cs.mvelezce.java.execute;

import com.mijecu25.meme.utils.gc.GC;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public abstract class BaseExecutor<T> implements Executor {

  public static final String USER = "user";
  public static final String REAL = "real";

  private final String programName;
  private final Set<Set<String>> configurations;
  private final BaseRawExecutionParser<T> rawExecutionParser;
  private final int waitAfterExecution;

  public BaseExecutor(
      String programName,
      Set<Set<String>> configurations,
      BaseRawExecutionParser<T> rawExecutionParser,
      int waitAfterExecution) {
    this.programName = programName;
    this.configurations = configurations;
    this.rawExecutionParser = rawExecutionParser;
    this.waitAfterExecution = waitAfterExecution;
  }

  @Override
  public void execute(String[] args) throws IOException, InterruptedException {
    Options.getCommandLine(args);

    String outputDir = this.outputDir() + "/" + this.programName;
    File file = new File(outputDir);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return;
    }

    int iterations = Options.getIterations();
    this.execute(iterations);
  }

  @Override
  public void execute(int iterations) throws InterruptedException, IOException {
    for (int i = 0; i < iterations; i++) {
      this.executeIteration(i);
    }
  }

  @Override
  public void executeIteration(int iteration) throws InterruptedException, IOException {
    ExecutorAdapter adapter = this.getExecutorAdapter();
    System.out.println("Executing " + this.configurations.size() + " configs");

    for (Set<String> configuration : this.configurations) {
      adapter.execute(configuration);
      this.rawExecutionParser.logExecution(configuration, iteration);

      GC.gc(this.waitAfterExecution);
    }
  }

  protected abstract ExecutorAdapter getExecutorAdapter();

  public String getProgramName() {
    return programName;
  }

  public Set<Set<String>> getConfigurations() {
    return configurations;
  }

  public BaseRawExecutionParser<T> getRawExecutionParser() {
    return rawExecutionParser;
  }
}

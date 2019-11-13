package edu.cmu.cs.mvelezce.java.execute;

import com.mijecu25.meme.utils.gc.GC;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public abstract class BaseExecutor implements Executor {

  private final String programName;
  private final Set<Set<String>> configurations;
  private final BaseRawExecutionParser rawExecutionParser;

  public BaseExecutor(
      String programName,
      Set<Set<String>> configurations,
      BaseRawExecutionParser rawExecutionParser) {
    this.programName = programName;
    this.configurations = configurations;
    this.rawExecutionParser = rawExecutionParser;
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

    for (Set<String> configuration : this.configurations) {
      adapter.execute(configuration);
      this.rawExecutionParser.logExecution(configuration, iteration);

      GC.gc(2000);
    }
  }

  protected abstract ExecutorAdapter getExecutorAdapter();

  public String getProgramName() {
    return programName;
  }

  public Set<Set<String>> getConfigurations() {
    return configurations;
  }

  public BaseRawExecutionParser getRawExecutionParser() {
    return rawExecutionParser;
  }
}

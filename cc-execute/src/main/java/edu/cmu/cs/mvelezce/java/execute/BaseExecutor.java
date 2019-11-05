package edu.cmu.cs.mvelezce.java.execute;

import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.trivial.TrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.java.processor.RawExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;
import edu.cmu.cs.mvelezce.utils.gc.GC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BaseExecutor implements Executor {

  private final String programName;
  private final Set<Set<String>> configurations;
  private final RawExecutionParser rawExecutionParser;

  public BaseExecutor(String programName, Set<Set<String>> configurations) {
    this.programName = programName;
    this.configurations = configurations;

    this.rawExecutionParser = new RawExecutionParser(programName, this.outputDir());
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
  public void executeProgram(String programClassPath, String mainClass, String[] configArgs)
      throws InterruptedException, IOException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(programClassPath, mainClass, configArgs);
    builder.command(commandList);

    Process process = builder.start();

    edu.cmu.cs.mvelezce.utils.execute.Executor.processOutput(process);
    edu.cmu.cs.mvelezce.utils.execute.Executor.processError(process);

    process.waitFor();
  }

  @Override
  public void executeIteration(int iteration) throws InterruptedException, IOException {
    ExecutorAdapter adapter;

    switch (this.programName) {
      case TrivialExecutorAdapter.PROGRAM_NAME:
        adapter = new TrivialExecutorAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.programName);
    }

    for (Set<String> configuration : this.configurations) {
      adapter.execute(configuration);
      this.rawExecutionParser.logExecution(configuration, iteration);

      GC.gc(2000);
    }
  }

  private List<String> buildCommandAsList(
      String programClassPath, String mainClass, String[] configArgs) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add("-Xmx26g");
    commandList.add("-Xms26g");
    commandList.add("-XX:+UseConcMarkSweepGC");
    //    commandList.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005");
    commandList.add("-cp");

    commandList.add(this.getClassPath(programClassPath));
    commandList.add(mainClass);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }

  private String getClassPath(String programClassPath) {
    return edu.cmu.cs.mvelezce.utils.execute.Executor.CLASS_PATH
        + edu.cmu.cs.mvelezce.utils.execute.Executor.PATH_SEPARATOR
        + "../cc-analysis/"
        + edu.cmu.cs.mvelezce.utils.execute.Executor.CLASS_PATH
        + edu.cmu.cs.mvelezce.utils.execute.Executor.PATH_SEPARATOR
        + "../../producer-consumer-4j/"
        + edu.cmu.cs.mvelezce.utils.execute.Executor.CLASS_PATH
        + edu.cmu.cs.mvelezce.utils.execute.Executor.PATH_SEPARATOR
        + programClassPath;
  }
}

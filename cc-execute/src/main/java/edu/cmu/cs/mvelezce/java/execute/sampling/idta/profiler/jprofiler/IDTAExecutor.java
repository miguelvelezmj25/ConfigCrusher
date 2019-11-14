package edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.adapters.measureDiskOrderedScan.profiler.jprofiler.JProfilerSamplingMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.adapters.trivial.profiler.jprofiler.JProfilerSamplingTrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler.RawExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.IOException;
import java.util.*;

public class IDTAExecutor extends BaseExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute/" + Options.DIRECTORY + "/executor/java/idta/programs/sampling";

  public IDTAExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  IDTAExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new RawExecutionParser(programName, OUTPUT_DIR));
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case JProfilerSamplingTrivialExecutorAdapter.PROGRAM_NAME:
        adapter = new JProfilerSamplingTrivialExecutorAdapter(this);
        break;
      case JProfilerSamplingMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new JProfilerSamplingMeasureDiskOrderedScanAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }

  public void executeProgram(
      String programClassPath, String mainClass, String agentPath, String[] configArgs)
      throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList =
        this.buildCommandAsList(programClassPath, mainClass, agentPath, configArgs);
    builder.command(commandList);

    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList(
      String programClassPath, String mainClass, String agentPath, String[] configArgs) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add(agentPath);
    commandList.add("-Xmx26g");
    commandList.add("-Xms26g");
    commandList.add("-cp");
    commandList.add(programClassPath);
    commandList.add(mainClass);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }
}

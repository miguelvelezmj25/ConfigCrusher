package edu.cmu.cs.mvelezce.e2e.execute.executor;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.convert.E2EConvertAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.indexFiles.E2EIndexFilesAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.measureDiskOrderedScan.E2EMeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.multithread.E2EMultithreadAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.runBenchC.E2ERunBenchCAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class E2EExecutor extends BaseExecutor<ProcessedPerfExecution> {

  public static final String OUTPUT_DIR = "../cc-execute-e2e/" + Options.DIRECTORY + "/execute";

  protected E2EExecutor(
      String programName,
      Set<Set<String>> configurations,
      BaseE2EExecutionParser e2EExecutionParser,
      int waitAfterExecution) {
    super(programName, configurations, e2EExecutionParser, waitAfterExecution);
  }

  protected abstract List<String> buildCommandAsList(
      String programClassPath, String mainClass, String[] configArgs);

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new E2EMeasureDiskOrderedScanExecutorAdapter(this);
        break;
      case BaseIndexFilesAdapter.PROGRAM_NAME:
        adapter = new E2EIndexFilesAdapter(this);
        break;
      case BaseConvertAdapter.PROGRAM_NAME:
        adapter = new E2EConvertAdapter(this);
        break;
      case BaseMultithreadAdapter.PROGRAM_NAME:
        adapter = new E2EMultithreadAdapter(this);
        break;
      case BaseRunBenchCAdapter.PROGRAM_NAME:
        adapter = new E2ERunBenchCAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }

  public void executeProgram(String programClassPath, String mainClass, String[] configArgs)
      throws InterruptedException, IOException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(programClassPath, mainClass, configArgs);
    builder.command(commandList);

    Process process = builder.start();

    Executor.processOutput(process);
    Executor.processError(process);

    process.waitFor();
  }
}

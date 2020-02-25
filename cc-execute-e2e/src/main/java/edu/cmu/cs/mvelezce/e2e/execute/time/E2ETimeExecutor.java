package edu.cmu.cs.mvelezce.e2e.execute.time;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.executor.E2EExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.E2ETimeExecutionParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class E2ETimeExecutor extends E2EExecutor {

  public static final String OUTPUT_DIR = E2EExecutor.OUTPUT_DIR + "/time";

  private static final String SCRIPTS_DIR =
      "../cc-execute-e2e/src/main/java/edu/cmu/cs/mvelezce/e2e/execute/time/scripts/";

  protected E2ETimeExecutor(
      String programName,
      Set<Set<String>> configurations,
      E2ETimeExecutionParser e2ETimeExecutionParser,
      int waitAfterExecution) {
    super(programName, configurations, e2ETimeExecutionParser, waitAfterExecution);
  }

  @Override
  protected List<String> buildCommandAsList(
      String programClassPath, String mainClass, String[] configArgs) {
    List<String> commandList = new ArrayList<>();

    switch (this.getProgramName()) {
      case BaseMultithreadAdapter.PROGRAM_NAME:
        commandList.add(SCRIPTS_DIR + "multithread.sh");
        break;
      case BaseConvertAdapter.PROGRAM_NAME:
        commandList.add(SCRIPTS_DIR + "convert.sh");
        break;
      case BaseRunBenchCAdapter.PROGRAM_NAME:
        commandList.add(SCRIPTS_DIR + "runBenchC.sh");
        break;
      default:
        throw new RuntimeException("Could not find a script for " + this.getProgramName());
    }

    commandList.add(programClassPath);
    commandList.add(mainClass);

    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }
}

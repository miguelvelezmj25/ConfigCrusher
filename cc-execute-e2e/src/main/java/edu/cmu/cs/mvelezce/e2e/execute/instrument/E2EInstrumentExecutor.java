package edu.cmu.cs.mvelezce.e2e.execute.instrument;

import edu.cmu.cs.mvelezce.e2e.execute.executor.E2EExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.instrument.parser.E2EInstrumentExecutionParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class E2EInstrumentExecutor extends E2EExecutor {

  public static final String OUTPUT_DIR = E2EExecutor.OUTPUT_DIR + "/instrument";

  protected E2EInstrumentExecutor(
      String programName,
      Set<Set<String>> configurations,
      E2EInstrumentExecutionParser e2EInstrumentExecutionParser,
      int waitAfterExecution) {
    super(programName, configurations, e2EInstrumentExecutionParser, waitAfterExecution);
  }

  @Override
  protected List<String> buildCommandAsList(
      String programClassPath, String mainClass, String[] configArgs) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add("-Xmx12g");
    commandList.add("-Xms12g");
    commandList.add("-cp");

    commandList.add(programClassPath);
    commandList.add(mainClass);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }
}

package edu.cmu.cs.mvelezce.java.execute.instrumentation;

import com.mijecu25.meme.utils.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BaseInstrumentExecutor<T> extends BaseExecutor<T> {

  public BaseInstrumentExecutor(
      String programName,
      Set<Set<String>> configurations,
      BaseRawExecutionParser<T> rawExecutionParser) {
    super(programName, configurations, rawExecutionParser);
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

  private List<String> buildCommandAsList(
      String programClassPath, String mainClass, String[] configArgs) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add("-Xmx12g");
    commandList.add("-Xms12g");
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
    return programClassPath
    //            Executor.CLASS_PATH
    //        + Executor.PATH_SEPARATOR
    //        + "../cc-analysis/"
    //        + Executor.CLASS_PATH
    //        + Executor.PATH_SEPARATOR
    //        + "../cc-utils/"
    //        + Executor.CLASS_PATH
    //        + Executor.PATH_SEPARATOR
    //        + "../../producer-consumer-4j/"
    //        + Executor.CLASS_PATH
    //        + Executor.PATH_SEPARATOR
    //    +
    ;
  }
}

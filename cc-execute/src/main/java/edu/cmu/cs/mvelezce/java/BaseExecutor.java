package edu.cmu.cs.mvelezce.java;

import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BaseExecutor implements Executor {

  private final String programName;
  private final Set<Set<String>> configurations;

  public BaseExecutor(String programName, Set<Set<String>> configurations) {
    this.programName = programName;
    this.configurations = configurations;
  }

  @Override
  public Object execute(String[] args) throws IOException, InterruptedException {
    Options.getCommandLine(args);

    String outputDir = this.outputDir() + "/" + this.programName;
    File file = new File(outputDir);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      throw new UnsupportedOperationException("Implement");
      //      List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();
      //
      //      int i = 0;
      //      File outputFile = new File(root + "/" + i);
      //
      //      while (outputFile.exists()) {
      //        Set<DefaultPerformanceEntry> results = this.aggregateExecutions(outputFile);
      //        performanceEntriesList.add(results);
      //
      //        i++;
      //        outputFile = new File(root + "/" + i);
      //      }
      //
      //      Set<PerformanceEntryStatistic> averagedPerformanceEntries =
      //          this.averageExecutions(performanceEntriesList);
      //      return averagedPerformanceEntries;
    }

    int iterations = Options.getIterations();

    return this.execute(iterations);
  }

  @Override
  public Object execute(int iterations) throws InterruptedException, IOException {
    for (int i = 0; i < iterations; i++) {
      Object perfMeasurement = this.executeIteration(i);
      System.err.println("Add the perf result to a collection of perf results");
    }

    throw new UnsupportedOperationException("Return the collection of perf results");

    //    List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();
    //
    //    for(int i = 0; i < this.repetitions; i++) {
    //      Set<DefaultPerformanceEntry> results = this.execute(i);
    //
    //      if(i < 0) {
    //        continue;
    //      }
    //
    //      performanceEntriesList.add(results);
    //    }
    //
    //    Set<PerformanceEntryStatistic> averagedPerformanceEntries =
    // this.averageExecutions(performanceEntriesList);
    //
    //    return averagedPerformanceEntries;
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

  private List<String> buildCommandAsList(
      String programClassPath, String mainClass, String[] configArgs) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add("-Xmx26g");
    commandList.add("-Xms26g");
    commandList.add("-XX:+UseConcMarkSweepGC");
    //    commandList.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005");
    commandList.add("-cp");
    //    //        commandList.add(this.directory + BaseAdapter.PATH_SEPARATOR +
    //    // BaseAdapter.CLASS_CONTAINER + BaseAdapter.PATH_SEPARATOR +
    //    // BaseAdapter.JACKSON_PATH + BaseAdapter.PATH_SEPARATOR + cp.toString());
    //    commandList.add(
    //        this.directory
    //            + PATH_SEPARATOR
    //            + CLASS_PATH
    //            + PATH_SEPARATOR
    //            //            + CLASS_CONTAINER
    //            //            + PATH_SEPARATOR
    //            + JACKSON_PATH
    //            + PATH_SEPARATOR
    //            + COMMONS_CLI);

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
        + programClassPath;
  }

  protected String getProgramName() {
    return programName;
  }

  protected Set<Set<String>> getConfigurations() {
    return configurations;
  }

  /*
    //  //  private static final String CONFIGCRUSHER = "./target/ConfigCrusher-0.1.0-SNAPSHOT.jar";
  //  //  private static final String CLASS_CONTAINER = "target/classes/";
  //  private static final String JACKSON_PATH =
  //      BaseAdapter.USER_HOME
  //          + "/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar"
  //          + BaseAdapter.PATH_SEPARATOR
  //          + BaseAdapter.USER_HOME
  //          + "/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar"
  //          + BaseAdapter.PATH_SEPARATOR
  //          + BaseAdapter.USER_HOME
  //          + "/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar";
  //  private static final String COMMONS_CLI =
  //      BaseAdapter.USER_HOME
  //          + "/.m2/repository/commons-cli/commons-cli/1.4/commons-cli-1.4.jar"
  //          + BaseAdapter.PATH_SEPARATOR
  //          + BaseAdapter.USER_HOME
  //          + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar";
  //  private String programName;
  //  //  // TODO figure out what prevayler's path is
  //  //  private static final String PREVAYLER_PATH =
  //  //      BaseAdapter.USER_HOME
  //  //          + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar"
  //  //          + BaseAdapter.USER_HOME
  //  //          + "/.m2/repository/log4j/log4j/1.2.15/log4j-1.2.15.jar"
  //  //          + BaseAdapter.USER_HOME
  //  //          + "/.m2/repository/com/thoughtworks/xstream/xstream/1.4.5/xstream-1.4.5.jar";
  //  //  private static final String KANZI_PATH =
  //  //      BaseAdapter.USER_HOME + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar";
  //  private String mainClass;
  //  private String directory;
  //  private List<String> options;
  //
  //  public BaseAdapter(String programName, String mainClass, String directory, List<String> options) {
  //    this.programName = programName;
  //    this.mainClass = mainClass;
  //    this.directory = directory;
  //    this.options = options;
  //  }

    public Set<String> configurationAsSet(String[] configuration) {
      Set<String> performanceConfiguration = new HashSet<>();

      for (int i = 0; i < configuration.length; i++) {
        if (configuration[i].equals("true")) {
          performanceConfiguration.add(this.options.get(i));
        }
      }

      return performanceConfiguration;
    }

    @Override
    public String[] configurationAsMainArguments(Set<String> configuration) {
      //    String[] sleepConfiguration = new String[this.options.size()];
      //
      //    for (int i = 0; i < sleepConfiguration.length; i++) {
      //      if (configuration.contains(this.options.get(i))) {
      //        sleepConfiguration[i] = "true";
      //      } else {
      //        sleepConfiguration[i] = "false";
      //      }
      //    }
      //
      //    return sleepConfiguration;
      throw new UnsupportedOperationException("Implement");
    }

    //  @Override
    //  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    //    //    this.execute(configuration, 0);
    //    throw new UnsupportedOperationException("Implement");
    //  }

    private String[] buildCommand(List<String> commandList) {
      //    String[] command = new String[commandList.size()];
      //    command = commandList.toArray(command);
      //    System.out.println(Arrays.toString(command));
      //
      //    return command;
      throw new UnsupportedOperationException("Implement");
    }


     */
}

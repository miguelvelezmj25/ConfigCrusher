package edu.cmu.cs.mvelezce.tool.execute.java.adapter;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.CompileInstrumenter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public abstract class BaseAdapter implements Adapter {

  public static final String USER_HOME = System.getProperty("user.home");
  public static final String PATH_SEPARATOR = System.getProperty("path.separator");

  private static final String CONFIGCRUSHER = "./target/classes";
  //  private static final String CONFIGCRUSHER = "./target/ConfigCrusher-0.1.0-SNAPSHOT.jar";
  //  private static final String CLASS_CONTAINER = "target/classes/";
  private static final String JACKSON_PATH =
      BaseAdapter.USER_HOME
          + "/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.8.9/jackson-core-2.8.9.jar"
          + BaseAdapter.PATH_SEPARATOR
          + BaseAdapter.USER_HOME
          + "/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.8.9/jackson-annotations-2.8.9.jar"
          + BaseAdapter.PATH_SEPARATOR
          + BaseAdapter.USER_HOME
          + "/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.8.9/jackson-databind-2.8.9.jar";
  private static final String COMMONS_CLI =
      BaseAdapter.USER_HOME
          + "/.m2/repository/commons-cli/commons-cli/1.4/commons-cli-1.4.jar"
          + BaseAdapter.PATH_SEPARATOR
          + BaseAdapter.USER_HOME
          + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar";
  //  // TODO figure out what prevayler's path is
  //  private static final String PREVAYLER_PATH =
  //      BaseAdapter.USER_HOME
  //          + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar"
  //          + BaseAdapter.USER_HOME
  //          + "/.m2/repository/log4j/log4j/1.2.15/log4j-1.2.15.jar"
  //          + BaseAdapter.USER_HOME
  //          + "/.m2/repository/com/thoughtworks/xstream/xstream/1.4.5/xstream-1.4.5.jar";
  //  private static final String KANZI_PATH =
  //      BaseAdapter.USER_HOME + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar";

  private String programName;
  private String mainClass;
  private String directory;
  private List<String> options;

  public BaseAdapter(String programName, String mainClass, String directory, List<String> options) {
    this.programName = programName;
    this.mainClass = mainClass;
    this.directory = directory;
    this.options = options;
  }

  @Override
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
    String[] sleepConfiguration = new String[this.options.size()];

    for (int i = 0; i < sleepConfiguration.length; i++) {
      if (configuration.contains(this.options.get(i))) {
        sleepConfiguration[i] = "true";
      }
      else {
        sleepConfiguration[i] = "false";
      }
    }

    return sleepConfiguration;
  }

  public String getProgramName() {
    return programName;
  }

  public String getMainClass() {
    return mainClass;
  }

  public String getDirectory() {
    return directory;
  }

  public List<String> getOptions() {
    return options;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.execute(configuration, 0);
  }

  @Override
  public void execute(String mainAdapter, String[] args) throws InterruptedException, IOException {
    //    String mvnLocalRepo = this.getMVNLocalRepoAsClassPath();
    List<String> commandList = this.buildCommandAsList(mainAdapter, args);
    String[] command = this.buildCommand(commandList);
    Process process = Runtime.getRuntime().exec(command);

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();

    //    if (!output.toString().isEmpty()) {
    //      throw new IOException();
    //    }
  }

  private String[] buildCommand(List<String> commandList) {
    String[] command = new String[commandList.size()];
    command = commandList.toArray(command);
    System.out.println(Arrays.toString(command));

    return command;
  }

  private List<String> buildCommandAsList(String mainAdapter, String[] args) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
    commandList.add("-Xms10G");
    commandList.add("-Xmx10G");
    commandList.add("-XX:+UseConcMarkSweepGC");
    commandList.add("-cp");
    //        commandList.add(this.directory + BaseAdapter.PATH_SEPARATOR + BaseAdapter.CLASS_CONTAINER + BaseAdapter.PATH_SEPARATOR +
    // BaseAdapter.JACKSON_PATH + BaseAdapter.PATH_SEPARATOR + cp.toString());
    commandList.add(
        this.directory
            + BaseAdapter.PATH_SEPARATOR
            + BaseAdapter.CONFIGCRUSHER
            + BaseAdapter.PATH_SEPARATOR
            //            + BaseAdapter.CLASS_CONTAINER
            //            + BaseAdapter.PATH_SEPARATOR
            + BaseAdapter.JACKSON_PATH
            + BaseAdapter.PATH_SEPARATOR
            + BaseAdapter.COMMONS_CLI);
    commandList.add(mainAdapter);
    commandList.add(this.programName);
    commandList.add(this.mainClass);
    commandList.addAll(Arrays.asList(args));

    return commandList;
  }

//  private String getMVNLocalRepoAsClassPath() {
//    Collection<File> m2Files = getM2Files();
//    StringBuilder m2FilesAsClassPath = new StringBuilder();
//
//    for (File jarFile : m2Files) {
//      m2FilesAsClassPath.append(jarFile);
//      m2FilesAsClassPath.append(BaseAdapter.PATH_SEPARATOR);
//    }
//
//    m2FilesAsClassPath.deleteCharAt(m2FilesAsClassPath.length() - 1);
//
//    return m2FilesAsClassPath.toString();
//  }
//
//  private Collection<File> getM2Files() {
//    File m2Dir = new File(CompileInstrumenter.M2_DIR);
//    return FileUtils.listFiles(m2Dir, new String[]{"jar"}, true);
//  }
}

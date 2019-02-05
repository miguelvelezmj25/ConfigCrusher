package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext.OrContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.return2Example.Return2ExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample.SimpleForExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample3.SimpleForExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample4.SimpleForExample4Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jboss.util.file.Files;

/**
 * Class to execute the programs that have been instrumented with code to perform subtrace
 * analysis.
 */
public class SubtracesAnalysisExecutor extends BaseDynamicAnalysis<Map<Set<String>, List<String>>> {

  private static final Map<Set<String>, List<String>> CONFIGS_TO_TRACES = new HashMap<>();

  public SubtracesAnalysisExecutor(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public SubtracesAnalysisExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  @Override
  public Map<Set<String>, List<String>> analyze() throws IOException, InterruptedException {
    Set<Set<String>> configs = Helper.getConfigurations(this.getOptions());

    for (Set<String> config : configs) {
      this.runProgram(config);
      this.processResults(config);
    }

    Files.delete(SubtracesLogger.RESULTS_FILE);

    return CONFIGS_TO_TRACES;
  }

  private void processResults(Set<String> config) throws IOException {
    List<String> trace = this.getTrace();
    CONFIGS_TO_TRACES.put(config, trace);
  }

  private List<String> getTrace() throws IOException {
    try (FileInputStream fis = new FileInputStream(SubtracesLogger.RESULTS_FILE);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      return (List<String>) ois.readObject();
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException("There was an error when processing the results", cnfe);
    }
  }

  @Override
  public Map<Set<String>, List<String>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<ConfigToTraceInfo> configToTraceInfoList = mapper
        .readValue(file, new TypeReference<List<ConfigToTraceInfo>>() {
        });

    for (ConfigToTraceInfo configToTraceInfo : configToTraceInfoList) {
      CONFIGS_TO_TRACES.put(configToTraceInfo.getConfig(), configToTraceInfo.getTrace());
    }

    return CONFIGS_TO_TRACES;
  }

  @Override
  public void writeToFile(Map<Set<String>, List<String>> analysisResults) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<ConfigToTraceInfo> infos = new ArrayList<>();

    for (Map.Entry<Set<String>, List<String>> entry : analysisResults.entrySet()) {
      ConfigToTraceInfo configToTraceInfo = new ConfigToTraceInfo(entry.getKey(), entry.getValue());
      infos.add(configToTraceInfo);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, infos);
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/analysis/spec/traces/java/programs/" + this.getProgramName();
  }

  private void runProgram(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();
    commandList.add("java");
//    commandList.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005");
    commandList.add("-cp");

    String programName = this.getProgramName();
    String ccClasspath = "./target/classes";
//        + BaseAdapter.PATH_SEPARATOR
//        + APACHE_COMMONS_PATH;
    Adapter adapter;

    switch (programName) {
      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + DynamicRunningExampleAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new DynamicRunningExampleAdapter();
        break;
      case SimpleExample1Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SimpleExample1Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new SimpleExample1Adapter();
        break;
      case Example1Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + Example1Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new Example1Adapter();
        break;
      case PhosphorExample2Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + PhosphorExample2Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new PhosphorExample2Adapter();
        break;
      case OrContextAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + OrContextAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new OrContextAdapter();
        break;
      case MultiFacetsAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + MultiFacetsAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new MultiFacetsAdapter();
        break;
      case SimpleForExampleAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SimpleForExampleAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new SimpleForExampleAdapter();
        break;
      case SimpleForExample2Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SimpleForExample2Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new SimpleForExample2Adapter();
        break;
      case SimpleForExample3Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SimpleForExample3Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new SimpleForExample3Adapter();
        break;
      case SimpleForExample4Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SimpleForExample4Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new SimpleForExample4Adapter();
        break;
      case Return2ExampleAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + Return2ExampleAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new Return2ExampleAdapter();
        break;
      case SubtracesAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SubtracesAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new SubtracesAdapter();
        break;
      case Subtraces2Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + Subtraces2Adapter.INSTRUMENTED_CLASS_PATH);
        adapter = new Subtraces2Adapter();
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + programName);
//        if (this.mainClass != null) {
//          commandList.add(ccClasspath
//              + BaseAdapter.PATH_SEPARATOR
//              + AllDynamicAdapter.INSTRUMENTED_CLASS_PATH);
//          adapter = new AllDynamicAdapter(programName, this.mainClass);
//        }
//        else {
//          throw new RuntimeException("Could not find a phosphor script to run " + programName);
//        }
    }

    commandList.add(adapter.getMainClass());
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }
}

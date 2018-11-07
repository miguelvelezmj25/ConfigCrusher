package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jboss.util.file.Files;

public class BranchCoverageAnalysis extends BaseDynamicAnalysis<DecisionInfo> {

  private static final String APACHE_COMMONS_PATH =
      BaseAdapter.USER_HOME
          + "/.m2/repository/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar";

  private final Map<String, DecisionInfo> sinksToDecisionInfos = new HashMap<>();
  private final Set<Set<String>> executedConfigs = new HashSet<>();

  BranchCoverageAnalysis(String programName) {
    this(programName, new HashSet<>());
  }

  BranchCoverageAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  @Override
  public Map<JavaRegion, DecisionInfo> analyze()
      throws IOException, InterruptedException {
    Set<Set<String>> configs = Helper.getConfigurations(this.getOptions());

    for (Set<String> config : configs) {
      this.executedConfigs.add(config);
      this.runProgram(config);

      try {
        this.processResults(config);
      }
      catch (ClassNotFoundException cnfe) {
        throw new RuntimeException("There was an error when processing the results", cnfe);
      }
    }

    this.padTablesWithNonReachedConfigs();
    Files.delete(BranchCoverageLogger.RESULTS_FILE);

    return this.getRegionsToDecisionTables();
  }

  private Map<JavaRegion, DecisionInfo> getRegionsToDecisionTables() {
    Map<JavaRegion, DecisionInfo> regionsToDecisionTables = new HashMap<>();

    for (Map.Entry<String, DecisionInfo> entry : this.sinksToDecisionInfos.entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      regionsToDecisionTables.put(region, entry.getValue());
    }

    return regionsToDecisionTables;
  }

  private void padTablesWithNonReachedConfigs() {
    for (DecisionInfo decisionInfo : this.sinksToDecisionInfos.values()) {
      DecisionBranchCountTable decisionBranchTable = decisionInfo.getDecisionBranchTable();
      Map<Map<String, Boolean>, ThenElseCounts> table = decisionBranchTable.getTable();

      for (Set<String> config : this.executedConfigs) {
        Map<String, Boolean> configWithValues = Constraint
            .toConfigWithValues(config, this.getOptions());

        if (!table.containsKey(configWithValues)) {
          decisionBranchTable.addEntry(config, new ThenElseCounts());
        }
      }
    }
  }

  @Override
  public Map<JavaRegion, DecisionInfo> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo> results = mapper.readValue(file, new TypeReference<List<RegionToInfo>>() {
    });

    Map<JavaRegion, DecisionInfo> regionsToDecisionInfos = new HashMap<>();

    for (RegionToInfo result : results) {
      Map<String, Collection> info = (Map<String, Collection>) result.getInfo();
      Map<String, Collection> decisionBranchTable = (Map<String, Collection>) info.get("decisionBranchTable");
      Set<String> options = new HashSet<>(decisionBranchTable.get("options"));

      DecisionInfo decisionInfo = new DecisionInfo(options);
      DecisionBranchCountTable decisionTable = decisionInfo.getDecisionBranchTable();
      Map<String, Map<String, Integer>> table = (Map<String, Map<String, Integer>>) decisionBranchTable.get("table");
      this.buildDecisionTable(table, decisionTable);

      Set<Set<String>> context = this.getContext((List<List<String>>) info.get("context"));
      decisionInfo.getContext().addAll(context);

      regionsToDecisionInfos.put(result.getRegion(), decisionInfo);
    }

    return regionsToDecisionInfos;
  }

  private Set<Set<String>> getContext(List<List<String>> configs) {
    Set<Set<String>> context = new HashSet<>();

    for (List<String> config : configs) {
      context.add(new HashSet<>(config));
    }

    return context;
  }

  private void buildDecisionTable(Map<String, Map<String, Integer>> table, DecisionBranchCountTable decisionTable) {
    for (Map.Entry<String, Map<String, Integer>> entry : table.entrySet()) {
      Set<String> config = this.parseConfig(entry.getKey());
      Map<String, Integer> pair = entry.getValue();

      ThenElseCounts thenElseCounts = new ThenElseCounts();
      thenElseCounts.setThenCount(pair.get("thenCount"));
      thenElseCounts.setElseCount(pair.get("elseCount"));
      decisionTable.addEntry(config, thenElseCounts);
    }
  }

  private Set<String> parseConfig(String key) {
    Set<String> config = new HashSet<>();

    String[] entries = key.split(",");

    for (String entry : entries) {
      entry = entry.replace("{", "");
      entry = entry.replace("}", "");
      entry = entry.trim();

      if (!entry.contains("=")) {
        throw new RuntimeException(
            "The entry " + entry + " does not have the expected Option = Value format");
      }

      int equalsIndex = entry.indexOf("=");
      boolean value = Boolean.valueOf(entry.substring(equalsIndex + 1));

      if (value) {
        String option = entry.substring(0, equalsIndex);
        config.add(option);
      }
    }

    return config;
  }

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/gt";
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
    commandList.add("-cp");

    String programName = this.getProgramName();
    String ccClasspath = "./target/classes"
        + BaseAdapter.PATH_SEPARATOR
        + APACHE_COMMONS_PATH;
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
        adapter = new Example1Adapter();
        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    commandList.add(adapter.getMainClass());
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }

  private void processResults(Set<String> config) throws IOException, ClassNotFoundException {
    Map<String, ThenElseCounts> sinksToBranchCounts = this.getSinksToBranchCounts();
    this.addSinks(sinksToBranchCounts.keySet());

    for (Entry<String, ThenElseCounts> entry : sinksToBranchCounts.entrySet()) {
      String sink = entry.getKey();
      DecisionInfo decisionInfo = this.sinksToDecisionInfos.get(sink);
      decisionInfo.getContext().add(config);

      DecisionBranchCountTable decisionBranchCountTable = decisionInfo.getDecisionBranchTable();
      decisionBranchCountTable.addEntry(config, entry.getValue());
    }
  }

  private void addSinks(Set<String> executedSinks) {
    for (String sink : executedSinks) {
      if (!this.sinksToDecisionInfos.containsKey(sink)) {
        this.sinksToDecisionInfos.put(sink, new DecisionInfo(this.getOptions()));
      }
    }
  }

  private Map<String, ThenElseCounts> getSinksToBranchCounts()
      throws IOException, ClassNotFoundException {
    Map<String, ThenElseCounts> sinksToBranchCounts;

    try (FileInputStream fis = new FileInputStream(BranchCoverageLogger.RESULTS_FILE);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      sinksToBranchCounts = (Map<String, ThenElseCounts>) ois.readObject();
    }

    return sinksToBranchCounts;
  }
}

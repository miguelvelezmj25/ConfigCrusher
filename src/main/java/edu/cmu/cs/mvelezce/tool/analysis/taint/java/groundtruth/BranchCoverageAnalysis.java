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
import org.apache.commons.lang3.tuple.MutablePair;
import org.jboss.util.file.Files;

public class BranchCoverageAnalysis extends BaseDynamicAnalysis<DecisionBranchCountTable> {

  private static final String APACHE_COMMONS_PATH =
      BaseAdapter.USER_HOME
          + "/.m2/repository/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar";

  private final Map<String, DecisionBranchCountTable> sinksToDecisionTables = new HashMap<>();
  private final Set<Set<String>> executedConfigs = new HashSet<>();

  BranchCoverageAnalysis(String programName) {
    this(programName, new HashSet<>());
  }

  BranchCoverageAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  @Override
  public Map<JavaRegion, DecisionBranchCountTable> analyze()
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

  private Map<JavaRegion, DecisionBranchCountTable> getRegionsToDecisionTables() {
    Map<JavaRegion, DecisionBranchCountTable> regionsToDecisionTables = new HashMap<>();

    for (Map.Entry<String, DecisionBranchCountTable> entry : this.sinksToDecisionTables
        .entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      regionsToDecisionTables.put(region, entry.getValue());
    }

    return regionsToDecisionTables;
  }

  private void padTablesWithNonReachedConfigs() {
    for (DecisionBranchCountTable decisionBranchTable : this.sinksToDecisionTables.values()) {
      Map<Map<String, Boolean>, MutablePair<Integer, Integer>> table = decisionBranchTable
          .getTable();

      for (Set<String> config : this.executedConfigs) {
        Map<String, Boolean> configWithValues = Constraint
            .toConfigWithValues(config, this.getOptions());

        if (!table.containsKey(configWithValues)) {
          decisionBranchTable.addEntry(config, MutablePair.of(0, 0));
        }
      }
    }
  }

  @Override
  public Map<JavaRegion, DecisionBranchCountTable> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo> results = mapper.readValue(file, new TypeReference<List<RegionToInfo>>() {
    });

    Map<JavaRegion, DecisionBranchCountTable> regionsToDecisionBranchCountTables = new HashMap<>();

    for (RegionToInfo result : results) {
      Map<String, Collection> info = (Map<String, Collection>) result.getInfo();
      List<String> options = new ArrayList<>(info.get("options"));
      DecisionBranchCountTable decisionTable = new DecisionBranchCountTable(new HashSet<>(options));

      Map<String, Map<Integer, Integer>> table = (Map<String, Map<Integer, Integer>>) info
          .get("table");
      this.buildDecisionTable(table, decisionTable);

      regionsToDecisionBranchCountTables.put(result.getRegion(), decisionTable);
    }

    return regionsToDecisionBranchCountTables;

    //    FileOutputStream fos = new FileOutputStream("table.ser");
//    ObjectOutputStream oos = new ObjectOutputStream(fos);
//    oos.writeObject(sinksToTruthTable);
//    oos.close();
//    fos.close();

//    FileInputStream fis = new FileInputStream("table.ser");
//    ObjectInputStream ois = new ObjectInputStream(fis);
//    Map<String, Map<Set<String>, Boolean>> res = (Map<String, Map<Set<String>, Boolean>>) ois.readObject();
//    ois.close();
//    fis.close();

//    for (Map.Entry<String, DecisionBranchCountTable> entry : sinksToTruthTable.entrySet()) {
//      System.out.println(entry.getKey());
//      System.out.println(entry.getValue());
//
//      System.out.println();
//    }
  }

  private void buildDecisionTable(Map<String, Map<Integer, Integer>> table,
      DecisionBranchCountTable decisionTable) {
    for (Map.Entry<String, Map<Integer, Integer>> entry : table.entrySet()) {
      Set<String> config = this.parseConfig(entry.getKey());
      Map<Integer, Integer> pair = entry.getValue();

      if (pair.size() != 1) {
        throw new RuntimeException("There should be only 1 entry per configuration");
      }

      Entry<Integer, Integer> savedPair = pair.entrySet().iterator().next();
      MutablePair<Integer, Integer> thenElseCounts = MutablePair
          .of(savedPair.getKey(), savedPair.getValue());
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
      // TODO add class path to adapter and use path separator
      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + ":../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes");
        adapter = new DynamicRunningExampleAdapter();
        break;
      case SimpleExample1Adapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + SimpleExample1Adapter.INSTRUMENTED_CLASS_PATH
            + BaseAdapter.PATH_SEPARATOR
            + SimpleExample1Adapter.ORIGINAL_CLASS_PATH);
        adapter = new SimpleExample1Adapter();
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
    Map<String, MutablePair<Integer, Integer>> sinksToBranchCounts = this.getSinksToBranchCounts();
    this.addSinks(sinksToBranchCounts.keySet());

    for (Map.Entry<String, MutablePair<Integer, Integer>> entry : sinksToBranchCounts.entrySet()) {
      String sink = entry.getKey();
      DecisionBranchCountTable decisionBranchCountTable = this.sinksToDecisionTables.get(sink);
      decisionBranchCountTable.addEntry(config, entry.getValue());
      this.sinksToDecisionTables.put(sink, decisionBranchCountTable);
    }
  }

  private void addSinks(Set<String> executedSinks) {
    for (String sink : executedSinks) {
      if (!this.sinksToDecisionTables.containsKey(sink)) {
        this.sinksToDecisionTables.put(sink, new DecisionBranchCountTable(this.getOptions()));
      }
    }
  }

  private Map<String, MutablePair<Integer, Integer>> getSinksToBranchCounts()
      throws IOException, ClassNotFoundException {
    Map<String, MutablePair<Integer, Integer>> sinksToBranchCounts;

    try (FileInputStream fis = new FileInputStream(BranchCoverageLogger.RESULTS_FILE);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      sinksToBranchCounts = (Map<String, MutablePair<Integer, Integer>>) ois.readObject();
    }

    return sinksToBranchCounts;
  }
}

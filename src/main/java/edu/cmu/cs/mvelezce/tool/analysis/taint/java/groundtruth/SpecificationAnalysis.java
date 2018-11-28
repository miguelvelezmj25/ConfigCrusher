package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic.AllDynamicAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gtOverapprox.GTOverapproxAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
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
import java.util.Set;
import org.jboss.util.file.Files;

public class SpecificationAnalysis extends BaseDynamicAnalysis<DecisionInfo> {

  private static final String APACHE_COMMONS_PATH =
      BaseAdapter.USER_HOME
          + "/.m2/repository/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar";

  private final Map<String, DecisionInfo> sinksToDecisionInfos = new HashMap<>();

  private String mainClass;

  public SpecificationAnalysis(String programName) {
    this(programName, new HashSet<>());
  }

  SpecificationAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  SpecificationAnalysis(String programName, String mainClass, Set<String> options) {
    this(programName, options);

    this.mainClass = mainClass;
  }

  @Override
  public Map<JavaRegion, DecisionInfo> analyze()
      throws IOException, InterruptedException {
    Set<Set<String>> configs = Helper.getConfigurations(this.getOptions());

    for (Set<String> config : configs) {
      this.runProgram(config);

      try {
        this.processResults(config);
      }
      catch (ClassNotFoundException cnfe) {
        throw new RuntimeException("There was an error when processing the results", cnfe);
      }
    }

    Files.delete(SpecificationLogger.RESULTS_FILE);

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

  @Override
  public Map<JavaRegion, DecisionInfo> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo> results = mapper.readValue(file, new TypeReference<List<RegionToInfo>>() {
    });

    Map<JavaRegion, DecisionInfo> regionsToDecisionInfos = new HashMap<>();

    for (RegionToInfo result : results) {
      DecisionInfo decisionInfo = new DecisionInfo();
      Map<String, Collection> info = (Map<String, Collection>) result.getInfo();

      this.addContextInfo(decisionInfo, info);
      this.addTableInfo(decisionInfo, info);

      regionsToDecisionInfos.put(result.getRegion(), decisionInfo);
    }

    return regionsToDecisionInfos;
  }

  private void addTableInfo(DecisionInfo decisionInfo, Map<String, Collection> info) {
    Map<String, Map<String, Collection>> callingContextsToCountTables = (Map<String, Map<String, Collection>>) info
        .get("callingContextsToDecisionBranchTables");
    Map<List<String>, DecisionBranchCountTable> callingContextsToTables = decisionInfo
        .getCallingContextsToDecisionBranchTables();

    for (Map.Entry<String, Map<String, Collection>> entry : callingContextsToCountTables
        .entrySet()) {
      List<String> callingContext = this.getCallingContext(entry.getKey());
      DecisionBranchCountTable decisionTable = this.getDecisionTable(entry.getValue());
      callingContextsToTables.put(callingContext, decisionTable);
    }
  }

  private DecisionBranchCountTable getDecisionTable(Map<String, Collection> stringsToEntries) {
    Set<String> options = new HashSet<>(stringsToEntries.get("options"));
    DecisionBranchCountTable decisionCountTable = new DecisionBranchCountTable(options);
    this.addDecisionTableEntries(decisionCountTable, stringsToEntries);

    return decisionCountTable;
  }

  private void addDecisionTableEntries(DecisionBranchCountTable decisionCountTable,
      Map<String, Collection> value) {
    Map<String, Map<String, Integer>> table = (Map<String, Map<String, Integer>>) value
        .get("table");

    for (Map.Entry<String, Map<String, Integer>> entry : table.entrySet()) {
      Set<String> config = this.parseConfig(entry.getKey());
      Map<String, Integer> pair = entry.getValue();

      ThenElseCounts thenElseCounts = new ThenElseCounts();
      thenElseCounts.setThenCount(pair.get("thenCount"));
      thenElseCounts.setElseCount(pair.get("elseCount"));
      decisionCountTable.addEntry(config, thenElseCounts);
    }
  }

  private void addContextInfo(DecisionInfo decisionInfo, Map<String, Collection> info) {
    Map<String, Map> stringCallingContextsToStringCallingCtxs = (Map<String, Map>) info
        .get("callingContextsToVariabilityCtxs");
    Map<List<String>, VariabilityCtx> callingContextsToVariabilityCtxs = decisionInfo
        .getCallingContextsToVariabilityCtxs();

    for (Map.Entry<String, Map> entry : stringCallingContextsToStringCallingCtxs.entrySet()) {
      List<String> callingContext = this.getCallingContext(entry.getKey());
      Map<String, List<List<String>>> contexts = entry.getValue();
      VariabilityCtx variabilityCtx = this.getContext(contexts.get("ctx"));
      callingContextsToVariabilityCtxs.put(callingContext, variabilityCtx);
    }
  }

  private VariabilityCtx getContext(List<List<String>> configs) {
    VariabilityCtx variabilityCtx = new VariabilityCtx();

    for (List<String> config : configs) {
      variabilityCtx.addConfig(new HashSet<>(config));
    }

    return variabilityCtx;
  }

  private List<String> getCallingContext(String callingContext) {
    callingContext = callingContext.replace("[", "");
    callingContext = callingContext.replace("]", "");
    String[] elements = callingContext.split(",");

    List<String> list = new ArrayList<>();

    for (String element : elements) {
      element = element.trim();

      if (!element.isEmpty()) {
        list.add(element);
      }
    }

    String[] x = new String[list.size()];
    x = list.toArray(x);

    return Arrays.asList(x);
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
//    commandList.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005");
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
        adapter = new PhosphorExample2Adapter();
        break;
      case GTOverapproxAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + GTOverapproxAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new GTOverapproxAdapter();
        break;
      case MultiFacetsAdapter.PROGRAM_NAME:
        commandList.add(ccClasspath
            + BaseAdapter.PATH_SEPARATOR
            + MultiFacetsAdapter.INSTRUMENTED_CLASS_PATH);
        adapter = new MultiFacetsAdapter();
        break;
      default:
        if (this.mainClass != null) {
          commandList.add(ccClasspath
              + BaseAdapter.PATH_SEPARATOR
              + AllDynamicAdapter.INSTRUMENTED_CLASS_PATH);
          adapter = new AllDynamicAdapter(programName, this.mainClass);
        }
        else {
          throw new RuntimeException("Could not find a phosphor script to run " + programName);
        }
    }

    commandList.add(adapter.getMainClass());
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }

  private void processResults(Set<String> config) throws IOException, ClassNotFoundException {
    Map<CallingContextDecision, ThenElseCounts> callSiteDecisionsToBranchCounts = this
        .getCallSiteDecisionsToBranchCounts();
    this.addSinks(callSiteDecisionsToBranchCounts.keySet());

    for (Map.Entry<CallingContextDecision, ThenElseCounts> entry : callSiteDecisionsToBranchCounts
        .entrySet()) {
      CallingContextDecision callingContextDecision = entry.getKey();
      String sink = callingContextDecision.getDecision();
      DecisionInfo decisionInfo = this.sinksToDecisionInfos.get(sink);

      List<String> callingContext = callingContextDecision.getCallingContext();
      this.updateContext(decisionInfo, callingContext, config);
      this.updateTable(decisionInfo, callingContext, config, entry.getValue());
    }

  }

  private void updateTable(DecisionInfo decisionInfo, List<String> callingContext,
      Set<String> config, ThenElseCounts thenElseCounts) {
    this.addCountTable(decisionInfo, callingContext);
    DecisionBranchCountTable table = decisionInfo.getCallingContextsToDecisionBranchTables()
        .get(callingContext);
    table.addEntry(config, thenElseCounts);
  }

  private void addCountTable(DecisionInfo decisionInfo, List<String> callingContext) {
    Map<List<String>, DecisionBranchCountTable> callingContextsToDecisionTables = decisionInfo
        .getCallingContextsToDecisionBranchTables();
    callingContextsToDecisionTables
        .putIfAbsent(callingContext, new DecisionBranchCountTable(this.getOptions()));
  }

  private void updateContext(DecisionInfo decisionInfo, List<String> callingContext,
      Set<String> config) {
    this.addContext(decisionInfo, callingContext);
    VariabilityCtx callSiteVariabilityCtx = decisionInfo.getCallingContextsToVariabilityCtxs().get(callingContext);
    callSiteVariabilityCtx.addConfig(config);
  }

  private void addContext(DecisionInfo decisionInfo, List<String> callingContext) {
    Map<List<String>, VariabilityCtx> callingContextsToContexts = decisionInfo
        .getCallingContextsToVariabilityCtxs();
    callingContextsToContexts.putIfAbsent(callingContext, new VariabilityCtx());
  }

  private void addSinks(Set<CallingContextDecision> callingContextDecisions) {
    for (CallingContextDecision callingContextDecision : callingContextDecisions) {
      String sink = callingContextDecision.getDecision();
      this.sinksToDecisionInfos.putIfAbsent(sink, new DecisionInfo());
    }
  }

  private Map<CallingContextDecision, ThenElseCounts> getCallSiteDecisionsToBranchCounts()
      throws IOException, ClassNotFoundException {
    Map<CallingContextDecision, ThenElseCounts> callSiteSinksToBranchCounts;

    try (FileInputStream fis = new FileInputStream(SpecificationLogger.RESULTS_FILE);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
      callSiteSinksToBranchCounts = (Map<CallingContextDecision, ThenElseCounts>) ois.readObject();
    }

    return callSiteSinksToBranchCounts;
  }
}

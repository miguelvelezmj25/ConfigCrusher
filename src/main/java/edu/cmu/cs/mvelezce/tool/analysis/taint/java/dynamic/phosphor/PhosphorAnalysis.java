package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.ifOr2.IfOr2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit.ImplicitAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit2.Implicit2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext.OrContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext2.OrContext2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext3.OrContext3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext6.OrContext6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample8.PhosphorExample8Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample5.SimpleForExample5Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sound.SoundAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces6.Subtraces6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces7.Subtraces7Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext1.VariabilityContext1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext2.VariabilityContext2Adapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class PhosphorAnalysis extends BaseDynamicRegionAnalysis<SinkData> {

  private static final String PHOSPHOR_SCRIPTS_DIR = "../phosphor/Phosphor/scripts/run-instrumented/implicit-optimized";

  private final PhosphorExecutionAnalysis phosphorExecutionAnalysis;
  private final PhosphorConfigConstraintTracker phosphorConfigConstraintTracker;
  private final ConfigConstraintAnalyzer configConstraintAnalyzer;

  public PhosphorAnalysis(String programName) {
    this(programName, new HashSet<>(), new HashSet<>());
  }

  PhosphorAnalysis(String programName, Set<String> options, Set<String> initialConfig) {
    super(programName, options, initialConfig);

    this.phosphorExecutionAnalysis = new PhosphorExecutionAnalysis(programName);
    this.phosphorConfigConstraintTracker = new PhosphorConfigConstraintTracker();
    this.configConstraintAnalyzer = new ConfigConstraintAnalyzer(options);
  }

  @Override
  public Map<JavaRegion, SinkData> analyze() throws IOException, InterruptedException {
    this.runDynamicAnalysis();

    Map<JavaRegion, SinkData> regionsToData = new HashMap<>();

    for (Map.Entry<String, Map<ExecVarCtx, ExecConfigConstraints>> entry : this.phosphorConfigConstraintTracker
        .getSinksToConfigConstraints().entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      Map<ExecVarCtx, ExecConfigConstraints> data = entry.getValue();
      SinkData sinkData = new SinkData(data);
      regionsToData.put(region, sinkData);
    }

    return regionsToData;
  }

  @Override
  public String outputDir() {
    return BaseDynamicRegionAnalysis.DIRECTORY + "/" + this.getProgramName() + "/cc";
  }

  void runDynamicAnalysis() throws IOException, InterruptedException {
    Set<ConfigConstraint> configConstraintsToSatisfy = new HashSet<>();
    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();

    Set<ConfigConstraint> exploredConfigConstraints = new HashSet<>();
    Set<String> configToExecute = this.getInitialConfig();

    while (configToExecute != null) {
      ConfigConstraint configConstraint = ConfigConstraint
          .fromConfig(configToExecute, this.getOptions());
      exploredConfigConstraints.add(configConstraint);
      Set<ConfigConstraint> satisfiedConfigConstraintsByConfig = this.configConstraintAnalyzer
          .getSatisfiedConfigConstraintsByConfig(configConstraint);
      satisfiedConfigConstraints.addAll(satisfiedConfigConstraintsByConfig);

      this.runPhosphorAnalysis(configToExecute);

      // TODO derive constraints
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints = this.phosphorExecutionAnalysis
          .getPhosphorResults();
      this.phosphorConfigConstraintTracker
          .deriveConstraints(configToExecute, sinksToAnalysisTaints);

      Set<ConfigConstraint> analysisConfigConstraints = this.phosphorConfigConstraintTracker
          .getConfigConstraints();
      configConstraintsToSatisfy.addAll(analysisConfigConstraints);
      configConstraintsToSatisfy.removeAll(satisfiedConfigConstraints);

      // TODO pick next config if we still need to sample something
      Set<Set<String>> configsToRun = this.configConstraintAnalyzer
          .getConfigsThatSatisfyConfigConstraints(configConstraintsToSatisfy,
              exploredConfigConstraints);

      configToExecute = this.getConfigToExecute(configsToRun);
    }
  }

  @Nullable
  private Set<String> getConfigToExecute(Set<Set<String>> configsToRun) {
    if (configsToRun.isEmpty()) {
      return null;
    }

    int maxSize = this.getMaxSizeOfConfigs(configsToRun);

    return this.getConfigOfSize(maxSize, configsToRun);
  }

  private Set<String> getConfigOfSize(int size, Set<Set<String>> configsToRun) {
    for (Set<String> config : configsToRun) {
      if (config.size() == size) {
        return config;
      }
    }

    throw new RuntimeException("Could not find a config with size " + size);
  }

  private int getMaxSizeOfConfigs(Set<Set<String>> configsToRun) {
    int maxSize = -1;

    for (Set<String> config : configsToRun) {
      maxSize = Math.max(maxSize, config.size());
    }

    return maxSize;
  }

  @Override
  public Map<JavaRegion, SinkData> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("Implement");
//    ObjectMapper mapper = new ObjectMapper();
//    List<RegionToInfo> results = mapper
//        .readValue(file, new TypeReference<List<RegionToInfo>>() {
//        });
//
//    Map<JavaRegion, SinkData> regionsToConstraints = new HashMap<>();
//
//    for (RegionToInfo<SinkData> result : results) {
//      Map<String, Map> info = (Map<String, Map>) result.getInfo();
//      Map<String, Map> sinkDataEntries = info.get("data");
//
//      SinkData sinkData = new SinkData();
//
//      for (Map.Entry<String, Map> entry : sinkDataEntries.entrySet()) {
//        String execVarCtxStr = entry.getKey();
//        ExecVarCtx execVarCtx = this.getExecVarCtx(execVarCtxStr);
//        Map<String, List> execTaints = entry.getValue();
//        ExecTaints executionTaints = this.getExecTaints(execTaints);
//
//        sinkData.putIfAbsent(execVarCtx, executionTaints);
//      }
//
//      regionsToConstraints.put(result.getRegion(), sinkData);
//    }
//
//    return regionsToConstraints;
  }

  void runPhosphorAnalysis(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));

    System.out.println("Running program");
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

  // TODO the access level was changed to hardcode some logic to execute all dynamic examples
  List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();

    String programName = this.getProgramName();
    Adapter adapter;

    switch (programName) {
      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new DynamicRunningExampleAdapter();
        break;
      case PhosphorExample2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample2Adapter();
        break;
      case PhosphorExample8Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample8Adapter();
        break;
      case PhosphorExample3Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample3Adapter();
        break;
      case SimpleExample1Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleExample1Adapter();
        break;
      case Example1Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Example1Adapter();
        break;
      case MultiFacetsAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new MultiFacetsAdapter();
        break;
      case SimpleForExample2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleForExample2Adapter();
        break;
      case SimpleForExample5Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SimpleForExample5Adapter();
        break;
      case OrContextAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContextAdapter();
        break;
      case OrContext2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContext2Adapter();
        break;
      case OrContext3Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContext3Adapter();
        break;
      case OrContext6Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new OrContext6Adapter();
        break;
      case IfOr2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new IfOr2Adapter();
        break;
      case VariabilityContext1Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new VariabilityContext1Adapter();
        break;
      case VariabilityContext2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new VariabilityContext2Adapter();
        break;
      case SubtracesAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SubtracesAdapter();
        break;
      case Subtraces2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Subtraces2Adapter();
        break;
      case Subtraces6Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Subtraces6Adapter();
        break;
      case Subtraces7Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Subtraces7Adapter();
        break;
      case ImplicitAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new ImplicitAdapter();
        break;
      case Implicit2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new Implicit2Adapter();
        break;
      case TrivialAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new TrivialAdapter();
        break;
      case SoundAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new SoundAdapter();
        break;
      case PrevaylerAdapter.PROGRAM_NAME:
        commandList.add("./prevayler.sh");
        commandList.add(PrevaylerAdapter.PROGRAM_NAME);
        adapter = new PrevaylerAdapter();
        break;
      case MeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        commandList.add("./measureDiskOrderedScan.sh");
        adapter = new MeasureDiskOrderedScanAdapter();
        ((MeasureDiskOrderedScanAdapter) adapter).preProcess();
        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    // TODO change the following method to take a Config object
    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.add(adapter.getMainClass());
    commandList.addAll(configList);

    return commandList;
  }

  private ExecTaints getExecTaints(Map<String, List> map) {
    List<List<String>> taintsLists = map.get("taints");
    List<Set<String>> allTaints = new ArrayList<>();

    for (List<String> taintLIst : taintsLists) {
      Set<String> taints = new HashSet<>(taintLIst);
      allTaints.add(taints);
    }

    return new ExecTaints(allTaints);
  }

  private ExecVarCtx getExecVarCtx(String execVarCtxStr) {
    execVarCtxStr = execVarCtxStr.replace(ExecVarCtx.LLBRACKET, "");
    execVarCtxStr = execVarCtxStr.replace(ExecVarCtx.RRBRACKET, "");
    String[] entries = execVarCtxStr.split(Pattern.quote("^"));

    ExecVarCtx execVarCtx = new ExecVarCtx();

    if (!(entries.length == 1 && "true".equals(entries[0]))) {
      for (String entry : entries) {
        String notStr = "!";
        entry = entry.trim();
        boolean value = !entry.contains(notStr);
        entry = entry.replace(notStr, "");
        execVarCtx.addEntry(entry, value);
      }
    }

    return execVarCtx;
  }

  static void printProgramConstraints(Map<JavaRegion, SinkData> regionsToSinkData) {
    throw new UnsupportedOperationException("Implement");
//    for (Map.Entry<JavaRegion, SinkData> regionToSinkData : regionsToSinkData.entrySet()) {
//      JavaRegion region = regionToSinkData.getKey();
//      SinkData sinkData = regionToSinkData.getValue();
//
//      printConfigConstraintsForRegion(region, sinkData);
//
//      System.out.println();
//    }
  }

  private static void printConfigConstraintsForRegion(JavaRegion region, SinkData sinkData) {
    throw new UnsupportedOperationException("Implement");
//    for (Map.Entry<ExecVarCtx, Set<ExecTaints>> data : sinkData.getData().entrySet()) {
//      ExecVarCtx execVarCtx = data.getKey();
//      Set<Set<ConfigConstraint>> regionConstraints = getConfigConstraintsForExecVarCtx(execVarCtx,
//          data);
//
//      for (Set<ConfigConstraint> cs : regionConstraints) {
//        System.out
//            .println(region.getRegionMethod() + ":" + region.getStartRegionIndex() + " -> " + cs);
//      }
//    }
  }

  // TODO add check to be sure that we are not sampling a constraint that we already sample
//    Set<Constraint> exploredConstraints = new HashSet<>();
//    Set<Constraint> constraintsToExplore = new HashSet<>();
//    // CE := to_constraint(c)
//    Set<String> options = this.getOptions();
//    Map<String, Boolean> initialConfigAsConfigWithValues = Constraint
//        .toConfigWithValues(this.getInitialConfig(), options);
//    constraintsToExplore.add(new Constraint(initialConfigAsConfigWithValues));
//
//    int count = 0;
//
//    while (!constraintsToExplore.isEmpty()) {
//      // CTE := get_next_constraint(CE,O)
//      Constraint currentConstraint = PhosphorAnalysis.getNextConstraint(constraintsToExplore);
//      // c:= to_config(CTE)
//      Set<String> config = currentConstraint.getConstraintAsPartialConfig();
//      Map<String, Boolean> configWithValues = Constraint.toConfigWithValues(config, options);
//      currentConstraint = new Constraint(configWithValues);
//
//      // CE.removeAll(CTE)
//      PhosphorAnalysis.removeAllSubConstraints(constraintsToExplore, currentConstraint);
//      // EC.addAll(CTE)
//      exploredConstraints.add(currentConstraint);
//
//      // ST := run_taint_analysis(P’, c)
//      this.runPhosphorAnalysis(config);
//      Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaintsResults = this
//          .analyzePhosphorResults();
//
////      // CFA := get_constraints_from_analysis(ST)
////      Set<Constraint> constraintsFromAnalysis = this
////          .getConstraintsFromAnalysis(sinksToTaintsResults, config);
////
////      // CFA.removeAll(EC)
////      PhosphorAnalysis.removeAllSubConstraints(constraintsFromAnalysis, exploredConstraints);
////      // CE.addAll(CC)
////      constraintsToExplore.addAll(constraintsFromAnalysis);
//
//      count++;
//    }
//
//    System.out.println(count);
////    // TODO this might be done in the compression step, not in the analysis
////    this.getConfigsForCC();

  //  Set<ConfigConstraint> getSatisfiedConfigConstraintsByConfig(
//      Set<ConfigConstraint> configConstraints, ConfigConstraint executedConfigConstraint) {
//    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();
//
//    for (ConfigConstraint configConstraint : configConstraints) {
//      if (configConstraint.isSubConstraintOf(executedConfigConstraint)) {
//        satisfiedConfigConstraints.add(configConstraint);
//      }
//    }
//
//    return satisfiedConfigConstraints;
//  }

//  Map<String, Map<Set<String>, List<Set<String>>>> postProcessPhosphorAnalysis()
//      throws IOException {
//
////    Map<String, SinkData> sinksToData = new HashMap<>();
////    this.addSinks(sinksToData, sinksToTaints.keySet());
////    this.addExecVarCtxs(sinksToData, sinksToTaints, config);
////    this.addExecTaints(sinksToData, sinksToTaints, config);
//
//    return this.phosphorExecutionAnalysis.analyzePhosphorResults();
//  }

//
//  private Set<Map<String, Boolean>> getConfigMapsToRun(Set<String> config) {
//    Set<Map<String, Boolean>> configMapsToRun = new HashSet<>();
//
//    for (SinkData sinkData : this.sinksToData.values()) {
//      Set<Map<String, Boolean>> configsToRunPerSink = this
//          .getConfigsToRunPerSink(sinkData.getData(), config);
//      configMapsToRun.addAll(configsToRunPerSink);
//    }
//
//    return configMapsToRun;
//  }
//
//  private Set<Map<String, Boolean>> getConfigsToRunPerSink(Map<ExecVarCtx, ExecTaints> data,
//      Set<String> config) {
//    Set<Map<String, Boolean>> configsToRun = new HashSet<>();
//    PartialConfig configToPartialConfig = configToPartialConfig(config, this.getOptions());
//
//    for (Map.Entry<ExecVarCtx, ExecTaints> entry : data.entrySet()) {
//      ExecVarCtx execVarCtx = entry.getKey();
//      Map<String, Boolean> execVariabilityPartialConfig = execVarCtx.getPartialConfig();
//      ExecTaints execTaints = entry.getValue();
//
//      for (Set<String> options : execTaints.getTaints()) {
//        Set<Map<String, Boolean>> configsToRunPerOptions = this.getConfigsToRunPerOptions(options);
//
//        for (Map<String, Boolean> configToRunPerOptions : configsToRunPerOptions) {
//          if (!configToPartialConfig.getPartialConfig().equals(execVariabilityPartialConfig)) {
//            configToRunPerOptions.putAll(execVariabilityPartialConfig);
//          }
//
//          configsToRun.add(configToRunPerOptions);
//        }
//      }
//    }
//
//    return configsToRun;
//  }
//
//  private static PartialConfig configToPartialConfig(Set<String> config, Set<String> options) {
//    PartialConfig partialConfig = new PartialConfig();
//
//    for (String option : options) {
//      partialConfig.addEntry(option, false);
//    }
//
//    for (String option : config) {
//      partialConfig.addEntry(option, true);
//    }
//
//    return partialConfig;
//  }
//
//  private Set<Map<String, Boolean>> getConfigsToRunPerOptions(Set<String> options) {
//    Set<Map<String, Boolean>> configsToRun = new HashSet<>();
//    Set<Set<String>> configsForOptions = Helper.getConfigurations(options);
//
//    for (Set<String> configForOptions : configsForOptions) {
//      Map<String, Boolean> config = new HashMap<>();
//
//      for (String option : this.getOptions()) {
//        config.put(option, false);
//      }
//
//      for (String newOpt : configForOptions) {
//        config.put(newOpt, true);
//      }
//
//      configsToRun.add(config);
//    }
//
//    return configsToRun;
//  }
//
//  private void addSinks(Map<String, SinkData> sinksToData, Set<String> sinks) {
//    for (String sink : sinks) {
//      sinksToData.putIfAbsent(sink, new SinkData());
//    }
//  }
//
//  private void addExecVarCtxs(Map<String, SinkData> sinksToData,
//      Map<String, Map<Set<String>, List<Set<String>>>> sinksToTaints, Set<String> config) {
//    for (Map.Entry<String, Map<Set<String>, List<Set<String>>>> entry : sinksToTaints.entrySet()) {
//      SinkData sinkData = sinksToData.get(entry.getKey());
//      Set<Set<String>> sinkVarCtxs = entry.getValue().keySet();
//
//      for (Set<String> sinkVarCtx : sinkVarCtxs) {
//        ExecVarCtx execVarCtx = this.getExecVarCtx(sinkVarCtx, config);
//        sinkData.putIfAbsent(execVarCtx, new ExecTaints());
//      }
//    }
//  }
//
//  private void addExecTaints(Map<String, SinkData> sinksToData,
//      Map<String, Map<Set<String>, List<Set<String>>>> sinksToTaints, Set<String> config) {
//    for (Map.Entry<String, Map<Set<String>, List<Set<String>>>> entry : sinksToTaints.entrySet()) {
//      SinkData sinkData = sinksToData.get(entry.getKey());
//      this.addExecTaintsFromSink(entry.getValue(), sinkData, config);
//    }
//  }
//
//  private void addExecTaintsFromSink(Map<Set<String>, List<Set<String>>> sinkResults,
//      SinkData sinkData, Set<String> config) {
//    for (Map.Entry<Set<String>, List<Set<String>>> entry : sinkResults.entrySet()) {
//      Set<String> sinkVariabilityCtx = entry.getKey();
//      ExecVarCtx execVarCtx = this.getExecVarCtx(sinkVariabilityCtx, config);
//      ExecTaints executionTaints = sinkData.getExecTaints(execVarCtx);
//      executionTaints.addExecTaints(entry.getValue());
//    }
//  }
//
//  private ExecVarCtx getExecVarCtx(Set<String> sinkVarCtx, Set<String> config) {
//    ExecVarCtx execVarCtx = new ExecVarCtx();
//
//    if (sinkVarCtx.isEmpty()) {
//      for (String option : this.getOptions()) {
//        execVarCtx.addEntry(option, config.contains(option));
//      }
//    }
//    else {
//      for (String option : sinkVarCtx) {
//        execVarCtx.addEntry(option, config.contains(option));
//      }
//    }
//
//    return execVarCtx;
//  }
//
//
////  static void removeAllSubConstraints(Set<Constraint> constraintsFromAnalysis,
////      Set<Constraint> exploredConstraints) {
////    for (Constraint explored : exploredConstraints) {
////      removeAllSubConstraints(constraintsFromAnalysis, explored);
////    }
////  }
//
////  /**
////   * Removes the subconstraints of the passed constraint from passes constraints set.
////   */
////  static void removeAllSubConstraints(Set<Constraint> constraints, Constraint constraint) {
////    constraints.removeIf(currentConstraint -> currentConstraint.isSubsetOf(constraint));
////  }

//
//  //  /**
////   * Input: P', c in C
////   *
////   * Output: ST: S --> (P(O), P(O))
////   *
////   * Helper method for running the phosphor analysis. This method processes the results of the
////   * analysis and returns the output specified in the algorithm.
////   */
//

//
//

//
//  private Map<String, Map<Taint, List<Taint>>> addSinksFromAnalysis(Set<String> sinks) {
//    Map<String, Map<Taint, List<Taint>>> sinksToTaintInfos = new HashMap<>();
//
//    for (String sink : sinks) {
//      sinksToTaintInfos.put(sink, new HashMap<>());
//    }
//
//    return sinksToTaintInfos;
//  }
//

//

//
//

//

//

//

//

//

//
////  private Map<String, Set<String>> changeTaintLabelsToTaints(
////      Map<String, Set<TaintLabel>> sinksToTaintLabels) {
////    Map<String, Set<String>> sinksToTaints = new HashMap<>();
////
////    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaintLabels.entrySet()) {
////      Set<String> taints = new HashSet<>();
////
////      for (TaintLabel taintLabel : entry.getValue()) {
////        taints.add(taintLabel.getSource());
////      }
////
////      sinksToTaints.put(entry.getKey(), taints);
////    }
////
////    return sinksToTaints;
////  }
//
//
//
////  /**
////   * Input: ST: S --> (P(O), P(O)), c ∈ C
////   *
////   * Output: CFA: P(CT)
////   *
////   * Calculate the constraints from running the dynamic analysis
////   */
////  Set<Constraint> getConstraintsFromAnalysis(
////      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults,
////      Set<String> config) {
////    Map<String, Set<String>> sinksToTaintsFromTaints = sinksToTaintsResults.getLeft();
////    Map<String, Set<String>> sinksToTaintsFromCtx = sinksToTaintsResults.getRight();
////
////    if (sinksToTaintsFromTaints == null || sinksToTaintsFromCtx == null) {
////      throw new IllegalArgumentException("The sinks to taints result cannot be empty");
////    }
////
////    Set<Constraint> constraintsFromAnalysis = new HashSet<>();
////
////    Set<String> executedSinks = new HashSet<>(sinksToTaintsFromTaints.keySet());
////    executedSinks.addAll(sinksToTaintsFromCtx.keySet());
////    this.addNewSinks(executedSinks);
////
////    for (String sink : executedSinks) {
////      Set<Constraint> constraintsAtSink = this
////          .getConstraintsAtSink(sinksToTaintsFromTaints.get(sink),
////              sinksToTaintsFromCtx.get(sink),
////              config);
////
////      constraintsFromAnalysis.addAll(constraintsAtSink);
////
////      Set<Constraint> oldConstraints = this.sinksToConstraints.get(sink);
////      oldConstraints.addAll(constraintsAtSink);
////      this.sinksToConstraints.put(sink, oldConstraints);
////    }
////
////    return constraintsFromAnalysis;
////  }
//
////  /**
////   * Input: stv ∈ ST.values, c ∈ C
////   *
////   * Output: CS: P(CFA) // CFA: P(CT)
////   *
////   * Calculate the constraints at a sink
////   */
////  private Set<Constraint> getConstraintsAtSink(@Nullable Set<String> taintsFromTaint,
////      @Nullable Set<String> taintsFromCtx, Set<String> config) {
////    Set<Constraint> constraints = new HashSet<>();
////
////    Set<Map<String, Boolean>> partialConfigs = Constraint.buildPartialConfigs(taintsFromTaint);
////    Map<String, Boolean> ctx = Constraint.buildCtx(taintsFromCtx, config);
////
////    if (partialConfigs.isEmpty()) {
////      partialConfigs.add(new HashMap<>());
////    }
////
////    for (Map<String, Boolean> partialConfig : partialConfigs) {
////      constraints.add(new Constraint(partialConfig, ctx));
////    }
////
////    PhosphorAnalysis.removeInvalidConstraints(constraints);
////
////    return constraints;
////  }
//
////  static void removeInvalidConstraints(Set<Constraint> constraints) {
////    constraints.removeIf(constraint -> !constraint.isValid());
////  }
//
////  private void addNewSinks(Set<String> sinks) {
////    for (String sink : sinks) {
////      if (!this.sinksToConstraints.containsKey(sink)) {
////        this.sinksToConstraints.put(sink, new HashSet<>());
////      }
////    }
////  }
//
////  /**
////   * Input: CE, O
////   *
////   * Output: CTE: P(CE)
////   *
////   * Input: since we represent options set to false by not including them in the set that represents
////   * configurations, there is no need to pass them in the method.
////   *
////   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
////   */
////  static Constraint getNextConstraint(Set<Constraint> constraintsToEvaluate) {
////    if (constraintsToEvaluate.isEmpty()) {
////      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
////    }
////
////    if (constraintsToEvaluate.size() == 1) {
////      return constraintsToEvaluate.iterator().next();
////    }
////
////    // TODO check results if picking the longest constraint first
////    Set<Map<String, Boolean>> completeConstraints = getCompleteConstraints(constraintsToEvaluate);
////    Iterator<Map<String, Boolean>> iter = completeConstraints.iterator();
////    Map<String, Boolean> finalConstraintAsConfigWithValues = new HashMap<>(iter.next());
////
////    while (iter.hasNext()) {
////      Map<String, Boolean> currentConstraintAsConfigWithValues = iter.next();
////      Set<String> pivotOptions = getPivotOptions(finalConstraintAsConfigWithValues,
////          currentConstraintAsConfigWithValues);
////
////      if (pivotOptions.isEmpty()) {
////        finalConstraintAsConfigWithValues.putAll(currentConstraintAsConfigWithValues);
////      }
////      else {
////        Map<String, Boolean> finalConstraintPivotValues = getPivotValues(
////            finalConstraintAsConfigWithValues, pivotOptions);
////        Map<String, Boolean> currentConstraintPivotValues = getPivotValues(
////            currentConstraintAsConfigWithValues, pivotOptions);
////
////        if (!finalConstraintPivotValues.equals(currentConstraintPivotValues)) {
////          // Could not merge the constraints
////          continue;
////        }
////
////        finalConstraintAsConfigWithValues.putAll(currentConstraintAsConfigWithValues);
////      }
////    }
////
////    // TODO check if the constraint we picked is NOT a proper subset of a set left in the constraints set
////    return new Constraint(finalConstraintAsConfigWithValues);
////  }
//
////  private static Map<String, Boolean> getPivotValues(
////      Map<String, Boolean> constraintAsConfigWithValues, Set<String> pivotOptions) {
////    Map<String, Boolean> pivotValues = new HashMap<>();
////
////    for (String option : pivotOptions) {
////      pivotValues.put(option, constraintAsConfigWithValues.get(option));
////    }
////
////    return pivotValues;
////  }
//
////  private static Set<String> getPivotOptions(Map<String, Boolean> configWaithValues1,
////      Map<String, Boolean> configWaithValues2) {
////    Set<String> pivotOptions = new HashSet<>(configWaithValues1.keySet());
////    pivotOptions.retainAll(configWaithValues2.keySet());
////
////    return pivotOptions;
////  }
//
////  private static Set<Map<String, Boolean>> getCompleteConstraints(Set<Constraint> constraints) {
////    Set<Map<String, Boolean>> completeConstraints = new HashSet<>();
////
////    for (Constraint constraint : constraints) {
////      completeConstraints.add(constraint.getCompleteConstraint());
////    }
////
////    return completeConstraints;
////  }
//
////  Set<Set<String>> getConfigsForCC() {
////    Set<Constraint> ccConstraints = this.getAllConstraints();
////    Set<Set<String>> configs = new HashSet<>();
////
////    for (Constraint ccConstraint : ccConstraints) {
////      Set<String> config = Constraint.toPartialCCConfig(ccConstraint);
////      configs.add(config);
////    }
////
////    System.out.println(configs);
////    return configs;
////  }
//
////  private Map<String, Set<TaintLabel>> merge(Map<String, Set<TaintLabel>> sinksToTaints1,
////      Map<String, Set<TaintLabel>> sinksToTaints2) {
////    Map<String, Set<TaintLabel>> sinksToTaints = new HashMap<>(sinksToTaints1);
////
////    for (String sink : sinksToTaints2.keySet()) {
////      if (!sinksToTaints.containsKey(sink)) {
////        sinksToTaints.put(sink, new HashSet<>());
////      }
////    }
////
////    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaints2.entrySet()) {
////      String sink = entry.getKey();
////      Set<TaintLabel> taints = sinksToTaints.get(sink);
////      taints.addAll(entry.getValue());
////      sinksToTaints.put(sink, taints);
////    }
////
////    return sinksToTaints;
////  }
//
//  Map<String, SinkData> getSinksToData() {
//    return sinksToData;
//  }

}

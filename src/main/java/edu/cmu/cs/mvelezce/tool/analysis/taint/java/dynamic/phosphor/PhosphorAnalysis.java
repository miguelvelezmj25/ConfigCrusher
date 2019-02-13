package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.cc.SinkEntry;
import edu.cmu.cs.mvelezce.cc.TaintInfo;
import edu.cmu.cs.mvelezce.cc.TaintLabel;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.ifOr2.IfOr2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit.ImplicitAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit2.Implicit2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext.OrContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext2.OrContext2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext3.OrContext3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext6.OrContext6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample8.PhosphorExample8Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample5.SimpleForExample5Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext1.VariabilityContext1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext2.VariabilityContext2Adapter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
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
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class PhosphorAnalysis extends BaseDynamicRegionAnalysis<SinkData> {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";
  private static final String PHOSPHOR_SCRIPTS_DIR = BaseAdapter.USER_HOME
      + "/Documents/Programming/Java/Projects/phosphor/Phosphor/scripts/run-instrumented/implicit-optimized";

  private final Map<String, SinkData> sinksToData = new HashMap<>();

  public PhosphorAnalysis(String programName) {
    this(programName, new HashSet<>(), new HashSet<>());
  }

  PhosphorAnalysis(String programName, Set<String> options, Set<String> initialConfig) {
    super(programName, options, initialConfig);
  }

  @Override
  public Map<JavaRegion, SinkData> analyze() throws IOException, InterruptedException {
    this.runDynamicAnalysis();

    Map<JavaRegion, SinkData> regionsToData = new HashMap<>();

    for (Map.Entry<String, SinkData> entry : this.sinksToData.entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      regionsToData.put(region, entry.getValue());
    }

    return regionsToData;
  }

  @Override
  public Map<JavaRegion, SinkData> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo> results = mapper
        .readValue(file, new TypeReference<List<RegionToInfo>>() {
        });

    Map<JavaRegion, SinkData> regionsToConstraints = new HashMap<>();

    for (RegionToInfo<SinkData> result : results) {
      Map<String, Map> info = (Map<String, Map>) result.getInfo();
      Map<String, List> sinkDataEntries = info.get("data");

      SinkData sinkData = new SinkData();

      for (Map.Entry<String, List> entry : sinkDataEntries.entrySet()) {
        String execVarCtxStr = entry.getKey();
        ExecVarCtx execVarCtx = this.getExecVarCtx(execVarCtxStr);
        List<Map> execTaintsList = entry.getValue();
        Set<ExecTaints> executionTaints = this.getExecTaints(execTaintsList);

        sinkData.putIfAbsent(execVarCtx, executionTaints);
      }

      regionsToConstraints.put(result.getRegion(), sinkData);
    }

    return regionsToConstraints;
  }

  private Set<ExecTaints> getExecTaints(List<Map> execTaintsList) {
    Set<ExecTaints> allExecTaints = new HashSet<>();

    for (Map<String, List> map : execTaintsList) {
      List<List<String>> taintsLists = map.get("taints");
      Set<Set<String>> allTaints = new HashSet<>();

      for (List<String> taintLIst : taintsLists) {
        Set<String> taints = new HashSet<>(taintLIst);
        allTaints.add(taints);
      }

      ExecTaints execTaints = new ExecTaints();
      execTaints.addExecTaints(allTaints);

      allExecTaints.add(execTaints);
    }

    return allExecTaints;
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

  /**
   * Input: P, c in C, O
   *
   * Output: SC: S —> P(CT)
   *
   * Input: The program is provided elsewhere. Therefore, there is no need to pass the program to
   * this method.
   */
  void runDynamicAnalysis() throws IOException, InterruptedException {
    Set<Set<String>> exploredConfigs = new HashSet<>();
    Set<Set<String>> configsToExplore = new HashSet<>();

    configsToExplore.add(this.getInitialConfig());

    while (!configsToExplore.isEmpty()) {
      Set<String> config = configsToExplore.iterator().next();
      configsToExplore.remove(config);
      exploredConfigs.add(config);

      this.runPhosphorAnalysis(config);
      this.postProcessPhosphorAnalysis(config);

      Set<Set<String>> configsToRun = this.getConfigsToRun();
      configsToRun.removeAll(exploredConfigs);
      configsToExplore.addAll(configsToRun);
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
  }

  void postProcessPhosphorAnalysis(Set<String> config) throws IOException {
    Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints = this.analyzePhosphorResults();
    this.addSinks(sinksToTaints.keySet());
    this.addExecVarCtxs(sinksToTaints, config);
    this.addExecTaints(sinksToTaints, config);
  }

  private Set<Set<String>> getConfigsToRun() {
    Set<Map<String, Boolean>> configMapsToRun = this.getConfigMapsToRun();
    Set<Set<String>> configsToRun = new HashSet<>();

    for (Map<String, Boolean> configMapToRun : configMapsToRun) {
      Set<String> configToRun = new HashSet<>();

      for (Map.Entry<String, Boolean> entry : configMapToRun.entrySet()) {
        if (entry.getValue()) {
          configToRun.add(entry.getKey());
        }
      }

      configsToRun.add(configToRun);
    }

    return configsToRun;
  }

  private Set<Map<String, Boolean>> getConfigMapsToRun() {
    throw new UnsupportedOperationException("Implement");
//    Set<Map<String, Boolean>> configMapsToRun = new HashSet<>();
//
//    for (SinkData sinkData : this.sinksToData.values()) {
//      Set<Map<String, Boolean>> configsToRunPerSink = this
//          .getConfigsToRunPerSink(sinkData.getData());
//      configMapsToRun.addAll(configsToRunPerSink);
//    }
//
//    return configMapsToRun;
  }

  private Set<Map<String, Boolean>> getConfigsToRunPerSink(Map<ExecVarCtx, Set<Set<String>>> data) {
    Set<Map<String, Boolean>> configsToRun = new HashSet<>();

    for (Map.Entry<ExecVarCtx, Set<Set<String>>> entry : data.entrySet()) {
      ExecVarCtx execVarCtx = entry.getKey();
      Map<String, Boolean> execVariabilityPartialConfig = execVarCtx.getPartialConfig();

      for (Set<String> options : entry.getValue()) {
        Set<Map<String, Boolean>> configsToRunPerOptions = this.getConfigsToRunPerOptions(options);

        for (Map<String, Boolean> configToRunPerOptions : configsToRunPerOptions) {
          configToRunPerOptions.putAll(execVariabilityPartialConfig);
          configsToRun.add(configToRunPerOptions);
        }
      }
    }

    return configsToRun;
  }

  private Set<Map<String, Boolean>> getConfigsToRunPerOptions(Set<String> options) {
    Set<Map<String, Boolean>> configsToRun = new HashSet<>();
    Set<Set<String>> configsForOptions = Helper.getConfigurations(options);

    for (Set<String> configForOptions : configsForOptions) {
      Map<String, Boolean> config = new HashMap<>();

      for (String option : this.getOptions()) {
        config.put(option, false);
      }

      for (String newOpt : configForOptions) {
        config.put(newOpt, true);
      }

      configsToRun.add(config);
    }

    return configsToRun;
  }

  private void addSinks(Set<String> sinks) {
    for (String sink : sinks) {
      this.sinksToData.putIfAbsent(sink, new SinkData());
    }
  }

  private void addExecVarCtxs(Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints,
      Set<String> config) {
    for (Map.Entry<String, Map<Set<String>, Set<Set<String>>>> entry : sinksToTaints.entrySet()) {
      SinkData sinkData = this.sinksToData.get(entry.getKey());
      Set<Set<String>> sinkVarCtxs = entry.getValue().keySet();

      for (Set<String> sinkVarCtx : sinkVarCtxs) {
        ExecVarCtx execVarCtx = this.getExecVarCtx(sinkVarCtx, config);
        sinkData.putIfAbsent(execVarCtx, new HashSet<>());
      }
    }
  }

  private void addExecTaints(Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints,
      Set<String> config) {
    for (Map.Entry<String, Map<Set<String>, Set<Set<String>>>> entry : sinksToTaints.entrySet()) {
      SinkData sinkData = this.sinksToData.get(entry.getKey());
      this.addExecTaintsFromSink(entry.getValue(), sinkData, config);
    }
  }

  private void addExecTaintsFromSink(Map<Set<String>, Set<Set<String>>> sinkResults,
      SinkData sinkData, Set<String> config) {
    for (Map.Entry<Set<String>, Set<Set<String>>> entry : sinkResults.entrySet()) {
      Set<String> sinkVariabilityCtx = entry.getKey();
      ExecVarCtx execVarCtx = this.getExecVarCtx(sinkVariabilityCtx, config);
      Set<ExecTaints> executionTaints = sinkData.getExecTaints(execVarCtx);

      ExecTaints execTaints = new ExecTaints();
      execTaints.addExecTaints(entry.getValue());

      executionTaints.add(execTaints);
    }
  }

  private ExecVarCtx getExecVarCtx(Set<String> sinkVarCtx, Set<String> config) {
    ExecVarCtx execVarCtx = new ExecVarCtx();

    for (String option : sinkVarCtx) {
      execVarCtx.addEntry(option, config.contains(option));
    }

    return execVarCtx;
  }

  @Override
  public String outputDir() {
    return BaseDynamicRegionAnalysis.DIRECTORY + "/" + this.getProgramName() + "/cc";
  }

//  static void removeAllSubConstraints(Set<Constraint> constraintsFromAnalysis,
//      Set<Constraint> exploredConstraints) {
//    for (Constraint explored : exploredConstraints) {
//      removeAllSubConstraints(constraintsFromAnalysis, explored);
//    }
//  }

//  /**
//   * Removes the subconstraints of the passed constraint from passes constraints set.
//   */
//  static void removeAllSubConstraints(Set<Constraint> constraints, Constraint constraint) {
//    constraints.removeIf(currentConstraint -> currentConstraint.isSubsetOf(constraint));
//  }

  //  /**
//   * Input: P', c in C
//   *
//   * Output: ST: S --> (P(O), P(O))
//   *
//   * Input: There is a script that this method calls to executed the instrumented program.
//   * Therefore, we do not not pass the instrumented program to this method.
//   *
//   * Output: This method only runs the phosphor analysis. There is another method that processes the
//   * results from the analysis.
//   */
  void runPhosphorAnalysis(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));
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

  //  /**
//   * Input: P', c in C
//   *
//   * Output: ST: S --> (P(O), P(O))
//   *
//   * Helper method for running the phosphor analysis. This method processes the results of the
//   * analysis and returns the output specified in the algorithm.
//   */
  Map<String, Map<Set<String>, Set<Set<String>>>> analyzePhosphorResults()
      throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.getProgramName();
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dir + " must have 1 file.");
    }

    return this.readPhosphorTaintResults(serializedFiles.iterator().next());
  }

  private Map<String, Map<Set<String>, Set<Set<String>>>> readPhosphorTaintResults(
      File serializedFile)
      throws IOException {
    List<SinkEntry> sinkEntries = this.deserialize(serializedFile);
    Map<String, Map<Taint, Set<Taint>>> analysisData = this.getDataFromAnalysis(sinkEntries);
    Map<String, Map<Set<TaintLabel>, Set<Set<TaintLabel>>>> analysisDataWithLabels = this
        .getSinksToLabelData(analysisData);
    return this.changeLabelsToTaints(analysisDataWithLabels);
  }

  private Map<String, Map<Taint, Set<Taint>>> getDataFromAnalysis(List<SinkEntry> sinkEntries) {
    Set<String> sinks = this.getSinksAnalysis(sinkEntries);
    Map<String, Map<Taint, Set<Taint>>> sinksToTaintInfos = this.addSinksFromAnalysis(sinks);
    this.addCtxsFromAnalysis(sinksToTaintInfos, sinkEntries);
    this.addTaintsFromAnalysis(sinksToTaintInfos, sinkEntries);

    return sinksToTaintInfos;
  }

  private void addTaintsFromAnalysis(Map<String, Map<Taint, Set<Taint>>> sinksToTaintInfos,
      List<SinkEntry> sinkEntries) {
    for (SinkEntry sinkEntry : sinkEntries) {
      TaintInfo taintInfo = sinkEntry.getTaintInfo();
      Taint ctx = taintInfo.getCtx();

      String sink = sinkEntry.getSink();
      Map<Taint, Set<Taint>> taintInfos = sinksToTaintInfos.get(sink);

      Set<Taint> taints = taintInfos.get(ctx);
      Taint taint = taintInfo.getTaint();
      taints.add(taint);
    }
  }

  private Map<String, Map<Taint, Set<Taint>>> addSinksFromAnalysis(Set<String> sinks) {
    Map<String, Map<Taint, Set<Taint>>> sinksToTaintInfos = new HashMap<>();

    for (String sink : sinks) {
      sinksToTaintInfos.put(sink, new HashMap<>());
    }

    return sinksToTaintInfos;
  }

  private void addCtxsFromAnalysis(Map<String, Map<Taint, Set<Taint>>> sinksToTaintInfos,
      List<SinkEntry> sinkEntries) {
    for (SinkEntry sinkEntry : sinkEntries) {
      TaintInfo taintInfo = sinkEntry.getTaintInfo();
      Taint ctx = taintInfo.getCtx();

      String sink = sinkEntry.getSink();
      Map<Taint, Set<Taint>> taintInfos = sinksToTaintInfos.get(sink);
      taintInfos.put(ctx, new HashSet<>());
    }
  }

  private Set<String> getSinksAnalysis(List<SinkEntry> sinkEntries) {
    Set<String> sinks = new HashSet<>();

    for (SinkEntry sinkEntry : sinkEntries) {
      sinks.add(sinkEntry.getSink());
    }

    return sinks;
  }

  private Map<String, Map<Set<String>, Set<Set<String>>>> changeLabelsToTaints(
      Map<String, Map<Set<TaintLabel>, Set<Set<TaintLabel>>>> sinksToLabelData) {
    Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints = new HashMap<>();

    for (Map.Entry<String, Map<Set<TaintLabel>, Set<Set<TaintLabel>>>> entry : sinksToLabelData
        .entrySet()) {
      Map<Set<String>, Set<Set<String>>> taintData = this
          .transformDataLabelsToTaints(entry.getValue());
      sinksToTaints.put(entry.getKey(), taintData);
    }

    return sinksToTaints;
  }

  private Map<Set<String>, Set<Set<String>>> transformDataLabelsToTaints(
      Map<Set<TaintLabel>, Set<Set<TaintLabel>>> ctxsToTaintLabels) {
    Map<Set<String>, Set<Set<String>>> ctxsToTaints = new HashMap<>();

    for (Map.Entry<Set<TaintLabel>, Set<Set<TaintLabel>>> entry : ctxsToTaintLabels
        .entrySet()) {
      Set<String> ctx = this.transformLabelsToTaints(entry.getKey());
      Set<Set<String>> taintSets = new HashSet<>();

      for (Set<TaintLabel> LabelSet : entry.getValue()) {
        Set<String> taintSet = this.transformLabelsToTaints(LabelSet);
        taintSets.add(taintSet);
      }

      ctxsToTaints.put(ctx, taintSets);
    }

    return ctxsToTaints;
  }

  private Set<String> transformLabelsToTaints(Set<TaintLabel> labels) {
    Set<String> strings = new HashSet<>();

    for (TaintLabel taintLabel : labels) {
      strings.add(taintLabel.getSource());
    }

    return strings;
  }

  private Map<String, Map<Set<TaintLabel>, Set<Set<TaintLabel>>>> getSinksToLabelData(
      Map<String, Map<Taint, Set<Taint>>> analysisData) {
    Map<String, Map<Set<TaintLabel>, Set<Set<TaintLabel>>>> sinksToLabelData = new HashMap<>();

    for (Map.Entry<String, Map<Taint, Set<Taint>>> entry : analysisData.entrySet()) {
      Map<Taint, Set<Taint>> sinkData = entry.getValue();
      Map<Set<TaintLabel>, Set<Set<TaintLabel>>> variabilityCtxsToLabels = this
          .getVariabilityCtxsToLabels(sinkData);
      sinksToLabelData.put(entry.getKey(), variabilityCtxsToLabels);
    }

    return sinksToLabelData;
  }

  private Map<Set<TaintLabel>, Set<Set<TaintLabel>>> getVariabilityCtxsToLabels(
      Map<Taint, Set<Taint>> sinkData) {
    Map<Set<TaintLabel>, Set<Set<TaintLabel>>> variabilityCtxsToLabels = new HashMap<>();

    for (Map.Entry<Taint, Set<Taint>> entry : sinkData.entrySet()) {
      Taint variabilityCtxTaint = entry.getKey();
      Set<TaintLabel> variabilityCtx = this.getVariabilityCtx(variabilityCtxTaint);

      Set<Taint> executionTaints = entry.getValue();
      Set<Set<TaintLabel>> executionLabelSet = this.getExecutionLabelSet(executionTaints);

      variabilityCtxsToLabels.put(variabilityCtx, executionLabelSet);
    }

    return variabilityCtxsToLabels;
  }

  private Set<Set<TaintLabel>> getExecutionLabelSet(Set<Taint> executionTaints) {
    Set<Set<TaintLabel>> executionLabelSet = new HashSet<>();

    for (Taint taint : executionTaints) {
      Set<TaintLabel> labels = taint.getLabels();
      Set<TaintLabel> executionLabels = new HashSet<>(labels);
      executionLabelSet.add(executionLabels);
    }

    return executionLabelSet;
  }

  private Set<TaintLabel> getVariabilityCtx(Taint variabilityCtx) {
    if (variabilityCtx == null) {
      return new HashSet<>();
    }

    return variabilityCtx.getLabels();
  }

//  private Map<String, Set<String>> changeTaintLabelsToTaints(
//      Map<String, Set<TaintLabel>> sinksToTaintLabels) {
//    Map<String, Set<String>> sinksToTaints = new HashMap<>();
//
//    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaintLabels.entrySet()) {
//      Set<String> taints = new HashSet<>();
//
//      for (TaintLabel taintLabel : entry.getValue()) {
//        taints.add(taintLabel.getSource());
//      }
//
//      sinksToTaints.put(entry.getKey(), taints);
//    }
//
//    return sinksToTaints;
//  }

  // TODO check catching and throwing
  private List<SinkEntry> deserialize(File file) throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream ois = new ObjectInputStream(fis);
    List<SinkEntry> sinkEntries;

    try {
      sinkEntries = (List<SinkEntry>) ois.readObject();

    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    ois.close();
    fis.close();

    return sinkEntries;
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

//  /**
//   * Input: ST: S --> (P(O), P(O)), c ∈ C
//   *
//   * Output: CFA: P(CT)
//   *
//   * Calculate the constraints from running the dynamic analysis
//   */
//  Set<Constraint> getConstraintsFromAnalysis(
//      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults,
//      Set<String> config) {
//    Map<String, Set<String>> sinksToTaintsFromTaints = sinksToTaintsResults.getLeft();
//    Map<String, Set<String>> sinksToTaintsFromCtx = sinksToTaintsResults.getRight();
//
//    if (sinksToTaintsFromTaints == null || sinksToTaintsFromCtx == null) {
//      throw new IllegalArgumentException("The sinks to taints result cannot be empty");
//    }
//
//    Set<Constraint> constraintsFromAnalysis = new HashSet<>();
//
//    Set<String> executedSinks = new HashSet<>(sinksToTaintsFromTaints.keySet());
//    executedSinks.addAll(sinksToTaintsFromCtx.keySet());
//    this.addNewSinks(executedSinks);
//
//    for (String sink : executedSinks) {
//      Set<Constraint> constraintsAtSink = this
//          .getConstraintsAtSink(sinksToTaintsFromTaints.get(sink),
//              sinksToTaintsFromCtx.get(sink),
//              config);
//
//      constraintsFromAnalysis.addAll(constraintsAtSink);
//
//      Set<Constraint> oldConstraints = this.sinksToConstraints.get(sink);
//      oldConstraints.addAll(constraintsAtSink);
//      this.sinksToConstraints.put(sink, oldConstraints);
//    }
//
//    return constraintsFromAnalysis;
//  }

//  /**
//   * Input: stv ∈ ST.values, c ∈ C
//   *
//   * Output: CS: P(CFA) // CFA: P(CT)
//   *
//   * Calculate the constraints at a sink
//   */
//  private Set<Constraint> getConstraintsAtSink(@Nullable Set<String> taintsFromTaint,
//      @Nullable Set<String> taintsFromCtx, Set<String> config) {
//    Set<Constraint> constraints = new HashSet<>();
//
//    Set<Map<String, Boolean>> partialConfigs = Constraint.buildPartialConfigs(taintsFromTaint);
//    Map<String, Boolean> ctx = Constraint.buildCtx(taintsFromCtx, config);
//
//    if (partialConfigs.isEmpty()) {
//      partialConfigs.add(new HashMap<>());
//    }
//
//    for (Map<String, Boolean> partialConfig : partialConfigs) {
//      constraints.add(new Constraint(partialConfig, ctx));
//    }
//
//    PhosphorAnalysis.removeInvalidConstraints(constraints);
//
//    return constraints;
//  }

//  static void removeInvalidConstraints(Set<Constraint> constraints) {
//    constraints.removeIf(constraint -> !constraint.isValid());
//  }

//  private void addNewSinks(Set<String> sinks) {
//    for (String sink : sinks) {
//      if (!this.sinksToConstraints.containsKey(sink)) {
//        this.sinksToConstraints.put(sink, new HashSet<>());
//      }
//    }
//  }

//  /**
//   * Input: CE, O
//   *
//   * Output: CTE: P(CE)
//   *
//   * Input: since we represent options set to false by not including them in the set that represents
//   * configurations, there is no need to pass them in the method.
//   *
//   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
//   */
//  static Constraint getNextConstraint(Set<Constraint> constraintsToEvaluate) {
//    if (constraintsToEvaluate.isEmpty()) {
//      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
//    }
//
//    if (constraintsToEvaluate.size() == 1) {
//      return constraintsToEvaluate.iterator().next();
//    }
//
//    // TODO check results if picking the longest constraint first
//    Set<Map<String, Boolean>> completeConstraints = getCompleteConstraints(constraintsToEvaluate);
//    Iterator<Map<String, Boolean>> iter = completeConstraints.iterator();
//    Map<String, Boolean> finalConstraintAsConfigWithValues = new HashMap<>(iter.next());
//
//    while (iter.hasNext()) {
//      Map<String, Boolean> currentConstraintAsConfigWithValues = iter.next();
//      Set<String> pivotOptions = getPivotOptions(finalConstraintAsConfigWithValues,
//          currentConstraintAsConfigWithValues);
//
//      if (pivotOptions.isEmpty()) {
//        finalConstraintAsConfigWithValues.putAll(currentConstraintAsConfigWithValues);
//      }
//      else {
//        Map<String, Boolean> finalConstraintPivotValues = getPivotValues(
//            finalConstraintAsConfigWithValues, pivotOptions);
//        Map<String, Boolean> currentConstraintPivotValues = getPivotValues(
//            currentConstraintAsConfigWithValues, pivotOptions);
//
//        if (!finalConstraintPivotValues.equals(currentConstraintPivotValues)) {
//          // Could not merge the constraints
//          continue;
//        }
//
//        finalConstraintAsConfigWithValues.putAll(currentConstraintAsConfigWithValues);
//      }
//    }
//
//    // TODO check if the constraint we picked is NOT a proper subset of a set left in the constraints set
//    return new Constraint(finalConstraintAsConfigWithValues);
//  }

//  private static Map<String, Boolean> getPivotValues(
//      Map<String, Boolean> constraintAsConfigWithValues, Set<String> pivotOptions) {
//    Map<String, Boolean> pivotValues = new HashMap<>();
//
//    for (String option : pivotOptions) {
//      pivotValues.put(option, constraintAsConfigWithValues.get(option));
//    }
//
//    return pivotValues;
//  }

//  private static Set<String> getPivotOptions(Map<String, Boolean> configWaithValues1,
//      Map<String, Boolean> configWaithValues2) {
//    Set<String> pivotOptions = new HashSet<>(configWaithValues1.keySet());
//    pivotOptions.retainAll(configWaithValues2.keySet());
//
//    return pivotOptions;
//  }

//  private static Set<Map<String, Boolean>> getCompleteConstraints(Set<Constraint> constraints) {
//    Set<Map<String, Boolean>> completeConstraints = new HashSet<>();
//
//    for (Constraint constraint : constraints) {
//      completeConstraints.add(constraint.getCompleteConstraint());
//    }
//
//    return completeConstraints;
//  }

//  Set<Set<String>> getConfigsForCC() {
//    Set<Constraint> ccConstraints = this.getAllConstraints();
//    Set<Set<String>> configs = new HashSet<>();
//
//    for (Constraint ccConstraint : ccConstraints) {
//      Set<String> config = Constraint.toPartialCCConfig(ccConstraint);
//      configs.add(config);
//    }
//
//    System.out.println(configs);
//    return configs;
//  }

//  private Map<String, Set<TaintLabel>> merge(Map<String, Set<TaintLabel>> sinksToTaints1,
//      Map<String, Set<TaintLabel>> sinksToTaints2) {
//    Map<String, Set<TaintLabel>> sinksToTaints = new HashMap<>(sinksToTaints1);
//
//    for (String sink : sinksToTaints2.keySet()) {
//      if (!sinksToTaints.containsKey(sink)) {
//        sinksToTaints.put(sink, new HashSet<>());
//      }
//    }
//
//    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaints2.entrySet()) {
//      String sink = entry.getKey();
//      Set<TaintLabel> taints = sinksToTaints.get(sink);
//      taints.addAll(entry.getValue());
//      sinksToTaints.put(sink, taints);
//    }
//
//    return sinksToTaints;
//  }

  static Set<ConfigConstraint> getAnalysisConfigConstraints(Collection<SinkData> sinkDatas) {
    Set<ConfigConstraint> configConstraints = new HashSet<>();

    for (SinkData sinkData : sinkDatas) {
      Set<ConfigConstraint> configConstraintsAtSink = getConfigConstraintsAtSink(sinkData);
      configConstraints.addAll(configConstraintsAtSink);
    }

    return configConstraints;
  }

  private static Set<ConfigConstraint> getConfigConstraintsAtSink(SinkData sinkData) {
    Set<ConfigConstraint> configConstraintsAtSink = new HashSet<>();

    for (Map.Entry<ExecVarCtx, Set<ExecTaints>> data : sinkData.getData().entrySet()) {
      ExecVarCtx execVarCtx = data.getKey();
      Set<Set<ConfigConstraint>> allSinkConstraints = PhosphorAnalysis
          .getConfigConstraintsForExecVarCtx(execVarCtx, data);

      for (Set<ConfigConstraint> sinkConstraint : allSinkConstraints) {
        configConstraintsAtSink.addAll(sinkConstraint);
      }
    }

    return configConstraintsAtSink;
  }

  static void printProgramConstraints(Map<JavaRegion, SinkData> regionsToSinkData) {
    for (Map.Entry<JavaRegion, SinkData> regionToSinkData : regionsToSinkData.entrySet()) {
      JavaRegion region = regionToSinkData.getKey();
      SinkData sinkData = regionToSinkData.getValue();

      printConfigConstraintsForRegion(region, sinkData);

      System.out.println();
    }
  }

  private static void printConfigConstraintsForRegion(JavaRegion region, SinkData sinkData) {
    for (Map.Entry<ExecVarCtx, Set<ExecTaints>> data : sinkData.getData().entrySet()) {
      ExecVarCtx execVarCtx = data.getKey();
      Set<Set<ConfigConstraint>> regionConstraints = getConfigConstraintsForExecVarCtx(execVarCtx,
          data);

      for (Set<ConfigConstraint> cs : regionConstraints) {
        System.out
            .println(region.getRegionMethod() + ":" + region.getStartRegionIndex() + " -> " + cs);
      }
    }
  }

  private static Set<Set<ConfigConstraint>> getConfigConstraintsForExecVarCtx(ExecVarCtx execVarCtx,
      Entry<ExecVarCtx, Set<ExecTaints>> data) {
    Set<Set<ConfigConstraint>> regionConfigConstraints = new HashSet<>();

    for (ExecTaints execTaints : data.getValue()) {
      Set<ConfigConstraint> configConstraints = getConfigConstraintsForExecTaints(execVarCtx,
          execTaints);
      regionConfigConstraints.add(configConstraints);
    }

    return regionConfigConstraints;
  }

  private static Set<ConfigConstraint> getConfigConstraintsForExecTaints(ExecVarCtx execVarCtx,
      ExecTaints execTaints) {
    Set<ConfigConstraint> configConstraints = new HashSet<>();

    for (Set<String> execTaint : execTaints.getTaints()) {
      Set<Set<String>> configs = Helper.getConfigurations(execTaint);
      Set<ConfigConstraint> execTaintConstraints = PhosphorAnalysis
          .getConfigConstraints(configs, execTaint);

      for (ConfigConstraint execTaintConstraint : execTaintConstraints) {
        ConfigConstraint configConstraint = new ConfigConstraint();
        configConstraint.addEntries(execTaintConstraint.getPartialConfig());

        if (!execVarCtx.getPartialConfig().isEmpty()) {
          configConstraint.addEntries(execVarCtx.getPartialConfig());
        }

        configConstraints.add(configConstraint);
      }
    }

    return configConstraints;
  }

  private static Set<ConfigConstraint> getConfigConstraints(Set<Set<String>> configs,
      Set<String> options) {
    Set<ConfigConstraint> configConstraints = new HashSet<>();

    for (Set<String> config : configs) {
      ConfigConstraint configConstraint = new ConfigConstraint();

      for (String option : options) {
        configConstraint.addEntry(option, config.contains(option));
      }

      configConstraints.add(configConstraint);
    }

    return configConstraints;
  }

  Map<String, SinkData> getSinksToData() {
    return sinksToData;
  }
}

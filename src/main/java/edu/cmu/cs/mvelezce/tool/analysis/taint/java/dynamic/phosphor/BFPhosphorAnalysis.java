package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic.AllDynamicAdapter;
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

public class BFPhosphorAnalysis extends PhosphorAnalysis {

  private final Map<String, SinkData> sinksToData = new HashMap<>();

  private String mainClass;

  BFPhosphorAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public BFPhosphorAnalysis(String programName) {
    super(programName);
  }

  public BFPhosphorAnalysis(String programName, String mainClass, Set<String> options) {
    this(programName, options);

    this.mainClass = mainClass;
  }

  @Override
  public Map<JavaRegion, SinkData> analyze() throws IOException, InterruptedException {
    this.runDynamicAnalysis();

    Map<JavaRegion, SinkData> regionsToConstraints = new HashMap<>();

    for (Map.Entry<String, SinkData> entry : this.sinksToData.entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      regionsToConstraints.put(region, entry.getValue());
    }

    return regionsToConstraints;
  }

  @Override
  protected void runDynamicAnalysis() throws IOException, InterruptedException {
    // TODO weird warning
    Set<String> options = this.getOptions();
    Set<Set<String>> configs = Helper.getConfigurations(options);

    for (Set<String> config : configs) {
//      config = this.getOptions();

      // ST := run_taint_analysis(P’, c)
      this.runPhosphorAnalysis(config);
      // TODO weird warning
      Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints = this.analyzePhosphorResults();

      this.addSinks(sinksToTaints.keySet());
      this.addExecVarCtxs(sinksToTaints, config);
      this.addExecTaints(sinksToTaints, config);
//      System.out.println();

//      // CFA := get_constraints_from_analysis(ST)
//      this.getConstraintsFromAnalysis(sinksToTaintsResults, config);
    }

    System.out.println();

    for (Map.Entry<String, SinkData> entry : this.sinksToData.entrySet()) {
      SinkData v = entry.getValue();

      for (Map.Entry<ExecVarCtx, Set<Set<String>>> x : v.getData().entrySet()) {
        for (Set<String> s : x.getValue()) {
          System.out.println(entry.getKey() + " --> " + x.getKey() + " --> " + s);
        }
      }
    }
  }

  // TODO this method is hardcoded to run all dynamic examples
  @Override
  protected List<String> buildCommandAsList(Set config) {
    if(this.mainClass == null) {
      return super.buildCommandAsList(config);
    }

    String programName = this.getProgramName();
    Adapter adapter = new AllDynamicAdapter(programName, this.mainClass);

    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);

    List<String> commandList = new ArrayList<>();
    commandList.add("./examples.sh");
    commandList.add(adapter.getMainClass());
    commandList.addAll(configList);

    return commandList;
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
      Set<String> sinkVariabilityContext = entry.getKey();
      ExecVarCtx execVarCtx = this.getExecVarCtx(sinkVariabilityContext, config);
      Set<Set<String>> executionTaints = sinkData.getExecTaints(execVarCtx);
      executionTaints.addAll(entry.getValue());
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
        List<List<String>> execTaintsList = entry.getValue();
        Set<Set<String>> executionTaints = this.getExecTaints(execTaintsList);

        sinkData.putIfAbsent(execVarCtx, executionTaints);
      }

      regionsToConstraints.put(result.getRegion(), sinkData);
    }

    return regionsToConstraints;
  }

  private Set<Set<String>> getExecTaints(List<List<String>> execTaintsList) {
    Set<Set<String>> execTaints = new HashSet<>();

    for (List<String> taints : execTaintsList) {
      Set<String> options = new HashSet<>(taints);
      execTaints.add(options);
    }

    return execTaints;
  }

  private ExecVarCtx getExecVarCtx(String execVarCtxStr) {
    execVarCtxStr = execVarCtxStr.replace("[[", "");
    execVarCtxStr = execVarCtxStr.replace("]]", "");
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

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/bf";
  }
}

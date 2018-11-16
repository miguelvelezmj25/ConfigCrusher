package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BFPhosphorAnalysis extends PhosphorAnalysis {

  private Map<String, SinkData> sinksToData = new HashMap<>();

  BFPhosphorAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public BFPhosphorAnalysis(String programName) {
    super(programName);
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

      for (Map.Entry<ExecVarCtx, Set<Set<String>>> x : v.getSinkData().entrySet()) {
        for (Set<String> s : x.getValue()) {
          System.out.println(entry.getKey() + " --> " + x.getKey() + " --> " + s);
        }
      }
    }
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
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/bf";
  }
}

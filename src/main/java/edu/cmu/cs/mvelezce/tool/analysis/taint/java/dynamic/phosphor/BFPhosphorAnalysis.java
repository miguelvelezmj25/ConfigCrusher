package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BFPhosphorAnalysis extends PhosphorAnalysis {

  private Map<String, Map<VariabilityContext, Set<Set<String>>>> sinksToData = new HashMap<>();

  BFPhosphorAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public BFPhosphorAnalysis(String programName) {
    super(programName);
  }

  @Override
  protected void runDynamicAnalysis() throws IOException, InterruptedException {

    Set<Set<String>> configs = Helper.getConfigurations(this.getOptions());

    for (Set<String> config : configs) {
//      config = this.getOptions();

      // ST := run_taint_analysis(P’, c)
      this.runPhosphorAnalysis(config);
      Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints = this.analyzePhosphorResults();

      this.addSinks(sinksToTaints.keySet());
      this.addVariabilityContexts(sinksToTaints, config);
      this.addExecutionTaints(sinksToTaints, config);
//      System.out.println();

//      // CFA := get_constraints_from_analysis(ST)
//      this.getConstraintsFromAnalysis(sinksToTaintsResults, config);
    }

    System.out.println();


    for(Map.Entry<String, Map<VariabilityContext, Set<Set<String>>>> entry : this.sinksToData.entrySet()) {
      Map<VariabilityContext, Set<Set<String>>> v = entry.getValue();

      for(Map.Entry<VariabilityContext, Set<Set<String>>> x : v.entrySet()) {
        for(Set<String> s : x.getValue()) {
          System.out.println(entry.getKey() + " --> " + x.getKey() + " --> " + s);
        }
      }
    }

//    for(Map.Entry<String, Context> entry : this.sinksToData.entrySet()) {
//      Object v = entry.getValue().getContext().isEmpty() ? configs : entry.getValue();
//      System.out.println(entry.getKey() + " -> " + v);
//    }
//
//    System.out.println();
//
//    for(Map.Entry<String, Set<String>> entry : this.sinksToOptions.entrySet()) {
//      System.out.println(entry.getKey() + " -> " + entry.getValue());
//    }
  }

  private void addExecutionTaints(Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints,
      Set<String> config) {
    for (Map.Entry<String, Map<Set<String>, Set<Set<String>>>> entry : sinksToTaints.entrySet()) {
      Map<VariabilityContext, Set<Set<String>>> sinkData = this.sinksToData.get(entry.getKey());
      this.addExecutionTaintsFromSink(entry.getValue(), sinkData, config);
    }
  }

  private void addExecutionTaintsFromSink(Map<Set<String>, Set<Set<String>>> sinkResults,
      Map<VariabilityContext, Set<Set<String>>> sinkData, Set<String> config) {
    for (Map.Entry<Set<String>, Set<Set<String>>> entry : sinkResults.entrySet()) {
      Set<String> sinkVariabilityContext = entry.getKey();
      VariabilityContext variabilityContext = this
          .getVariabilityContext(sinkVariabilityContext, config);
      Set<Set<String>> executionTaints = sinkData.get(variabilityContext);
      executionTaints.addAll(entry.getValue());
    }
  }

  private void addVariabilityContexts(Map<String, Map<Set<String>, Set<Set<String>>>> sinksToTaints,
      Set<String> config) {
    for (Map.Entry<String, Map<Set<String>, Set<Set<String>>>> entry : sinksToTaints.entrySet()) {
      Map<VariabilityContext, Set<Set<String>>> sinkData = this.sinksToData.get(entry.getKey());
      Set<Set<String>> sinkVariabilityContexts = entry.getValue().keySet();

      for (Set<String> sinkVariabilityContext : sinkVariabilityContexts) {
        VariabilityContext variabilityContext = this
            .getVariabilityContext(sinkVariabilityContext, config);
        sinkData.putIfAbsent(variabilityContext, new HashSet<>());
      }
    }
  }

  private VariabilityContext getVariabilityContext(Set<String> sinkVariabilityContext,
      Set<String> config) {
    VariabilityContext variabilityContext = new VariabilityContext();

    for (String option : sinkVariabilityContext) {
      variabilityContext.addEntry(option, config.contains(option));
    }

    return variabilityContext;
  }

  private void addSinks(Set<String> sinks) {
    for (String sink : sinks) {
      this.sinksToData.putIfAbsent(sink, new HashMap<>());
    }
  }

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/bf";
  }
}

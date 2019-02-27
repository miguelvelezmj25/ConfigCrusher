package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhosphorConfigConstraintTracker {

  private final Map<String, Map<ExecVarCtx, ExecConfigConstraints>> sinksToConfigConstraints = new HashMap<>();

  public Map<String, Map<ExecVarCtx, ExecConfigConstraints>> getSinksToConfigConstraints() {
    return sinksToConfigConstraints;
  }

  void deriveConstraints(Set<String> config,
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints) {
    addSinks(sinksToAnalysisTaints.keySet());
    addOrUpdateExecVarCtx(sinksToAnalysisTaints, config);
    addConfigConstraints(sinksToAnalysisTaints);
  }

  private void addConfigConstraints(
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints) {
    for (Map.Entry<String, Map<Set<String>, List<Set<String>>>> analysisEntry : sinksToAnalysisTaints
        .entrySet()) {
      String analysisSink = analysisEntry.getKey();
      Map<ExecVarCtx, ExecConfigConstraints> execVarCtxsToConfigConstraints = sinksToConfigConstraints
          .get(analysisSink);

      Map<Set<String>, List<Set<String>>> analysisCtxsToTaints = analysisEntry.getValue();
      addConfigConstraintsForSink(analysisCtxsToTaints, execVarCtxsToConfigConstraints);
    }
  }

  private void addConfigConstraintsForSink(
      Map<Set<String>, List<Set<String>>> analysisCtxsToTaints,
      Map<ExecVarCtx, ExecConfigConstraints> execVarCtxsToConfigConstraints) {
    Set<ExecVarCtx> execVarCtxs = execVarCtxsToConfigConstraints.keySet();

    for (ExecVarCtx execVarCtx : execVarCtxs) {
      Set<String> ctx = execVarCtx.toConfig();
      List<Set<String>> analysisTaints = analysisCtxsToTaints.get(ctx);

      ExecConfigConstraints execConfigConstraints = execVarCtxsToConfigConstraints.get(execVarCtx);
      List<Set<ConfigConstraint>> existingExecConfigConstraints = execConfigConstraints
          .getConfigConstraints();
      addConfigConstraintsForCtx(execVarCtx, analysisTaints, existingExecConfigConstraints);
    }

  }

  private void addConfigConstraintsForCtx(ExecVarCtx execVarCtx,
      List<Set<String>> analysisTaints,
      List<Set<ConfigConstraint>> existingExecConfigConstraints) {

    for (int i = 0; i < analysisTaints.size(); i++) {
      Set<String> analysisTaint = analysisTaints.get(i);
      Set<ConfigConstraint> analysisTaintConfigConstraints = getTaintConfigConstraintsForCtx(
          execVarCtx,
          analysisTaint);

      if (existingExecConfigConstraints.size() <= i) {
        existingExecConfigConstraints.add(analysisTaintConfigConstraints);
      }
      else {
        Set<ConfigConstraint> execConfigConstraint = existingExecConfigConstraints.get(i);

        if (!execConfigConstraint.equals(analysisTaintConfigConstraints)) {
          execConfigConstraint.addAll(analysisTaintConfigConstraints);
          this.addInteractingConfigConstraints(execConfigConstraint);
        }
      }

    }
  }

  private void addInteractingConfigConstraints(Set<ConfigConstraint> execConfigConstraint) {
    Set<String> options = new HashSet<>();

    for (ConfigConstraint configConstraint : execConfigConstraint) {
      options.addAll(configConstraint.getOptions());
    }

    Set<ConfigConstraint> updatedConfigConstraints = getTaintConfigConstraints(options);
    execConfigConstraint.clear();
    execConfigConstraint.addAll(updatedConfigConstraints);
  }

  private Set<ConfigConstraint> getTaintConfigConstraintsForCtx(ExecVarCtx execVarCtx,
      Set<String> taint) {
    Set<ConfigConstraint> taintConfigConstraintsForCtx = new HashSet<>();
    Set<ConfigConstraint> taintConfigConstraints = getTaintConfigConstraints(taint);

    for (ConfigConstraint execTaintConstraint : taintConfigConstraints) {
      ConfigConstraint configConstraint = new ConfigConstraint();
      configConstraint.addEntries(execTaintConstraint.getPartialConfig());

      if (!execVarCtx.getPartialConfig().isEmpty()) {
        configConstraint.addEntries(execVarCtx.getPartialConfig());
      }

      taintConfigConstraintsForCtx.add(configConstraint);
    }

    return taintConfigConstraintsForCtx;
  }

  private Set<ConfigConstraint> getTaintConfigConstraints(Set<String> taint) {
    Set<Set<String>> configs = Helper.getConfigurations(taint);
    return getConfigConstraints(configs, taint);
  }


  private Set<ConfigConstraint> getConfigConstraints(Set<Set<String>> configs,
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

  private void addOrUpdateExecVarCtx(
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints,
      Set<String> config) {
    for (Map.Entry<String, Map<Set<String>, List<Set<String>>>> analysisEntry : sinksToAnalysisTaints
        .entrySet()) {
      String analysisSink = analysisEntry.getKey();
      Map<ExecVarCtx, ExecConfigConstraints> existingExecVarCtxsToConfigConstraints = sinksToConfigConstraints
          .get(analysisSink);

      Map<Set<String>, List<Set<String>>> analysisCtxsToTaints = analysisEntry.getValue();
      addOrUpdateExecVarCtxsForSink(analysisCtxsToTaints.keySet(),
          existingExecVarCtxsToConfigConstraints, config);
    }

  }

  private void addOrUpdateExecVarCtxsForSink(Set<Set<String>> analysisCtxs,
      Map<ExecVarCtx, ExecConfigConstraints> existingExecVarCtxsToConfigConstraints,
      Set<String> config) {
    for (Set<String> analysisCtx : analysisCtxs) {
      ExecVarCtx analysisExecVarCtx = getExecVarCtx(analysisCtx, config);

      if (existingExecVarCtxsToConfigConstraints.isEmpty()) {
        existingExecVarCtxsToConfigConstraints.put(analysisExecVarCtx, new ExecConfigConstraints());
      }
      else if (!existingExecVarCtxsToConfigConstraints.containsKey(analysisExecVarCtx)) {
        this.updateExistingExecVarCtxs();
      }
    }
  }

  private void updateExistingExecVarCtxs() {
    throw new UnsupportedOperationException("Implement");
  }

  private ExecVarCtx getExecVarCtx(Set<String> ctx, Set<String> config) {
    ExecVarCtx execVarCtx = new ExecVarCtx();

    for (String option : ctx) {
      execVarCtx.addEntry(option, config.contains(option));
    }

    return execVarCtx;
  }

  private void addSinks(Set<String> sinks) {
    for (String sink : sinks) {
      sinksToConfigConstraints.putIfAbsent(sink, new HashMap<>());
    }
  }

  Set<ConfigConstraint> getConfigConstraints() {
    Set<ConfigConstraint> configConstraints = new HashSet<>();

    for (Map<ExecVarCtx, ExecConfigConstraints> constraintsPerCtxAtSinks : sinksToConfigConstraints
        .values()) {
      Collection<ExecConfigConstraints> constraintsAtSink = constraintsPerCtxAtSinks.values();

      for (ExecConfigConstraints execConstraints : constraintsAtSink) {
        List<Set<ConfigConstraint>> constraintsPerCtx = execConstraints.getConfigConstraints();

        for (Set<ConfigConstraint> execConfigConstraints : constraintsPerCtx) {
          configConstraints.addAll(execConfigConstraints);
        }
      }
    }

    return configConstraints;
  }

  //  private void toConfigConstraints(Set<String> config, Set<String> ctx,
//      List<Set<String>> taints) {
//    Set<ConfigConstraint> configConstraints = new HashSet<>();
//
//    PartialConfig ctxAsPartialConfig = toPartialConfig(ctx, config);
//
//        Map<String, Boolean> execVarCtxPartialConfig = execVarCtx.getPartialConfig();
//    Map<String, Boolean> configPartialConfig = configAsPartialConfig.getPartialConfig();
//
//    for (Set<String> execTaint : execTaints.getTaints()) {
//      Set<Set<String>> configs = Helper.getConfigurations(execTaint);
//      Set<ConfigConstraint> execTaintConstraints = PhosphorAnalysis
//          .getConfigConstraints(configs, execTaint);
//
//      for (ConfigConstraint execTaintConstraint : execTaintConstraints) {
//        ConfigConstraint configConstraint = new ConfigConstraint();
//        configConstraint.addEntries(execTaintConstraint.getPartialConfig());
//
//        if (!execVarCtxPartialConfig.equals(configPartialConfig)) {
//          configConstraint.addEntries(execVarCtx.getPartialConfig());
//        }
//
//        configConstraints.add(configConstraint);
//      }
//    }
//
//    return configConstraints;
//  }

//  private PartialConfig toPartialConfig(Set<String> ctx, Set<String> config) {
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

//  private Set<ConfigConstraint> getConfigConstraintsForExecTaints(ExecVarCtx execVarCtx,
//      ExecTaints execTaints, PartialConfig configAsPartialConfig) {
//    Set<ConfigConstraint> configConstraints = new HashSet<>();
//    Map<String, Boolean> execVarCtxPartialConfig = execVarCtx.getPartialConfig();
//    Map<String, Boolean> configPartialConfig = configAsPartialConfig.getPartialConfig();
//
//    for (Set<String> execTaint : execTaints.getTaints()) {
//      Set<Set<String>> configs = Helper.getConfigurations(execTaint);
//      Set<ConfigConstraint> execTaintConstraints = PhosphorAnalysis
//          .getConfigConstraints(configs, execTaint);
//
//      for (ConfigConstraint execTaintConstraint : execTaintConstraints) {
//        ConfigConstraint configConstraint = new ConfigConstraint();
//        configConstraint.addEntries(execTaintConstraint.getPartialConfig());
//
//        if (!execVarCtxPartialConfig.equals(configPartialConfig)) {
//          configConstraint.addEntries(execVarCtx.getPartialConfig());
//        }
//
//        configConstraints.add(configConstraint);
//      }
//    }
//
//    return configConstraints;
//  }

  //  Set<ConfigConstraint> getAnalysisConfigConstraints(Collection<SinkData> sinkDatas,
//      Set<String> config) {
//    Set<ConfigConstraint> configConstraints = new HashSet<>();
//
//    for (SinkData sinkData : sinkDatas) {
//      Set<ConfigConstraint> configConstraintsAtSink = getAnalysisConfigConstraints(
//          sinkData.getData(), config);
//      configConstraints.addAll(configConstraintsAtSink);
//    }
//
//    return configConstraints;
//  }
//
//  private Set<ConfigConstraint> getAnalysisConfigConstraints(
//      Map<ExecVarCtx, ExecTaints> sinkData, Set<String> config) {
//    Set<ConfigConstraint> configConstraintsAtSink = new HashSet<>();
//
//    for (Map.Entry<ExecVarCtx, ExecTaints> ctxsToSetsOfTaints : sinkData.entrySet()) {
//      ExecVarCtx execVarCtx = ctxsToSetsOfTaints.getKey();
//      ExecTaints execTaints = ctxsToSetsOfTaints.getValue();
//      PartialConfig configPartialConfig = configToPartialConfig(config, this.getOptions());
//      Set<ConfigConstraint> allSinkConstraints = PhosphorAnalysis
//          .getConfigConstraintsForExecVarCtx(execVarCtx, execTaints, configPartialConfig);
//
//      configConstraintsAtSink.addAll(allSinkConstraints);
//    }
//
//    return configConstraintsAtSink;
//  }
//
//
//  private Set<ConfigConstraint> getConfigConstraintsForExecVarCtx(ExecVarCtx execVarCtx,
//      ExecTaints execTaints, PartialConfig configAsPartialConfig) {
//    return getConfigConstraintsForExecTaints(execVarCtx, execTaints, configAsPartialConfig);
//  }
//
}


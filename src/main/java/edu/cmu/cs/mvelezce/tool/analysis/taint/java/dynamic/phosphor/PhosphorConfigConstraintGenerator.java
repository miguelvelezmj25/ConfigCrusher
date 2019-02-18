package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhosphorConfigConstraintGenerator {

  private final Map<String, Map<ExecVarCtx, ExecConstraints>> sinksToConfigConstraints = new HashMap<>();

  void updateConstraintsAtSinks(Set<String> config,
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints) {
    addSinks(sinksToAnalysisTaints.keySet());
    addExecVarCtx(sinksToAnalysisTaints, config);
    addOrUpdateConstraints(sinksToAnalysisTaints);
  }

  private void addOrUpdateConstraints(
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints) {
    for (Map.Entry<String, Map<Set<String>, List<Set<String>>>> entry : sinksToAnalysisTaints
        .entrySet()) {
      String sink = entry.getKey();
      Map<ExecVarCtx, ExecConstraints> execVarCtxsToConfigConstraints = sinksToConfigConstraints
          .get(sink);

      Map<Set<String>, List<Set<String>>> ctxsToTaints = entry.getValue();
      addOrUpdateConstraintsForSink(ctxsToTaints, execVarCtxsToConfigConstraints);
    }
  }

  private void addOrUpdateConstraintsForSink(
      Map<Set<String>, List<Set<String>>> ctxsToTaints,
      Map<ExecVarCtx, ExecConstraints> execVarCtxsToConfigConstraints) {
    Set<ExecVarCtx> execVarCtxs = execVarCtxsToConfigConstraints.keySet();

    for (ExecVarCtx execVarCtx : execVarCtxs) {
      Set<String> ctx = execVarCtx.toConfig();
      List<Set<String>> taints = ctxsToTaints.get(ctx);

      ExecConstraints configConstraints = execVarCtxsToConfigConstraints.get(execVarCtx);
      addOrUpdateConstraintsForCtx(execVarCtx, taints, configConstraints);
    }

  }

  private void addOrUpdateConstraintsForCtx(ExecVarCtx execVarCtx, List<Set<String>> taints,
      ExecConstraints configConstraints) {
    for (Set<String> taint : taints) {
      Set<ConfigConstraint> taintConfigConstraints = getTaintConfigConstraintsForCtx(execVarCtx,
          taint);

      if (configConstraints.getConstraints().isEmpty()) {
        configConstraints.addExecConstraints(taintConfigConstraints);
      }
      else {
        throw new UnsupportedOperationException("Implement");
      }
    }

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

  private void addExecVarCtx(
      Map<String, Map<Set<String>, List<Set<String>>>> sinksToAnalysisTaints,
      Set<String> config) {
    for (Map.Entry<String, Map<Set<String>, List<Set<String>>>> entry : sinksToAnalysisTaints
        .entrySet()) {
      String sink = entry.getKey();
      Map<ExecVarCtx, ExecConstraints> execVarCtxsToConfigConstraints = sinksToConfigConstraints
          .get(sink);

      Map<Set<String>, List<Set<String>>> ctxsToTaints = entry.getValue();
      AddExecVarCtxsForSink(ctxsToTaints.keySet(), execVarCtxsToConfigConstraints, config);
    }

  }

  private void AddExecVarCtxsForSink(Set<Set<String>> ctxs,
      Map<ExecVarCtx, ExecConstraints> execVarCtxsToConfigConstraints, Set<String> config) {
    for (Set<String> ctx : ctxs) {
      ExecVarCtx execVarCtx = getExecVarCtx(ctx, config);
      execVarCtxsToConfigConstraints.putIfAbsent(execVarCtx, new ExecConstraints());
    }
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

    for (Map<ExecVarCtx, ExecConstraints> constraintsPerCtxAtSinks : sinksToConfigConstraints
        .values()) {
      Collection<ExecConstraints> constraintsAtSink = constraintsPerCtxAtSinks.values();

      for (ExecConstraints execConstraints : constraintsAtSink) {
        List<Set<ConfigConstraint>> constraintsPerCtx = execConstraints.getConstraints();

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


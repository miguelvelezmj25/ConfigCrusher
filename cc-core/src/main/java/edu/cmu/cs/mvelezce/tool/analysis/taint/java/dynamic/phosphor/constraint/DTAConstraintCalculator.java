package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.TaintHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DTAConstraintCalculator {

  private final List<String> options;

  public DTAConstraintCalculator(List<String> options) {
    this.options = options;
  }

  public Map<DecisionTaints, Set<ConfigConstraint>> deriveConstraints(
      Set<DecisionTaints> results, Set<String> config) {
    Map<DecisionTaints, Set<ConfigConstraint>> taintsToConstraints = new HashMap<>();

    for (DecisionTaints decisionTaints : results) {
      Set<ConfigConstraint> constraints = deriveConstraints(decisionTaints, config);
      constraints.removeIf(constraint -> constraint.getPartialConfig().isEmpty());
      taintsToConstraints.put(decisionTaints, constraints);
    }

    return taintsToConstraints;
  }

  private Set<ConfigConstraint> deriveConstraints(
      DecisionTaints decisionTaints, Set<String> config) {
    Set<ConfigConstraint> constraints = new HashSet<>();
    Set<String> conditionTaints = TaintHelper.getConditionTaints(decisionTaints, this.options);
    Set<String> contextTaints = TaintHelper.getContextTaints(decisionTaints, this.options);

    Set<String> taintedOptions = new HashSet<>(conditionTaints);
    taintedOptions.addAll(contextTaints);
    Set<String> activatedContextOptions = new HashSet<>(contextTaints);
    activatedContextOptions.retainAll(config);
    Set<String> optionsInCondition = new HashSet<>(conditionTaints);
    optionsInCondition.removeAll(contextTaints);
    Set<Set<String>> combos = Helper.getConfigurations(optionsInCondition);

    for (Set<String> combo : combos) {
      Set<String> activatedTaintOptions = new HashSet<>(activatedContextOptions);
      activatedTaintOptions.addAll(combo);

      Set<String> partialConfig = new HashSet<>();

      for (String taintedOption : taintedOptions) {
        if (activatedTaintOptions.contains(taintedOption)) {
          partialConfig.add(taintedOption);
        }
      }

      ConfigConstraint constraint = ConfigConstraint.fromConfig(partialConfig, taintedOptions);
      constraints.add(constraint);
    }

    return constraints;
  }
}

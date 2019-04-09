package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class PhosphorConstraintCalculator {

  private final List<String> options;

  public PhosphorConstraintCalculator(List<String> options) {
    this.options = options;
  }

  public Set<ConfigConstraint> deriveConstraints(Set<DecisionTaints> results, Set<String> config) {
    Set<ConfigConstraint> constraints = new HashSet<>();

    for (DecisionTaints decisionTaints : results) {
      constraints.addAll(deriveConstraints(decisionTaints, config));
    }

    return constraints;
  }

  private Set<ConfigConstraint> deriveConstraints(DecisionTaints decisionTaints,
      Set<String> config) {
    Set<ConfigConstraint> constraints = new HashSet<>();
    Set<String> conditionTaints = this.getConditionTaints(decisionTaints);
    Set<String> contextTaints = this.getContextTaints(decisionTaints);

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

  private Set<String> getContextTaints(DecisionTaints decisionTaints) {
    @Nullable Taint contextTaintObject = decisionTaints.getExecCtxTaints();
    Set<String> contextTaints = new HashSet<>();

    if (contextTaintObject != null) {
      contextTaints = getTaintingOptions(contextTaintObject);
    }

    return contextTaints;
  }

  private Set<String> getConditionTaints(DecisionTaints decisionTaints) {
    Taint conditionTaintObject = decisionTaints.getConditionTaints();
    return getTaintingOptions(conditionTaintObject);
  }

  private Set<String> getTaintingOptions(Taint taint) {
    Set<String> taintingOptions = new HashSet<>();
    int[] tags = taint.getTags();

    if (tags == null) {
      throw new RuntimeException("You need to use the tags array for tainting");
    }

    if (tags.length > 1) {
      throw new RuntimeException("Implement how to handle array tags with more than 1 entry");
    }

    int tag = tags[0];

    for (int i = 0; tag != 0; i++) {
      if (tag % 2 == 1) {
        String taintingOption = this.options.get(i);
        taintingOptions.add(taintingOption);
      }

      tag = tag >> 1;
    }

    return taintingOptions;
  }

}

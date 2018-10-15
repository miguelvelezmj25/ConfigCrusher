package edu.cmu.cs.mvelezce.tool.analysis.taint.java.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class TaintAnalysis {

  /**
   * Builds a set of pratial configurations
   */
  static Set<Map<String, Boolean>> buildConstraints(Set<String> taintsAtSink) {
    if (taintsAtSink == null || taintsAtSink.isEmpty()) {
      throw new IllegalArgumentException("The taints at sink cannot be empty");
    }

    Set<Map<String, Boolean>> constraints = new HashSet<>();
    Set<Set<String>> configs = Helper.getConfigurations(taintsAtSink);

    for (Set<String> config : configs) {
      Map<String, Boolean> constraint = new HashMap<>();

      for (String taint : taintsAtSink) {
        boolean value = false;

        if (config.contains(taint)) {
          value = true;
        }

        constraint.put(taint, value);
      }

      constraints.add(constraint);
    }

    return constraints;
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Pair<Map<String, Boolean>, Set<String>> buildConfiguration(
      Set<Map<String, Boolean>> constraintsToEvaluate) {
    if (constraintsToEvaluate == null || constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    Map<String, Boolean> constraintToEvaluate = constraintsToEvaluate.iterator().next();
    Set<String> config = completeConfig(constraintToEvaluate);

    return Pair.of(constraintToEvaluate, config);
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Set<String> completeConfig(Map<String, Boolean> constraintToEvaluate) {
    if (constraintToEvaluate == null || constraintToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraint to evaluate should not be empty");
    }

    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : constraintToEvaluate.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

}

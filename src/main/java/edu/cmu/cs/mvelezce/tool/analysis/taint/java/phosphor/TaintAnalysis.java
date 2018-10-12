package edu.cmu.cs.mvelezce.tool.analysis.taint.java.phosphor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class TaintAnalysis {

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Pair<Map<String, Boolean>, Set<String>> buildConfiguration(
      Set<Map<String, Boolean>> constraintsToEvaluate) {
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
    if (constraintToEvaluate.isEmpty()) {
      throw new RuntimeException("The constraint to evaluation should not be empty");
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

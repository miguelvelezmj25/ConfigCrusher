package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Constraint {

  private final Map<String, Boolean> partialConfig;
  private final Set<String> partialConfigAsSet;
  private final Map<String, Boolean> context;

  Constraint(Map<String, Boolean> partialConfig, Map<String, Boolean> context) {
    this.partialConfig = partialConfig;
    this.context = context;

    this.partialConfigAsSet = toConfig(this.partialConfig);
  }

  Constraint(Map<String, Boolean> partialConfig) {
    this(partialConfig, new HashMap<>());
  }

  Map<String, Boolean> getPartialConfig() {
    return partialConfig;
  }

  Map<String, Boolean> getContext() {
    return context;
  }

  public Set<String> getPartialConfigAsSet() {
    return partialConfigAsSet;
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Set<String> toConfig(Map<String, Boolean> constraint) {
    if (constraint.isEmpty()) {
      throw new IllegalArgumentException("The constraint should not be empty");
    }

    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : constraint.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

  static Map<String, Boolean> toConstraint(Set<String> config, Set<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("The options cannot be empty");
    }

    Map<String, Boolean> constraint = new HashMap<>();

    for (String option : options) {
      boolean value = config.contains(option);
      constraint.put(option, value);
    }

    return constraint;
  }

  @Override
  public String toString() {
    return "Constraint{" +
        "partialConfig=" + partialConfig +
        ", context=" + context +
        '}';
  }
}

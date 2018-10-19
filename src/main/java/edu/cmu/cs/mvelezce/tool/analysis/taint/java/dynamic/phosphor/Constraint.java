package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public class Constraint {

  private final Map<String, Boolean> partialConfig;
  private final Map<String, Boolean> context;
//  private final Map<String, Boolean> constraint = new HashMap<>();
//  private final Set<String> partialConfigAsConfig;
//  private final Set<String> constraintAsConfig = new HashSet<>();
//
//  @Nullable
//  private final Set<String> contextAsConfig;

  Constraint(Map<String, Boolean> partialConfig, Map<String, Boolean> context) {
    if (partialConfig.isEmpty()) {
      throw new IllegalArgumentException("The partial config cannot be empty");
    }

    this.partialConfig = partialConfig;
    this.context = context;

//    this.partialConfigAsConfig = toConfig(this.partialConfig);
//
//    if (this.context.isEmpty()) {
//      this.contextAsConfig = null;
//    }
//    else {
//      this.contextAsConfig = toConfig(this.context);
//      this.constraintAsConfig.addAll(this.contextAsConfig);
//    }
//
//    this.constraintAsConfig.addAll(this.partialConfigAsConfig);
//
//    this.constraint.putAll(this.partialConfig);
//    this.constraint.putAll(this.context);
  }

  Constraint(Map<String, Boolean> partialConfig) {
    this(partialConfig, new HashMap<>());
  }

  public Map<String, Boolean> getPartialConfig() {
    return partialConfig;
  }

  public Map<String, Boolean> getContext() {
    return context;
  }

//  public Map<String, Boolean> getConstraint() {
//    return constraint;
//  }
//
//  public Set<String> getPartialConfigAsConfig() {
//    return partialConfigAsConfig;
//  }
//
//  public Set<String> getConstraintAsConfig() {
//    return constraintAsConfig;
//  }
//
//  @Nullable
//  public Set<String> getContextAsConfig() {
//    return contextAsConfig;
//  }

  /**
   * Checks whether the partial configuration can be executed under the condition specified in the
   * context.
   *
   * Example: partialConfig={A=false, B=false} ctx={A=false} is a valid constraint.
   * partialConfig={A=false, B=false} ctx={A=true} is an invalid constraint
   */
  boolean isValid() {
    if (this.context.isEmpty() || this.context.equals(this.partialConfig)) {
      return true;
    }

    for (Map.Entry<String, Boolean> entry : this.context.entrySet()) {
      String option = entry.getKey();

      if (!this.partialConfig.containsKey(option)) {
        continue;
      }

      boolean partialConfigValue = this.partialConfig.get(option);
      boolean value = entry.getValue();

      if (partialConfigValue != value) {
        return false;
      }
    }

    return true;
  }

  boolean isSubsetOf(Constraint constraint) {
    return constraint.getConstraint().entrySet().containsAll(this.getConstraint().entrySet());
  }

  Map<String, Boolean> getConstraint() {
    Map<String, Boolean> constraint = new HashMap<>();
    constraint.putAll(this.partialConfig);
    constraint.putAll(this.context);

    return constraint;
  }

  Set<String> getConstraintAsConfig() {
    Set<String> config = new HashSet<>(toConfig(this.partialConfig));

    if (!this.context.isEmpty()) {
      config.addAll(toConfig(this.context));
    }

    return config;
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Set<String> toConfig(Map<String, Boolean> partialConfig) {
    if (partialConfig.isEmpty()) {
      throw new IllegalArgumentException("The partial config should not be empty");
    }

    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : partialConfig.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

  static Map<String, Boolean> toPartialConfig(Set<String> config, Set<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("The options cannot be empty");
    }

    if (!options.containsAll(config)) {
      throw new IllegalArgumentException(
          "The config " + config + " is not a subset of the options " + options);
    }

    Map<String, Boolean> partialConfig = new HashMap<>();

    for (String option : options) {
      partialConfig.put(option, config.contains(option));
    }

    return partialConfig;
  }

  static Map<String, Boolean> buildContext(@Nullable Set<String> taintsFromContext,
      Set<String> config) {
    Map<String, Boolean> context = new HashMap<>();

    if (taintsFromContext == null || taintsFromContext.isEmpty()) {
      return context;
    }

    for (String taint : taintsFromContext) {
      context.put(taint, config.contains(taint));
    }

    return context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Constraint that = (Constraint) o;

    if (!partialConfig.equals(that.partialConfig)) {
      return false;
    }
    return context.equals(that.context);
  }

  @Override
  public int hashCode() {
    int result = partialConfig.hashCode();
    result = 31 * result + context.hashCode();
    return result;
  }

  @Override
  public String toString() {
    String partialConfig = "{}";

    if (!this.partialConfig.isEmpty()) {
      partialConfig = this.partialConfig.toString();
    }

    String context = "True";

    if (!this.context.isEmpty()) {
      context = this.context.toString();
    }

    return "Constraint{" +
        "partialConfig=" + partialConfig +
        ", ctx=" + context +
        '}';
  }
}

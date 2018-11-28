package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public class Constraint {

  private final Map<String, Boolean> partialConfig;
  private final Map<String, Boolean> ctx;

  // Dummy constructor needed for jackson xml
  private Constraint() {
    this.partialConfig = new HashMap<>();
    this.ctx = new HashMap<>();
  }

  Constraint(Map<String, Boolean> partialConfig, Map<String, Boolean> ctx) {
    this.partialConfig = partialConfig;
    this.ctx = ctx;
  }

  Constraint(Map<String, Boolean> partialConfig) {
    this(partialConfig, new HashMap<>());
  }

  public Map<String, Boolean> getCtx() {
    return ctx;
  }

  public Map<String, Boolean> getPartialConfig() {
    return partialConfig;
  }

  /**
   * Checks whether the partial configuration can be executed under the condition specified in the
   * ctx.
   *
   * Example: partialConfig={A=false, B=false} ctx={A=false} is a valid constraint.
   * partialConfig={A=false, B=false} ctx={A=true} is an invalid constraint
   */
  boolean isValid() {
    if (this.ctx.isEmpty() || this.ctx.equals(this.partialConfig)) {
      return true;
    }

    for (Map.Entry<String, Boolean> entry : this.ctx.entrySet()) {
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
    Set<Entry<String, Boolean>> constraintAsConstraint = constraint.getCompleteConstraint()
        .entrySet();
    Set<Entry<String, Boolean>> thisAsConstraint = this.getCompleteConstraint().entrySet();

    return constraintAsConstraint.containsAll(thisAsConstraint);
  }

  boolean isEqualTo(Constraint constraint) {
    Set<Entry<String, Boolean>> constraintAsConstraint = constraint.getCompleteConstraint()
        .entrySet();
    Set<Entry<String, Boolean>> thisAsConstraint = this.getCompleteConstraint().entrySet();

    return constraintAsConstraint.equals(thisAsConstraint);
  }

  Map<String, Boolean> getCompleteConstraint() {
    Map<String, Boolean> constraint = new HashMap<>();
    constraint.putAll(this.partialConfig);
    constraint.putAll(this.ctx);

    return constraint;
  }

  Set<String> getConstraintAsPartialConfig() {
    Set<String> config = new HashSet<>(toPartialCCConfig(this.partialConfig));

    if (!this.ctx.isEmpty()) {
      config.addAll(toPartialCCConfig(this.ctx));
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
  static Set<String> toPartialCCConfig(Map<String, Boolean> partialConfig) {
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

  public static Map<String, Boolean> toConfigWithValues(Set<String> config, Set<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("The options cannot be empty");
    }

    if (!options.containsAll(config)) {
      throw new IllegalArgumentException(
          "The config " + config + " is not a subset of the options " + options);
    }

    Map<String, Boolean> configWithValues = new HashMap<>();

    for (String option : options) {
      configWithValues.put(option, config.contains(option));
    }

    return configWithValues;
  }

  static Set<Map<String, Boolean>> buildPartialConfigs(@Nullable Set<String> taintsAtSink) {
    Set<Map<String, Boolean>> partialConfigs = new HashSet<>();

    if (taintsAtSink == null || taintsAtSink.isEmpty()) {
      return partialConfigs;
    }

    Set<Set<String>> configs = Helper.getConfigurations(taintsAtSink);

    for (Set<String> config : configs) {
      Map<String, Boolean> partialConfig = new HashMap<>();

      for (String taint : taintsAtSink) {
        boolean value = config.contains(taint);
        partialConfig.put(taint, value);
      }

      partialConfigs.add(partialConfig);
    }

    return partialConfigs;
  }

  static Map<String, Boolean> buildCtx(@Nullable Set<String> taintsFromCtx,
      Set<String> config) {
    Map<String, Boolean> ctx = new HashMap<>();

    if (taintsFromCtx == null || taintsFromCtx.isEmpty()) {
      return ctx;
    }

    for (String taint : taintsFromCtx) {
      ctx.put(taint, config.contains(taint));
    }

    return ctx;
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
    return ctx.equals(that.ctx);
  }

  @Override
  public int hashCode() {
    int result = partialConfig.hashCode();
    result = 31 * result + ctx.hashCode();
    return result;
  }

  @Override
  public String toString() {
    String partialConfig = "{}";

    if (!this.partialConfig.isEmpty()) {
      partialConfig = this.partialConfig.toString();
    }

    String ctx = "True";

    if (!this.ctx.isEmpty()) {
      ctx = this.ctx.toString();
    }

    return "Constraint{" +
        "partialConfig=" + partialConfig +
        ", ctx=" + ctx +
        '}';
  }
}

package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.Map;
import java.util.Set;

public class ConfigSubtraceValueInfo {

  private Set<String> config;
  private Map<String, String> subtracesToValues;

  // Dummy constructor needed for jackson xml
  private ConfigSubtraceValueInfo() {
  }

  public ConfigSubtraceValueInfo(Set<String> config, Map<String, String> subtracesToValues) {
    this.config = config;
    this.subtracesToValues = subtracesToValues;
  }

  public Set<String> getConfig() {
    return config;
  }

  public Map<String, String> getSubtracesToValues() {
    return subtracesToValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigSubtraceValueInfo that = (ConfigSubtraceValueInfo) o;

    if (!config.equals(that.config)) {
      return false;
    }
    return subtracesToValues.equals(that.subtracesToValues);
  }

  @Override
  public int hashCode() {
    int result = config.hashCode();
    result = 31 * result + subtracesToValues.hashCode();
    return result;
  }
}

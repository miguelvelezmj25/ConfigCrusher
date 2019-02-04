package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.Map;
import java.util.Set;

public class ConfigLabelValueInfo {

  private Set<String> config;
  private Map<String, String> labelsToValues;

  // Dummy constructor needed for jackson xml
  private ConfigLabelValueInfo() {
  }

  public ConfigLabelValueInfo(Set<String> config, Map<String, String> labelsToValues) {
    this.config = config;
    this.labelsToValues = labelsToValues;
  }

  public Set<String> getConfig() {
    return config;
  }

  public Map<String, String> getLabelsToValues() {
    return labelsToValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigLabelValueInfo that = (ConfigLabelValueInfo) o;

    if (!config.equals(that.config)) {
      return false;
    }
    return labelsToValues.equals(that.labelsToValues);
  }

  @Override
  public int hashCode() {
    int result = config.hashCode();
    result = 31 * result + labelsToValues.hashCode();
    return result;
  }
}

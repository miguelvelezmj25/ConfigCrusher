package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PartialConfig {

  private Map<String, Boolean> partialConfig = new HashMap<>();

  void addEntry(String option, boolean value) {
    if(!this.partialConfig.isEmpty()) {
      throw new UnsupportedOperationException("Implement");
    }

    this.partialConfig.put(option, value);
  }

  Set<String> getOptions() {
    return this.partialConfig.keySet();
  }

  protected Map<String, Boolean> getPartialConfig() {
    return partialConfig;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PartialConfig that = (PartialConfig) o;

    return partialConfig.equals(that.partialConfig);
  }

  @Override
  public int hashCode() {
    return partialConfig.hashCode();
  }

  @Override
  public String toString() {
    return this.partialConfig.toString();
  }
}

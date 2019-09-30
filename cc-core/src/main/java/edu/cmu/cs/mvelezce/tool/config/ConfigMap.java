package edu.cmu.cs.mvelezce.tool.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigMap {

  private final Map<String, Boolean> config = new HashMap<>();

  public boolean addOption(String option, boolean value) {
    return this.config.put(option, value);
  }

  public void addOptions(Map<String, Boolean> config) {
    this.config.putAll(config);
  }

}

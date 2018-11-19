package edu.cmu.cs.mvelezce.tool.config;

import java.util.HashSet;
import java.util.Set;

public class ConfigSet implements Config {

  private final Set<String> config = new HashSet<>();

  public void addOption(String option) {
    this.config.add(option);
  }

  public void addOptions(Set<String> options) {
    this.config.addAll(options);
  }

}

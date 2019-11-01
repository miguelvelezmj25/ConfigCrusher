package edu.cmu.cs.mvelezce.adapter.adapters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseAdapter implements Adapter {

  private String programName;
  private String directory;
  private List<String> options;

  public BaseAdapter(String programName, String directory, List<String> options) {
    this.programName = programName;
    this.directory = directory;
    this.options = options;
  }

  @Override
  public Set<String> configurationAsSet(String[] configuration) {
    Set<String> performanceConfiguration = new HashSet<>();

    for (int i = 0; i < configuration.length; i++) {
      if (configuration[i].equals("true")) {
        performanceConfiguration.add(this.options.get(i));
      }
    }

    return performanceConfiguration;
  }

  @Override
  public String[] configurationAsMainArguments(Set<String> configuration) {
    String[] sleepConfiguration = new String[this.options.size()];

    for (int i = 0; i < sleepConfiguration.length; i++) {
      if (configuration.contains(this.options.get(i))) {
        sleepConfiguration[i] = "true";
      } else {
        sleepConfiguration[i] = "false";
      }
    }

    return sleepConfiguration;
  }

  public String getProgramName() {
    return programName;
  }

  public String getDirectory() {
    return directory;
  }

  public List<String> getOptions() {
    return options;
  }
}

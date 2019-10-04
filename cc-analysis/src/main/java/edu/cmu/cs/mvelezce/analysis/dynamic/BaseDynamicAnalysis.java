package edu.cmu.cs.mvelezce.analysis.dynamic;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.utils.Options;

import java.util.Set;

public abstract class BaseDynamicAnalysis<T> extends BaseAnalysis<T> implements DynamicAnalysis<T> {

  protected static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/dynamic/programs";

  private final Set<String> options;
  private final Set<String> initialConfig;

  public BaseDynamicAnalysis(String programName, Set<String> options, Set<String> initialConfig) {
    super(programName);

    this.options = options;
    this.initialConfig = initialConfig;
  }

  public Set<String> getOptions() {
    return options;
  }

  public Set<String> getInitialConfig() {
    return initialConfig;
  }
}

package edu.cmu.cs.mvelezce.eval.java.blackbox.results;

import java.util.HashSet;
import java.util.Set;

public class BlackBoxResult {

  private final Set<String> configuration;
  private final long time;

  // Dummy constructor for jackson xml
  private BlackBoxResult() {
    this.configuration = new HashSet<>();
    this.time = 0;
  }

  public BlackBoxResult(Set<String> configuration, long time) {
    this.configuration = configuration;
    this.time = time;
  }

  public Set<String> getConfiguration() {
    return configuration;
  }

  public long getTime() {
    return time;
  }
}

package edu.cmu.cs.mvelezce.java.results.sampling.raw;

import java.util.HashSet;
import java.util.Set;

public class Hotspot extends JProfilerHotspot {

  private final Set<String> configuration;

  // Dummy constructor for jackson
  public Hotspot() {
    super();

    this.configuration = new HashSet<>();
  }

  public Hotspot(
      String leaf,
      String className,
      String methodName,
      String methodSignature,
      long time,
      int count,
      int lineNumber,
      double percent,
      Set<String> configuration) {
    super(leaf, className, methodName, methodSignature, time, count, lineNumber, percent);

    this.configuration = configuration;
  }

  public String getClassName() {
    return super.getClassName();
  }
}

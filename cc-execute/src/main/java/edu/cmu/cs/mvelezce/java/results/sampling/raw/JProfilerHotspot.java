package edu.cmu.cs.mvelezce.java.results.sampling.raw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JProfilerHotspot {

  private final String leaf;
  private final String className;
  private final String methodName;
  private final String methodSignature;
  private final long time;
  private final int count;
  private final int lineNumber;
  private final double percent;

  // Dummy constructor for jackson
  public JProfilerHotspot() {
    this.leaf = "";
    this.className = "";
    this.methodName = "";
    this.methodSignature = "";
    this.time = 0;
    this.count = 0;
    this.lineNumber = 0;
    this.percent = 0;
  }

  public JProfilerHotspot(
      String leaf,
      String className,
      String methodName,
      String methodSignature,
      long time,
      int count,
      int lineNumber,
      double percent) {
    this.leaf = leaf;
    this.className = className;
    this.methodName = methodName;
    this.methodSignature = methodSignature;
    this.time = time;
    this.count = count;
    this.lineNumber = lineNumber;
    this.percent = percent;
  }

  public String getLeaf() {
    return leaf;
  }

  @JsonProperty("class")
  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getMethodSignature() {
    return methodSignature;
  }

  public long getTime() {
    return time;
  }

  public int getCount() {
    return count;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public double getPercent() {
    return percent;
  }
}

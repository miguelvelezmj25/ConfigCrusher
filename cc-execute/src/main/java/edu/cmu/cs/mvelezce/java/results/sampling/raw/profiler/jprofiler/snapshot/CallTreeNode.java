package edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CallTreeNode {

  private final boolean leaf;
  private final String className;
  private final String methodName;
  private final String methodSignature;
  private final long time;
  private final int count;
  private final long selfTime;
  private final int lineNumber;
  private final double percent;

  public CallTreeNode(
      boolean leaf,
      String className,
      String methodName,
      String methodSignature,
      long time,
      int count,
      long selfTime,
      int lineNumber,
      double percent) {
    this.leaf = leaf;
    this.className = className;
    this.methodName = methodName;
    this.methodSignature = methodSignature;
    this.time = time;
    this.count = count;
    this.selfTime = selfTime;
    this.lineNumber = lineNumber;
    this.percent = percent;
  }

  public CallTreeNode() {
    this.leaf = false;
    this.className = "";
    this.methodName = "";
    this.methodSignature = "";
    this.time = -1;
    this.count = -1;
    this.selfTime = -1;
    this.lineNumber = -1;
    this.percent = -1;
  }

  public boolean getLeaf() {
    return leaf;
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

  public long getSelfTime() {
    return selfTime;
  }

  @JsonProperty("class")
  public String getClassName() {
    return className;
  }
}

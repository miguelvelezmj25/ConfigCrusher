package edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Hotspot {

  private final boolean leaf;
  private final String className;
  private final String methodName;
  private final String methodSignature;
  private final long time;
  private final int count;
  private final int lineNumber;
  private final double percent;
  private final List<Node> node;

  // Dummy constructor for jackson xml
  private Hotspot() {
    this.leaf = false;
    this.className = "";
    this.methodName = "";
    this.methodSignature = "";
    this.time = 0;
    this.count = 0;
    this.lineNumber = 0;
    this.percent = 0.0;
    this.node = new ArrayList<>();
  }

  public Hotspot(
      boolean leaf,
      String className,
      String methodName,
      String methodSignature,
      long time,
      int count,
      int lineNumber,
      double percent,
      List<Node> node) {
    this.leaf = leaf;
    this.className = className;
    this.methodName = methodName;
    this.methodSignature = methodSignature;
    this.time = time;
    this.count = count;
    this.lineNumber = lineNumber;
    this.percent = percent;
    this.node = node;
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

  public List<Node> getNode() {
    return node;
  }

  @JsonProperty("class")
  public Object getClassName() {
    return className;
  }
}

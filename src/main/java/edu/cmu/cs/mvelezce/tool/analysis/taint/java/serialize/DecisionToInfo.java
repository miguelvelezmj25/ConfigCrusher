package edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

public class DecisionToInfo<T> {

  private JavaRegion region;
  private T info;

  private DecisionToInfo() {
    ;
  }

  public DecisionToInfo(JavaRegion region, T info) {
    this.region = region;
    this.info = info;
  }

  public JavaRegion getRegion() {
    return region;
  }

  public void setRegion(JavaRegion region) {
    this.region = region;
  }

  public T getInfo() {
    return info;
  }

  public void setInfo(T info) {
    this.info = info;
  }

}

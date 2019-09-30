package edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

public class RegionToInfo<T> {

  private JavaRegion region;
  private T info;

  private RegionToInfo() {
    ;
  }

  public RegionToInfo(JavaRegion region, T info) {
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

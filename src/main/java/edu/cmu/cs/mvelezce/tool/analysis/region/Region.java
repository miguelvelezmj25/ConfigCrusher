package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.UUID;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Region {

  private final String regionID;

  private long startTime;
  private long endTime;
  private long duration;

  // Needed for saving and reading regions in json
  protected Region() {
    this.regionID = null;
  }

  protected Region(Builder builder) {
    this.regionID = builder.regionID;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
    this.duration = builder.duration;
  }

  public void startTime(long startTime) {
    this.startTime = startTime;
  }

  public void endTime(long endTime) {
    this.endTime = endTime;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public String getRegionID() {
    return regionID;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getEndTime() {
    return this.endTime;
  }

  public long getDuration() {
    return duration;
  }

  @Override
  public String toString() {
    return "Region{" +
        "regionID='" + regionID + '\'' +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", duration=" + duration +
        '}';
  }

  public void enter() {
    this.startTime();
  }

  public void startTime() {
    this.startTime(System.nanoTime());
  }

  public void enter(long startTime) {
    this.startTime(startTime);
  }

  public void exit() {
    this.endTime();
  }

  public void endTime() {
    this.endTime(System.nanoTime());
  }

  public void exit(long endTime) {
    this.endTime(endTime);
  }

  public static class Builder {

    private final String regionID;

    private long startTime = -1;
    private long endTime = -1;
    private long duration = -1;

    public Builder() {
      this(UUID.randomUUID().toString());
    }

    public Builder(String regionID) {
      this.regionID = regionID;
    }

    public Builder startTime(long startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder endTime(long endTime) {
      this.endTime = endTime;
      return this;
    }

    public Builder duration(long duration) {
      this.duration = duration;
      return this;
    }

    public Region build() {
      return new Region(this);
    }
  }
}

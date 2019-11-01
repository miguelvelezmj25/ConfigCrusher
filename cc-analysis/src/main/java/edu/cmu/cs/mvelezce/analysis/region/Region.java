package edu.cmu.cs.mvelezce.analysis.region;

import java.util.UUID;

public class Region {

  private final UUID id;

  private long startTime;
  private long endTime;
  private long duration;

  // Dummy constructor for faster xml
  protected Region() {
    this.id = UUID.randomUUID();
  }

  protected Region(Builder builder) {
    this.id = builder.id;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
    this.duration = builder.duration;
  }

  public UUID getId() {
    return id;
  }

  //  public UUID getId() {
  //    return id;
  //  }
  //
  //  public long getStartTime() {
  //    return startTime;
  //  }
  //
  //  public long getEndTime() {
  //    return endTime;
  //  }
  //
  //  public long getDuration() {
  //    return duration;
  //  }

  //  public void startTime(long startTime) {
  //    this.startTime = startTime;
  //  }
  //
  //  public void endTime(long endTime) {
  //    this.endTime = endTime;
  //  }
  //
  //  public UUID getId() {
  //    return id;
  //  }
  //
  //  public long getStartTime() {
  //    return this.startTime;
  //  }
  //
  //  public long getEndTime() {
  //    return this.endTime;
  //  }
  //
  //  public long getDuration() {
  //    return duration;
  //  }
  //
  //  public void setDuration(long duration) {
  //    this.duration = duration;
  //  }
  //
  //  public void enter() {
  //    this.startTime();
  //  }
  //
  //  public void startTime() {
  //    this.startTime(System.nanoTime());
  //  }
  //
  //  public void enter(long startTime) {
  //    this.startTime(startTime);
  //  }
  //
  //  public void exit() {
  //    this.endTime();
  //  }
  //
  //  public void endTime() {
  //    this.endTime(System.nanoTime());
  //  }
  //
  //  public void exit(long endTime) {
  //    this.endTime(endTime);
  //  }

  public static class Builder {

    private final UUID id;

    private long startTime = -1;
    private long endTime = -1;
    private long duration = -1;

    protected Builder() {
      this(UUID.randomUUID());
    }

    public Builder(UUID id) {
      this.id = id;
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

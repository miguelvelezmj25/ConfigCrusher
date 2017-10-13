package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.UUID;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Region {

    private String regionID;
    private long startTime = -1;
    private long endTime = -1;
    private long duration = -1;

    public Region(String regionID, long startTime, long endTime) {
        this.regionID = regionID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime - startTime;
    }

    public Region(String regionID) {
        this.regionID = regionID;
    }

    public Region() {
        this(UUID.randomUUID().toString());
    }

//    private void enterRegion() {
//        Regions.addExecutingRegion(this);
//    }

    public void enter() {
//        this.enterRegion();
        this.startTime();
    }

    public void enter(long startTime) {
//        this.enterRegion();
        this.startTime(startTime);
    }

    public void exit() {
        this.endTime();
//        Regions.removeExecutingRegion(this);
    }

    public void exit(long endTime) {
        this.endTime(endTime);
//        Regions.removeExecutingRegion(this);
    }

    public void startTime() {
        this.startTime(System.nanoTime());
    }

    public void startTime(long startTime) {
        this.startTime = startTime;
    }

    public void endTime() {
        this.endTime(System.nanoTime());
    }

    public void endTime(long endTime) {
        this.endTime = endTime;
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

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

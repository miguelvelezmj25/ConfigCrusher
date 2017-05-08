package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.UUID;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Region {

    private String regionID;
    private long startTime;
    private long endTime;

    public Region(String regionID, long startTime, long endTime) {
        this.regionID = regionID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Region(String regionID) {
        this(regionID, 0, 0);
    }

    public Region() {
        this(UUID.randomUUID().toString());
    }

    public static long getExecutionTime(long startTime, long endTime) {
        return endTime - startTime;
    }

    public static double getMilliExecutionTime(long startTime, long endTime) {
        return Region.getExecutionTime(startTime, endTime)/1000000.0;
    }

    public static double getSecondsExecutionTime(long startTime, long endTime) {
        return Region.getMilliExecutionTime(startTime, endTime)/1000.0;
    }

    public static long toNanoTime(int time) {
        return ((long) time) * 1000000000;
    }

    private void enterRegion() {
        Regions.addExecutingRegion(this);
    }

    public void enter() {
        this.enterRegion();
        this.startTime();
    }

    public void enter(long startTime) {
        this.enterRegion();
        this.startTime(startTime);
    }

    public void exit() {
        this.endTime();
        Regions.removeExecutingRegion(this);
    }

    public void exit(long endTime) {
        this.endTime(endTime);
        Regions.removeExecutingRegion(this);
    }

    public void startTime() {
        this.startTime(System.nanoTime());
    }

    public void startTime(long startTime) {
        this.resetExecution();
        this.startTime = startTime;
    }

    public void endTime() {
        this.endTime(System.nanoTime());
    }

    public void endTime(long endTime) {
        this.endTime = endTime;
    }

    public void resetExecution() {
        this.startTime = 0;
        this.endTime = 0;
    }

    public void resetState() {
        this.resetExecution();
    }

    @Override
    public String toString() {
        return "Region{" +
                "regionID='" + regionID + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public String getRegionID() { return this.regionID; }

    public long getStartTime() { return this.startTime; }

    public long getEndTime() { return this.endTime; }
}

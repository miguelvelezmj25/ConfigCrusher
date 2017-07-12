package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.UUID;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Region {

    private String regionID;
    private long startTime;
    private long endTime;
    private long overhead = 0;

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
        this.overhead = System.nanoTime();
        this.enterRegion();
        this.startTime();
        this.overhead = System.nanoTime() - this.overhead;
    }

    public void enter(long startTime) {
        this.enterRegion();
        this.startTime(startTime);
    }

    public void exit() {
        this.endTime();
        this.overhead = System.nanoTime();
        Regions.removeExecutingRegion(this);
        this.overhead = System.nanoTime() - this.overhead;
    }

    public void exit(long endTime) {
        this.endTime(endTime);
        Regions.removeExecutingRegion(this);
    }

    public void startTime() {
        this.startTime(System.nanoTime());
    }

    public void startTime(long startTime) {
//        this.resetExecution();
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
                ", overhead=" + this.overhead +
                '}';
    }

    public String getRegionID() { return this.regionID; }

    public long getStartTime() { return this.startTime; }

    public long getEndTime() { return this.endTime; }

    public long getOverhead() { return this.overhead; }
}

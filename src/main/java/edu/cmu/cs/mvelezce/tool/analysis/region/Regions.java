package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {

    public static final String PROGRAM_REGION_ID = "program";
    public static int startCount = 0;
    public static int endCound = 0;
    private static List<Region> executedRegionsTrace = new ArrayList<>(1_000_000);

    public static void main(String[] args) {
        System.out.println("Started");
        long start = System.nanoTime();

        for(int i = 0; i < 120_000_000; i++) {
            Regions.enter("dsf");
            Regions.exit("dsf");
        }

        long end = System.nanoTime();
        long time = end - start;

        System.out.println("start count " + Regions.startCount);
        System.out.println("end count " + Regions.endCound);
        System.out.println(time / 1000000000.0);
    }

    public static void enter(String regionID) {
        Region region = new Region(regionID);
        region.enter();

        Regions.addExecutingRegion(region);
        Regions.startCount++;
    }

    public static void exit(String regionID) {
        Region region = new Region(regionID);
        region.exit();

        Regions.removeExecutingRegion(region);
        Regions.endCound++;
    }

    public static void addExecutingRegion(Region region) {
        Regions.executedRegionsTrace.add(region);
    }

    public static void removeExecutingRegion(Region region) {
        Regions.executedRegionsTrace.add(region);
    }

    public static List<Region> getExecutedRegionsTrace() {
        return Regions.executedRegionsTrace;
    }
}

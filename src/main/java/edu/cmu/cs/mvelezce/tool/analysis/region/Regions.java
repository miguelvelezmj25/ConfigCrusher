package edu.cmu.cs.mvelezce.tool.analysis.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {

    public static final String PROGRAM_REGION_ID = "program";

//    private static Set<Region> regions = new HashSet<>();
//    private static Set<Region> regions = new HashSet<Region>() {
//    @Override
//    public boolean add(Region region) {
//        return false;
//    }
//};

    private static List<Region> executedRegionsTrace = new ArrayList<>(1_000_000);
    private static Stack<Region> executingRegions = new Stack<>();

//    public static void addRegion(Region region) {
////        if(region == null) {
////            throw new IllegalArgumentException("Region cannot be null");
////        }
//
//        Regions.regions.add(region);
//    }

//    /**
//     * Get a region by id where the location is not important
//     * @param regionID
//     * @return
//     */
//    public static Region getRegion(String regionID) {
//        if(regionID == null) {
//            throw new IllegalArgumentException("RegionID cannot be null");
//        }
//
//        for(Region entry : Regions.regions) {
//            if(entry.getRegionID().equals(regionID)) {
//                return entry;
//            }
//        }
//
//        Region region = new Region(regionID);
//        Regions.addRegion(region);
//
//        return region;
//    }

//    /**
//     * Getting a region that uses the context of the region rather than the ID
//     * @param region
//     *
//     * @return
//     */
//    // TODO this is only used for testing and in the Sleep pipeline
//    public static Region getRegion(Region region) {
//        if(region == null) {
//            throw new IllegalArgumentException("Region cannot be null");
//        }
//
//        for(Region entry : Regions.regions) {
//            if(entry.equals(region)) {
//                return entry;
//            }
//        }
//
//        return null;
//    }

    public static void enter(String regionID) {
        Region region = new Region(regionID);
//        Regions.addRegion(region);
        region.enter();
    }

    // TODO hacky way to not call the exit method of a region if it does not exit. This can be fixed if we can instrument
    // better and do not exit a region that has not been started
    public static void exit(String regionID) {
        if(Regions.executingRegions.peek().getRegionID().equals(regionID)) {
            Region region = new Region(regionID);
            region.exit();
//            Regions.addRegion(region);
        }
//        boolean exit = false;
//
//        for(Region region : Regions.executedRegionsTrace) {
//            if(region.getRegionID().equals(regionID)) {
//                exit = true;
//                break;
//            }
//        }
//
//        if(exit) {
//            Region region = new Region(regionID);
//            region.exit();
//            Regions.addRegion(region);
//        }
    }

    public static void addExecutingRegion(Region region) {
        Regions.executedRegionsTrace.add(region);
        Regions.executingRegions.push(region);
    }

//    public static Region getExecutingRegion() {
//        return Regions.executingRegions.peek();
//    }

    // TODO this is hacking and not how it is supposed to be working due to problems with how we end instrumenting inner regions
    public static void removeExecutingRegion(Region region) {
//        System.out.println("exit " + region.getRegionID());
//        for(Region r : executingRegions) {
//            System.out.println(r.getRegionID());
//        }
//        if(!Regions.executingRegions.peek().getRegionID().equals(region.getRegionID())) {
//            return;
//        }

        Region executing = Regions.executingRegions.pop();
        // TODO this is for testing that the region that believes to have executed is the one that was executing
        if(!region.getRegionID().equals(executing.getRegionID())) {
            throw new RuntimeException("The region that wanted to be removed " + region.getRegionID() + " from the executing regions is not the last region " +
                    "to be executing " + executing.getRegionID());
        }

        Regions.executedRegionsTrace.add(region);
//        System.out.println(" ");
    }

    public static List<Region> getExecutedRegionsTrace() { return Regions.executedRegionsTrace; }

//    public static Set<Region> getRegions() {
//        return Regions.regions;
//    }

    public static Stack<Region> getExecutingRegions() { return Regions.executingRegions; }
}

package edu.cmu.cs.mvelezce.tool.analysis;

import java.util.*;

/**
 * Created by miguelvelez on 4/7/17.
 */
// TODO use types?
// TODO this is only used when the program is executing
public class Regions {
    private static Region program = null;
    private static Set<Region> regions = new HashSet<>();
    private static List<Region> executedRegionsTrace = new LinkedList<>();
    private static Stack<Region> executingRegions = new Stack<>();

    public static void addProgram(Region program) {
        if(program == null) {
            throw new IllegalArgumentException("Program cannot be null");
        }

        Regions.program = program;
    }

    public static void removeProgram() {
        Regions.program = null;
    }

    public static void addRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        // TODO there used to be a clone method here
        Regions.regions.add(region);
    }

    public static void removeRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        Regions.regions.remove(region);
    }

    /**
     * Get a region by id where the location is not important
     * @param regionID
     * @return
     */
    public static Region getRegion(String regionID) {
        if(regionID == null) {
            throw new IllegalArgumentException("RegionID cannot be null");
        }

        for(Region entry : Regions.regions) {
            if(entry.getRegionID().equals(regionID)) {
                return entry;
            }
        }

        Region region = new Region(regionID);
        Regions.addRegion(region);

        return region;
    }

    /**
     * Getting a region that uses the context of the region rather than the ID
     * @param region
     *
     * @return
     */
    // TODO this is only used for testing and in the Sleep pipeline
    public static Region getRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        for(Region entry : Regions.regions) {
            if(entry.equals(region)) {
                return entry;
            }
        }

        return null;
    }

    public static void enter(String regionID) {
        Region region = new Region(regionID);
        Regions.addRegion(region);
        region.enter();
    }

    // TODO hacky way to not call the exit method of a region if it does not exit. This can be fixed if we can instrument
    // better and do not exit a region that has not been started
    public static void exit(String regionID) {
        boolean exit = false;

        for(Region region : Regions.executedRegionsTrace) {
            if(region.getRegionID().equals(regionID)) {
                exit = true;
                break;
            }
        }

        if(exit) {
            Region region = new Region(regionID);
            Regions.addRegion(region);
            region.exit();
        }
    }

    public static void addExecutingRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("The region cannot be null");
        }
        System.out.println("enter " + region.getRegionID());

        Regions.executedRegionsTrace.add(region);
        Regions.executingRegions.push(region);
    }

    public static Region getExecutingRegion() {
        return Regions.executingRegions.peek();
    }

    public static void removeExecutingRegion(Region region) {
        System.out.println("exit " + region.getRegionID());
        Region executing = Regions.executingRegions.pop();
//        // TODO this is for testing that the region that believes to have executed is the one that was executing
//        if(!region.equals(executing)) {
//            throw new RuntimeException("The region that wanted to be removed from the executing regions is not the last region " +
//                    "to be executing");
//        }

        Regions.executedRegionsTrace.add(region);
    }

    public static void resetRegions() {
        // TODO do we need to use this?
        for(Region region : Regions.regions) {
            region.resetState();
        }

        Regions.program.resetState();
    }

//    public static Map<Region, Set<String>> getOptionsInRegionsWithInnerRegions(Map<Region, Set<String>> regionsToOptions) {
//        Map<Region, Set<String>> regionsToInvolvedOptions = new HashMap<>();
//
//        Set<Region> programInnerRegions = Regions.regionsToInnerRegions.get(Regions.program);
//
//        if(programInnerRegions != null) {
//            for(Region programInnerRegion : programInnerRegions) {
//                Regions.calculateOptionsOfRegionsWithPossibleInnerRegions(programInnerRegion, regionsToOptions, regionsToInvolvedOptions);
//            }
//        }
//
//        // The program is not a region in the sense that it has options affecting it
//        Regions.regionsToInnerRegions.remove(Regions.program);
//
//        return regionsToInvolvedOptions;
//    }
//
//    private static void calculateOptionsOfRegionsWithPossibleInnerRegions(Region region, Map<Region, Set<String>> regionsToOptions, Map<Region, Set<String>> result) {
//        if(!result.containsKey(region)) {
//            Set<String> allAffectingOptions = regionsToOptions.get(region);
//            Set<Region> innerRegions = Regions.regionsToInnerRegions.get(region);
//
//            if(!innerRegions.isEmpty()) {
//                for(Region innerRegion : Regions.regionsToInnerRegions.get(region)) {
//                    Regions.calculateOptionsOfRegionsWithPossibleInnerRegions(innerRegion, regionsToOptions, result);
//                }
//
//                for(Region innerRegion : Regions.regionsToInnerRegions.get(region)) {
//                    allAffectingOptions.addAll(result.get(innerRegion));
//                }
//            }
//
//            result.put(region, allAffectingOptions);
//        }
//    }

    public static void reset() {
        Regions.removeProgram();
        Regions.regions = new HashSet<>();
        Regions.executingRegions = new Stack<>();
    }

    public static List<Region> getExecutedRegionsTrace() { return Regions.executedRegionsTrace; }

    public static Region getProgram() {
        return Regions.program;
    }

    public static Set<Region> getRegions() {
        return Regions.regions;
    }

    public static Stack<Region> getExecutingRegions() { return Regions.executingRegions; }
}

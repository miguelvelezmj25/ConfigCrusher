package edu.cmu.cs.mvelezce.tool.analysis;

import java.util.*;

/**
 * Created by miguelvelez on 4/7/17.
 */
// TODO use types
public class Regions {
    private static Region program = null;
    private static Set<Region> regions = new HashSet<>();
    private static Stack<Region> executingRegions = new Stack<>();
    private static Map<Region, Set<Region>> regionsToAllPossibleInnerRegions = new HashMap<>();

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

        Regions.regions.add(region.clone());
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

        // TODO this is were a new region should be created if it is not found. But how do you link this region to options, which was found before?
        for(Region entry : Regions.regions) {
            if(entry.getRegionID().equals(regionID)) {
                return entry;
            }
        }

        return null;
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

    /**
     * Adds an inner region to a region. This track the inner regions of an specific region regardless of the configuration
     * that was executed. Essentially it has all possible inner regions of a region.
     * @param parent
     * @param child
     */
    public static void addPossibleInnerRegion(Region parent, Region child) {
        if(parent == null) {
            throw new IllegalArgumentException("The parent region cannot be null");
        }

        if(child == null) {
            throw new IllegalArgumentException("The child region cannot be null");
        }

        if(!Regions.regionsToAllPossibleInnerRegions.containsKey(parent)) {
            Regions.regionsToAllPossibleInnerRegions.put(parent, new HashSet<>());
        }

        Regions.regionsToAllPossibleInnerRegions.get(parent).add(child);

        if(!Regions.regionsToAllPossibleInnerRegions.containsKey(child)) {
            Regions.regionsToAllPossibleInnerRegions.put(child, new HashSet<>());
        }
    }

    public static Set<Region> getPossibleInnerRegions(Region region) {
        return Regions.regionsToAllPossibleInnerRegions.get(region);
    }

    public static void addExecutingRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("The region cannot be null");
        }

//        System.out.println("Executing " + region);
        Regions.executingRegions.push(region);
    }

    public static Region getExecutingRegion() {
        return Regions.executingRegions.peek();
    }

    public static void removeExecutingRegion(Region region) {
        Region executing = Regions.executingRegions.pop();
        // TODO this is for testing that the region that believes to have executed is the one that was executing
        if(!region.equals(executing)) {
            throw new RuntimeException("The region that wanted to be removed from the executing regions is not the last region " +
                    "to be executing");
        }
    }

    public static void resetRegions() {
        for(Region region : Regions.regions) {
            region.resetState();
        }

        Regions.program.resetState();
    }

    public static Map<Region, Set<String>> getOptionsInRegionsWithPossibleInnerRegions(Map<Region, Set<String>> regionsToOptions) {
        Map<Region, Set<String>> regionsToInvolvedOptions = new HashMap<>();

        Set<Region> programInnerRegions = Regions.regionsToAllPossibleInnerRegions.get(Regions.program);

        if(programInnerRegions != null) {
            for(Region programInnerRegion : programInnerRegions) {
                Regions.calculateOptionsOfRegionsWithPossibleInnerRegions(programInnerRegion, regionsToOptions, regionsToInvolvedOptions);
            }
        }

        // The program is not a region in the sense that it has options affecting it
        Regions.regionsToAllPossibleInnerRegions.remove(Regions.program);

        return regionsToInvolvedOptions;
    }

    private static void calculateOptionsOfRegionsWithPossibleInnerRegions(Region region, Map<Region, Set<String>> regionsToOptions, Map<Region, Set<String>> result) {
        if(!result.containsKey(region)) {
            Set<String> allAffectingOptions = regionsToOptions.get(region);
            Set<Region> innerRegions = Regions.regionsToAllPossibleInnerRegions.get(region);

            if(!innerRegions.isEmpty()) {
                for(Region innerRegion : Regions.regionsToAllPossibleInnerRegions.get(region)) {
                    Regions.calculateOptionsOfRegionsWithPossibleInnerRegions(innerRegion, regionsToOptions, result);
                }

                for(Region innerRegion : Regions.regionsToAllPossibleInnerRegions.get(region)) {
                    allAffectingOptions.addAll(result.get(innerRegion));
                }
            }

            result.put(region, allAffectingOptions);
        }
    }

    public static void reset() {
        Regions.removeProgram();
        Regions.regions = new HashSet<>();
        Regions.executingRegions = new Stack<>();
        Regions.regionsToAllPossibleInnerRegions = new HashMap<>();

    }

    public static Region getProgram() {
        return Regions.program;
    }

    public static Set<Region> getRegions() {
        return Regions.regions;
    }

    public static Stack<Region> getExecutingRegions() { return Regions.executingRegions; }

    public static Map<Region, Set<Region>> getRegionsToAllPossibleInnerRegions() { return Regions.regionsToAllPossibleInnerRegions; }
}

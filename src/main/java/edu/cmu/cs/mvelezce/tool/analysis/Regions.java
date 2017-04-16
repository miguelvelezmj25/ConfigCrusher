package edu.cmu.cs.mvelezce.tool.analysis;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {
    private static Region program = null;
    private static Set<Region> regions = new HashSet<>();

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

        Regions.regions.add(region);
    }

    public static void removeRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        Regions.regions.remove(region);
    }

    public static Region getRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        for(Region entry : Regions.regions) {
            // TODO check equals
            if(entry.equals(region)) {
                return entry;
            }
        }

        return null;
    }

    public static void resetRegions() {
        for(Region region : Regions.regions) {
            region.resetExecution();
        }
    }

    public static Region getProgram() { return Regions.program; }

    public static Set<Region> getRegions() {
        return Regions.regions;
    }

    public static void removeAllRegions() {
        Regions.regions = new HashSet<>();
    }

    public static void reset() {
        Regions.removeProgram();
        Regions.removeAllRegions();
    }
}

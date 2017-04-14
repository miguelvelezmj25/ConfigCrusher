package edu.cmu.cs.mvelezce.tool.analysis;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepRegion;
import org.apache.commons.collections4.map.HashedMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {
    private static Region program = null;
    // This looks weird
    private static Map<Region, Region> regions = new HashMap<>();

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

        Regions.regions.put(region, region);
    }

    public static Region removeRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        return Regions.regions.remove(region);
    }

    public static Region getRegion(Region region) {
        if(region == null) {
            throw new IllegalArgumentException("Region cannot be null");
        }

        return Regions.regions.get(region);
    }

    public static void resetRegions() {
        for(Region region : Regions.regions.values()) {
            region.resetExecution();
        }
    }

    public static Region getProgram() { return Regions.program; }

    public static Set<Region> getRegions() {
        return new HashSet<>(Regions.regions.values());
    }

    public static void removeAllRegions() {
        Regions.regions = new HashedMap<>();
    }

    public static void reset() {
        Regions.removeProgram();
        Regions.removeAllRegions();
    }
}

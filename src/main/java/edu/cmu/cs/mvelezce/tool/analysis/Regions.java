package edu.cmu.cs.mvelezce.tool.analysis;

import org.apache.commons.collections4.map.HashedMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {
    // This looks weird
    private static Map<Region, Region> regions = new HashMap<>();

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
        HashMap<Region, Region> hold = new HashMap<>();
        for(Region region : Regions.regions.values()) {
            region.resetExecution();
        }
    }

    public static Set<Region> getRegions() {
        return new HashSet<>(Regions.regions.values());
    }

    public static void removeAllRegions() {
        Regions.regions = new HashedMap<>();
    }
}

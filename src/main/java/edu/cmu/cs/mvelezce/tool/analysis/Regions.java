package edu.cmu.cs.mvelezce.tool.analysis;

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

//    public static Region getRegion(String regionPackage, String regionClass, String regionMethod) {
//        // TODO check for empty strings
//        Region hold = new Region(regionPackage, regionClass, regionMethod);
//        Region result = Regions.regions.get(hold);
//
//        if(result == null) {
//            // TODO this is for testing. We would change this for the full pipeline
////            throw new NoSuchElementException("This region is not a relevant region");
//            Regions.addRegion(hold);
//            return hold;
//        }
//
//        return result;
//    }

    public void resetRegions() {
        for(Region region : Regions.regions.values()) {
            region.resetExecution();
        }
    }

    public static Set<Region> getRegions() {
        return new HashSet<>(Regions.regions.values());
    }
}

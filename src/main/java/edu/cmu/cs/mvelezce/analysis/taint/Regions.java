package edu.cmu.cs.mvelezce.analysis.taint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguelvelez on 4/7/17.
 */
public class Regions {
    // This looks weird
    public static Map<Region, Region> regions = new HashMap<>();

    public static Region getRegion(String regionPackage, String regionClass, String regionMethod) {
        // TODO check for empty strings
        Region hold = new Region(regionPackage, regionClass, regionMethod);
        Region result = Regions.regions.get(hold);

        if(result == null) {
            // TODO this is for testing. We would change this for the full pipeline
//            throw new NoSuchElementException("This region is not a relevant region");
            Regions.regions.put(hold, hold);
            return hold;
        }

        return result;
    }
}

package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/5/17.
 */
public class LotrackProcessor {

    public static Map<JavaRegion, Set<String>> filterBooleans(Map<JavaRegion, Set<String>> regionToOptions) {
        // These are language dependent since they can be writen with other capitalization
        Set<String> optionsToRemove = new HashSet<>();
        optionsToRemove.add("true");
        optionsToRemove.add("false");

        Map<JavaRegion, Set<String>> filteredMap = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : regionToOptions.entrySet()) {
            Set<String> options = entry.getValue();
            options.removeAll(optionsToRemove);
            filteredMap.put(entry.getKey(), options);
        }

        return filteredMap;
    }

    public static Map<JavaRegion, Set<String>> filterRegionsNoOptions(Map<JavaRegion, Set<String>> regionToOptions) {
        Map<JavaRegion, Set<String>> filteredMap = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : regionToOptions.entrySet()) {
            if(!entry.getValue().isEmpty()) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredMap;
    }

}

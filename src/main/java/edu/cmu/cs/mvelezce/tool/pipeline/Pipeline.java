package edu.cmu.cs.mvelezce.tool.pipeline;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
// TODO Create adapter for each program/language to pass configurationss
public abstract class Pipeline {

    public static PerformanceModel createPerformanceModel(Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
        Map<Region, Set<String>> regionsToOptionsOfInnerRegions = Regions.getOptionsInRegionsWithPossibleInnerRegions(regionsToOptions);

        // Calculate raw performance for each region
        Map<Region, Map<Set<String>, Double>> regionsToRawPerformance = new HashMap<>();

        for(Map.Entry<Region, Set<String>> entry : regionsToOptionsOfInnerRegions.entrySet()) {
            Map<Set<String>, Double> configurationsToPerformance = new HashMap<>();
            Set<String> entryConfiguration = entry.getValue();

            for(PerformanceEntry performanceEntry : measuredPerformance) {
                Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
                configurationValueInMeasuredConfiguration.retainAll(entryConfiguration);

                Region region = entry.getKey();
                double time = performanceEntry.getRegion(region).getSecondsExecutionTime();

                configurationsToPerformance.put(configurationValueInMeasuredConfiguration, time);
            }

            regionsToRawPerformance.put(entry.getKey(), configurationsToPerformance);
        }

        // Calculate real performance by subtracting child regions' performance for each region's performance
        Map<Region, Map<Set<String>, Double>> regionsToRealPerformance = new HashMap<>();

        for(Region region : Regions.getRegionsToAllPossibleInnerRegions().keySet()) {
            Pipeline.calculateRealPerformanceOfRegion(region, regionsToRawPerformance, regionsToRealPerformance);
        }

        // Calculate base time
        PerformanceEntry performanceEntry = measuredPerformance.iterator().next();
        Set<String> baseConfiguration =  performanceEntry.getConfiguration();
        double baseTime = performanceEntry.getProgram().getSecondsExecutionTime();

        for(Map.Entry<Region, Map<Set<String>, Double>> regionToRealPerformance : regionsToRealPerformance.entrySet()) {
            Set<String> baseConfigurationValueOnRealPerformance = regionsToOptions.get(regionToRealPerformance.getKey());
            baseConfigurationValueOnRealPerformance.retainAll(baseConfiguration);

            baseTime -= regionToRealPerformance.getValue().get(baseConfigurationValueOnRealPerformance);
        }

        List<Map<Set<String>, Double>> blockTimeList = new ArrayList<>(regionsToRealPerformance.values());

        return new PerformanceModel(baseTime, blockTimeList);
    }

    private static void calculateRealPerformanceOfRegion(Region region, Map<Region, Map<Set<String>, Double>> regionsToRawPerformance, Map<Region, Map<Set<String>, Double>> regionsToRealPerformance) {
        // Already have real performance
        if(regionsToRealPerformance.containsKey(region)) {
            return;
        }

        Set<Region> possibleInnerRegions = Regions.getPossibleInnerRegions(region);

        // Region does not have inner regions
        if(possibleInnerRegions.isEmpty()) {
            regionsToRealPerformance.put(region, regionsToRawPerformance.get(region));
            return;
        }

        // Calculate real performance by subtracting inner performances
        Map<Set<String>, Double> configurationsToRealPerformance = regionsToRawPerformance.get(region);

        for(Region innerRegion : possibleInnerRegions) {
            Pipeline.calculateRealPerformanceOfRegion(innerRegion, regionsToRawPerformance, regionsToRealPerformance);
        }

        for(Map.Entry<Set<String>, Double> configurationsToRealPerformanceEntry : configurationsToRealPerformance.entrySet()) {
            Set<String> parentConfiguration = configurationsToRealPerformanceEntry.getKey();

            for(Region innerRegion : possibleInnerRegions) {
                double time = configurationsToRealPerformanceEntry.getValue();
                Map<Set<String>, Double> innerConfigurationsToRealPerformance = regionsToRealPerformance.get(innerRegion);

                if(innerConfigurationsToRealPerformance.keySet().contains(configurationsToRealPerformanceEntry.getKey())) {
                    time -= innerConfigurationsToRealPerformance.get(parentConfiguration);
                }
                else if(parentConfiguration.size() > 1){
                    // If there is a region that executed a sub configuration of the current parent configuration
                    for(Map.Entry<Set<String>, Double> innerConfigurationToRealPerformance : innerConfigurationsToRealPerformance.entrySet()) {
                        Set<String> childConfiguration = innerConfigurationToRealPerformance.getKey();
                        Set<String> childConfigurationValueOfParentConfiguration = new HashSet<>(parentConfiguration);
                        childConfigurationValueOfParentConfiguration.retainAll(childConfiguration);

                        if(!childConfigurationValueOfParentConfiguration.isEmpty() && childConfigurationValueOfParentConfiguration.equals(childConfiguration)) {
                            time -= innerConfigurationToRealPerformance.getValue();
                        }
                    }
                }

                // Could have subtracted from something that was not executed
                configurationsToRealPerformance.put(configurationsToRealPerformanceEntry.getKey(), Math.max(0.0, time));
            }
        }

        regionsToRealPerformance.put(region, configurationsToRealPerformance);
    }

}

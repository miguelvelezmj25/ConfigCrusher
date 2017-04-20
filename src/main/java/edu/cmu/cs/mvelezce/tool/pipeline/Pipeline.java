package edu.cmu.cs.mvelezce.tool.pipeline;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;

import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
// TODO How do we represent configurations in programs
public abstract class Pipeline {

    public static Set<Set<String>> getConfigurationsToExecute(Set<Set<String>> relevantOptionsSet) {
        // Calculates which options are subsets of other options
        Set<Set<String>> filteredOptions = Pipeline.filterOptions(relevantOptionsSet);

        // Get the configurations for each option
        Map<Set<String>, Set<Set<String>>> optionsToConfigurationsToExecute = new HashMap<>();

        for(Set<String> options : filteredOptions) {
            Set<Set<String>> configurationsToExecuteForOption = new HashSet<>();
            configurationsToExecuteForOption.addAll(Helper.getConfigurations(options));
            optionsToConfigurationsToExecute.put(options, configurationsToExecuteForOption);
        }

        // Compresses which configurations to execute
        Set<Set<String>> configurationsToExecute = new HashSet<>();

        if(optionsToConfigurationsToExecute.size() == 0) {
            configurationsToExecute.add(new HashSet<>());
            return configurationsToExecute;
        }

        // Base case covering 1 configuration
        if(optionsToConfigurationsToExecute.size() == 1) {
            configurationsToExecute.addAll(optionsToConfigurationsToExecute.entrySet().iterator().next().getValue());
            return configurationsToExecute;
        }

        Iterator<Map.Entry<Set<String>, Set<Set<String>>>> optionsToConfigurationsToExecuteIterator = optionsToConfigurationsToExecute.entrySet().iterator();
        Map.Entry<Set<String>, Set<Set<String>>> entry1 = optionsToConfigurationsToExecuteIterator.next();

        while(optionsToConfigurationsToExecuteIterator.hasNext()) {
            Map.Entry<Set<String>, Set<Set<String>>> entry2 = optionsToConfigurationsToExecuteIterator.next();
            Set<String> pivotOptions = new HashSet<>(entry1.getKey());
            pivotOptions.retainAll(entry2.getKey());

            configurationsToExecute = new HashSet<>();

            if(entry1.getValue().size() <= entry2.getValue().size()) {
                Pipeline.simpleMerging(entry1, entry2, pivotOptions, configurationsToExecute);
            }
            else {
                Pipeline.simpleMerging(entry2, entry1, pivotOptions, configurationsToExecute);
            }

            Set<String> newCalculatedOptions = new HashSet<>(entry1.getKey());
            newCalculatedOptions.addAll(entry2.getKey());
            Map<Set<String>, Set<Set<String>>> entryHolder = new HashMap<>();
            entryHolder.put(newCalculatedOptions, configurationsToExecute);
            entry1 = entryHolder.entrySet().iterator().next();
        }

        return configurationsToExecute;
    }

    public static Set<Set<String>> filterOptions(Set<Set<String>> relevantOptionsSet) {
        Set<Set<String>> filteredOptions = new HashSet<>();

        for(Set<String> relevantOptions : relevantOptionsSet) {
            if(filteredOptions.isEmpty()) {
                filteredOptions.add(relevantOptions);
                continue;
            }

            Set<Set<String>> optionsToRemove = new HashSet<>();
            Set<Set<String>> optionsToAdd = new HashSet<>();

            for(Set<String> options : filteredOptions) {
                if(options.equals(relevantOptions) || options.containsAll(relevantOptions)) {
                    optionsToAdd.remove(relevantOptions);
                    break;
                }

                if(!options.containsAll(relevantOptions) && relevantOptions.containsAll(options)) {
                    optionsToRemove.add(options);
                }

                optionsToAdd.add(relevantOptions);
            }

            filteredOptions.removeAll(optionsToRemove);
            filteredOptions.addAll(optionsToAdd);
        }

        return filteredOptions;
    }

    private static void simpleMerging(Map.Entry<Set<String>, Set<Set<String>>> largeEntry, Map.Entry<Set<String>, Set<Set<String>>> smallEntry, Set<String> pivotOptions, Set<Set<String>> configurationsToExecute) {
        Iterator<Set<String>> largeSet = largeEntry.getValue().iterator();
        Iterator<Set<String>> smallSet = smallEntry.getValue().iterator();

//        System.out.println("entry 1 size: " + largeEntry.getValue().size());
//        System.out.println("entry 2 size: " + smallEntry.getValue().size());

        while(largeSet.hasNext() && smallSet.hasNext()) {
            Set<String> configurationInLargeSet = largeSet.next();
//            System.out.println("Set1: " + configurationInLargeSet);

            Set<String> valuePivotOptionsInLargeSet = new HashSet<>(configurationInLargeSet);
            valuePivotOptionsInLargeSet.retainAll(pivotOptions);
            boolean merged = false;

            while(smallSet.hasNext()) {
                Set<String> configurationInSmallSet = smallSet.next();
//                System.out.println("Set2: " + configurationInSmallSet);

                Set<String> valuePivotOptionsInSmallSet = new HashSet<>(configurationInSmallSet);
                valuePivotOptionsInSmallSet.retainAll(pivotOptions);

                if(valuePivotOptionsInLargeSet.equals(valuePivotOptionsInSmallSet)) {
//                    System.out.println("Can compress");
                    Set<String> compressedConfiguration = new HashSet<>(configurationInLargeSet);
                    compressedConfiguration.addAll(configurationInSmallSet);
                    configurationsToExecute.add(compressedConfiguration);

                    smallEntry.getValue().remove(configurationInSmallSet);
                    merged = true;
                    break;
                }
            }

            if(!merged) {
                Set<String> compressedConfiguration = new HashSet<>(configurationInLargeSet);
                configurationsToExecute.add(compressedConfiguration);
//                System.out.println("Just add");
//                    throw new RuntimeException("Could not merge the sets");
            }

            smallSet = smallEntry.getValue().iterator();
        }

        while(largeSet.hasNext()) {
            configurationsToExecute.add(largeSet.next());
//            System.out.println("Added rest from largetset");
        }

        while(smallSet.hasNext()) {
            configurationsToExecute.add(smallSet.next());
//            System.out.println("Added rest from smallset");
        }
    }

    public static PerformanceModel createPerformanceModel(Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
        // Get region dependencies
        Map<Region, Set<Region>> regionsToChildRegions = new HashMap<>();

        // TODO get this from the REGIONS
        for(Map.Entry<Region, Set<String>> entry : regionsToOptions.entrySet()) {
            // TODO further analysze children of children. You are not doing that
            regionsToChildRegions.put(entry.getKey(), entry.getKey().getInnerRegions());
        }

        // Calculate raw performance for each region
        Map<Region, Map<Set<String>, Integer>> regionsToRawPerformance = new HashMap<>();

        for(Map.Entry<Region, Set<String>> entry : regionsToOptions.entrySet()) {
            Map<Set<String>, Integer> configurationsToPerformance = new HashMap<>();
            Set<String> entryConfiguration = entry.getValue();

            // So that we track the performance of inner regions
            for(Region innerRegion : entry.getKey().getInnerRegions()) {
                entryConfiguration.addAll(regionsToOptions.get(innerRegion));
            }

            for(PerformanceEntry performanceEntry : measuredPerformance) {
                Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
                configurationValueInMeasuredConfiguration.retainAll(entryConfiguration);

                Region region = entry.getKey();
                Integer time = performanceEntry.getRegion(region).getExecutionTime();

                configurationsToPerformance.put(configurationValueInMeasuredConfiguration, time);
            }

            regionsToRawPerformance.put(entry.getKey(), configurationsToPerformance);
        }

        // Calculate real performance by subtracting child regions' performance for each region's performance
        Map<Region, Map<Set<String>, Integer>> regionsToRealPerformance = new HashMap<>();

        // Method to track that all regions have been updated
        while(regionsToRealPerformance.size() != regionsToRawPerformance.size()) {
            for(Map.Entry<Region, Set<Region>> entry : regionsToChildRegions.entrySet()) {
                // Already have real performance
                if(regionsToRealPerformance.containsKey(entry.getKey())) {
                    continue;
                }

                // Region does not have inner regions
                if(entry.getKey().getInnerRegions().isEmpty()) {
                    regionsToRealPerformance.put(entry.getKey(), regionsToRawPerformance.get(entry.getKey()));
                    continue;
                }

                // Calculate if all inner regions have been calculated with real performance
                Set<Region> innerRegions = entry.getKey().getInnerRegions();
                boolean allCalculated = true;

                for(Region innerRegion : innerRegions) {
                    if(!regionsToRealPerformance.containsKey(innerRegion)) {
                        allCalculated = false;
                        break;
                    }
                }

                // If there are inner regions to still calculate
                if(!allCalculated) {
                    continue;
                }

                // Calculate real performance by subtracting inner performances
                Map<Set<String>, Integer> configurationsToRealPerformance = regionsToRawPerformance.get(entry.getKey());

                for(Region innerRegion : innerRegions) {
                    Map<Set<String>, Integer> innerConfigurationsToRealPerformance = regionsToRealPerformance.get(innerRegion);

                    for(Map.Entry<Set<String>, Integer> innerConfigurationToRealPerformance : innerConfigurationsToRealPerformance.entrySet()) {
                        int time = configurationsToRealPerformance.get(innerConfigurationToRealPerformance.getKey());
                        time -= innerConfigurationToRealPerformance.getValue();
                        configurationsToRealPerformance.put(innerConfigurationToRealPerformance.getKey(), time);
                    }
                }

                regionsToRealPerformance.put(entry.getKey(), configurationsToRealPerformance);
            }
        }

        long baseTime = measuredPerformance.iterator().next().getBaseTime();
        List<Map<Set<String>, Integer>> blockTimeList = new ArrayList<>(regionsToRealPerformance.values());

        return new PerformanceModel(baseTime, blockTimeList);
    }

}

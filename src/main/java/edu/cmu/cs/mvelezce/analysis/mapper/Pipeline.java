package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.analysis.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.analysis.taint.Region;

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

        // Base case covering 0 and 1 configurations
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
        List<Map<Set<String>, Integer>> blockTimeList = new ArrayList<>();
        int baseTime = -1;

//        for(Map.PerformanceEntry<Region, Set<String>> entry : regionsToOptions.entrySet()) {
//            Map<Set<String>, Integer> blockTime = new HashMap<>();
//
//            for(PerformanceEntry performanceEntry : measuredPerformance) {
//                Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
//                configurationValueInMeasuredConfiguration.retainAll(entry.getValue());
//
//                Statement statement = entry.getKey();
//
//                if(statement instanceof StatementIf) {
//                    statement = ((StatementIf) statement).getThenBlock();
//                }
//
//                Integer time = performanceEntry.getRegions().get(statement);
//
//                if(time != null) {
//                    blockTime.put(configurationValueInMeasuredConfiguration, time);
////                    System.out.println(configurationValueInMeasuredConfiguration + " " + time);
//                }
//                else {
//                    blockTime.put(configurationValueInMeasuredConfiguration, 0);
////                    System.out.println(configurationValueInMeasuredConfiguration + " " + 0);
//                }
//
//                baseTime = performanceEntry.getBaseTime();
//            }
//
//            blockTimeList.add(blockTime);
//        }

        return new PerformanceModel(baseTime, blockTimeList);
    }

}

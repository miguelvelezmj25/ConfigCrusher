package edu.cmu.cs.mvelezce.tool.performance;

import org.apache.commons.collections4.map.HashedMap;

import java.util.*;

// TODO time can be in seconds, milliseonds, minutes,.... That affects the type of the block.
/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModel {
    private long baseTime;
    private Map<Set<String>, Map<Set<String>, Integer>> regionToInfluenceTable;

    public PerformanceModel(long baseTime, List<Map<Set<String>, Integer>> blocks) {
        this.baseTime = baseTime;
        this.regionToInfluenceTable = new HashMap<>();

        for(Map<Set<String>, Integer> block : blocks) {
            Set<String> relevantOptions = new HashSet<>();

            for(Set<String> configuration : block.keySet()) {
                relevantOptions.addAll(configuration);
            }

            this.regionToInfluenceTable.put(relevantOptions, this.calculateConfigurationsInfluence(block));
        }
    }

    public long evaluate(Set<String> configuration) {
        long performance = this.baseTime;

        for(Map.Entry<Set<String>, Map<Set<String>, Integer>> region : this.regionToInfluenceTable.entrySet()) {
            for(Map.Entry<Set<String>, Integer> entry : region.getValue().entrySet()) {
                Set<String> configurationValueOfOptionInBlock = new HashSet<>(entry.getKey());
                configurationValueOfOptionInBlock.retainAll(configuration);

                if(entry.getKey().equals(configurationValueOfOptionInBlock)) {
                    performance += entry.getValue();
                }
            }
        }

        return performance;
    }

    public static Map<Set<String>, Integer> calculateConfigurationsInfluence(Map<Set<String>, Integer> regionTable) {
        Map<Set<String>, Integer> configurationToInfluence = new HashedMap<>();

        int numberOfOptions = (int) Math.sqrt(regionTable.size());
        Set<String> regionOptions = new HashSet<>();

        for(Map.Entry<Set<String>, Integer> entry : regionTable.entrySet()) {
            if(entry.getKey().size() == numberOfOptions) {
                regionOptions.addAll(entry.getKey());
            }
        }

        PerformanceModel.calculateConfigurationInfluence(regionOptions, regionTable, configurationToInfluence);

        return configurationToInfluence;
    }

    public static int calculateConfigurationInfluence(Set<String> longestConfiguration, Map<Set<String>, Integer> configurationsToPerformance, Map<Set<String>, Integer> memoizationStore) {
        if(!memoizationStore.containsKey(longestConfiguration)) {
            int currentLength = longestConfiguration.size();
            int influence = configurationsToPerformance.get(longestConfiguration);

            if(currentLength > 0) {
                influence = configurationsToPerformance.get(longestConfiguration);

                for(Map.Entry<Set<String>, Integer> entry : configurationsToPerformance.entrySet()) {
                    Set<String> configuration = entry.getKey();
                    Set<String> intersectionWithLongestConfiguration = new HashSet<>(longestConfiguration);
                    intersectionWithLongestConfiguration.retainAll(configuration);

                    if(configuration.size() < currentLength && intersectionWithLongestConfiguration.equals(configuration)/*!intersectionWithLongestConfiguration.isEmpty()*/) {
                        influence -= PerformanceModel.calculateConfigurationInfluence(entry.getKey(), configurationsToPerformance, memoizationStore);
                    }
                }
            }

            memoizationStore.put(longestConfiguration, influence);
        }

        return memoizationStore.get(longestConfiguration);
    }

    @Override
    public String toString() {
        StringBuilder performanceModel = new StringBuilder("T =");

//        if(this.baseTime != 0) {
            performanceModel.append(" ");
            performanceModel.append(this.baseTime);
//        }

        for(Map<Set<String>, Integer> region : this.regionToInfluenceTable.values()) {
            Set<String> regionOptions = new HashSet<>();

            for(Set<String> configurations : region.keySet()) {
                regionOptions.addAll(configurations);
            }

            for(int i = 0; i <= regionOptions.size(); i++) {
                for(Map.Entry<Set<String>, Integer> entry : region.entrySet()) {
                    if(entry.getKey().size() != i) {
                        continue;
                    }

                    if(entry.getValue() == 0) {
                        continue;
                    }

                    if(entry.getValue() > 0) {
                        performanceModel.append(" + ");
                    }
                    else {
                        performanceModel.append(" - ");
                    }

                    performanceModel.append(Math.abs(entry.getValue()));

                    for(String option : entry.getKey()) {
                        performanceModel.append(option);
                    }
                }
            }
        }

        return performanceModel.toString();
    }
}

package edu.cmu.cs.mvelezce.analysis;

import java.util.*;

/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModel {
    private int baseTime;
    private Map<Set<String>, Map<Set<String>, Integer>> configurationToBlocks;

    public PerformanceModel(int baseTime, List<Map<Set<String>, Integer>> blocks) {
        this.baseTime = baseTime;
        this.configurationToBlocks = new HashMap<>();

        for(Map<Set<String>, Integer> block : blocks) {
            Set<String> relevantOptions = new HashSet<>();

            for(Set<String> configuration : block.keySet()) {
                relevantOptions.addAll(configuration);
            }

            this.configurationToBlocks.put(relevantOptions, block);
        }

    }

    public int evaluate(Set<String> configuration) {
        int performance = this.baseTime;

        for(Map.Entry<Set<String>, Map<Set<String>, Integer>> entry : this.configurationToBlocks.entrySet()) {
            Set<String> configurationValueInBlockForConfiguration = new HashSet<>(configuration);
            configurationValueInBlockForConfiguration.retainAll(entry.getKey());

            performance += entry.getValue().get(configurationValueInBlockForConfiguration);
        }

        return performance;
    }

    @Override
    public String toString() {
        String performanceModel = "T = " + this.baseTime;

        for(Map<Set<String>, Integer> block : this.configurationToBlocks.values()) {
            performanceModel += " + (";
            int count = 0;

            for(Map.Entry<Set<String>, Integer> entry : block.entrySet()) {
                if(entry.getValue() != 0) {
                    if(count == 0) {
                        performanceModel += "";
                    }
                    else {
                        performanceModel += " v +";
                    }

                    performanceModel += "" + entry.getValue();

                    for(String configuration : entry.getKey()) {
                        performanceModel += configuration;
                    }

                    count++;
                }
            }

            performanceModel += ")";
        }

        return performanceModel;
    }
}

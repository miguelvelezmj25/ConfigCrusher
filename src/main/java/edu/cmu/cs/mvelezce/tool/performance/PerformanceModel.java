package edu.cmu.cs.mvelezce.tool.performance;

import java.util.*;

// TODO time can be in seconds, milliseonds, minutes,.... That affects the type of the block.
/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModel {
    private int baseTime;
    private Map<Set<String>, Map<Set<String>, Integer>> optionToBlock;

    public PerformanceModel(List<Map<Set<String>, Integer>> blocks) {
        this(0, blocks);
    }

    public PerformanceModel(int baseTime, List<Map<Set<String>, Integer>> blocks) {
        this.baseTime = baseTime;
        this.optionToBlock = new HashMap<>();

        for(Map<Set<String>, Integer> block : blocks) {
            Set<String> relevantOptions = new HashSet<>();

            for(Set<String> configuration : block.keySet()) {
                relevantOptions.addAll(configuration);
            }

            this.optionToBlock.put(relevantOptions, block);
        }

    }

    public int evaluate(Set<String> configuration) {
        int performance = this.baseTime;

        for(Map.Entry<Set<String>, Map<Set<String>, Integer>> entry : this.optionToBlock.entrySet()) {
            Set<String> configurationValueOfOptionInBlock = new HashSet<>(configuration);
            configurationValueOfOptionInBlock.retainAll(entry.getKey());

            performance += entry.getValue().get(configurationValueOfOptionInBlock);
        }

        return performance;
    }

    @Override
    public String toString() {
        String performanceModel = "T = " ;

        if(this.baseTime != 0) {
            performanceModel += this.baseTime;
        }

        for(Map<Set<String>, Integer> block : this.optionToBlock.values()) {
            performanceModel += " + (";
            int count = 0;

            for(Map.Entry<Set<String>, Integer> entry : block.entrySet()) {
                if(entry.getValue() != 0) {
                    if(count == 0) {
                        performanceModel += "";
                    }
                    else {
                        performanceModel += " v + ";
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

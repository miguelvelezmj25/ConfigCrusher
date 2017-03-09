package edu.cmu.cs.mvelezce.analysis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModel {
    private int baseTime;
    private List<Map<Set<String>, Integer>> blocks;

    public PerformanceModel(int baseTime, List<Map<Set<String>, Integer>> blocks) {
        this.baseTime = baseTime;
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        String performanceModel = "T = " + this.baseTime;

        for(Map<Set<String>, Integer> block : this.blocks) {
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

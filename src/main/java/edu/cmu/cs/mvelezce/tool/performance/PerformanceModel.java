package edu.cmu.cs.mvelezce.tool.performance;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO time can be in seconds, milliseonds, minutes,.... That affects the type of the block.
/**
 * Created by mvelezce on 3/9/17.
 */
public class PerformanceModel {
    private double baseTime;
    private MultiValuedMap<Set<String>, Map<Set<String>, Double>> regionToInfluenceTable;
    private Map<Set<String>, Double> configurationToPerformance;


    public PerformanceModel(double baseTime, List<Map<Set<String>, Double>> blocks) {
        this.baseTime = baseTime;
        this.regionToInfluenceTable = new HashSetValuedHashMap<>();
        this.configurationToPerformance = new HashedMap<>();

        for(Map<Set<String>, Double> block : blocks) {
            Set<String> relevantOptions = new HashSet<>();

            for(Set<String> configuration : block.keySet()) {
                relevantOptions.addAll(configuration);
            }

            this.regionToInfluenceTable.put(relevantOptions, PerformanceModel.calculateConfigurationsInfluence(block));
        }

        for(Map.Entry<Set<String>, Map<Set<String>, Double>> optionToPerformanceTable : this.regionToInfluenceTable.entries()) {
            for(Map.Entry<Set<String>, Double> configurationToPerformance : optionToPerformanceTable.getValue().entrySet()) {
                double time = configurationToPerformance.getValue();

                if(this.configurationToPerformance.containsKey(configurationToPerformance.getKey())) {
                    time += this.configurationToPerformance.get(configurationToPerformance.getKey());
                }

                this.configurationToPerformance.put(configurationToPerformance.getKey(), time);
            }
        }

        HashSet<String> emptyConfiguration = new HashSet<>();
        double emptyConfigurationPerformance = this.baseTime;
        emptyConfigurationPerformance += this.configurationToPerformance.get(emptyConfiguration);
        this.configurationToPerformance.put(emptyConfiguration, emptyConfigurationPerformance);
    }

    public long evaluate(Set<String> configuration) {
        long performance = 0;

        for(Map.Entry<Set<String>, Double> entry : this.configurationToPerformance.entrySet()) {
            Set<String> configurationValueOfOptionInBlock = new HashSet<>(entry.getKey());
            configurationValueOfOptionInBlock.retainAll(configuration);

            if(entry.getKey().equals(configurationValueOfOptionInBlock)) {
                performance += entry.getValue();
            }
        }

        return performance;
    }

    public static Map<Set<String>, Double> calculateConfigurationsInfluence(Map<Set<String>, Double> regionTable) {
        Map<Set<String>, Double> configurationToInfluence = new HashedMap<>();

        long numberOfOptions = (long) Math.sqrt(regionTable.size());
        Set<String> regionOptions = new HashSet<>();

        for(Map.Entry<Set<String>, Double> entry : regionTable.entrySet()) {
            if(entry.getKey().size() == numberOfOptions) {
                regionOptions.addAll(entry.getKey());
            }
        }

        PerformanceModel.calculateConfigurationInfluence(regionOptions, regionTable, configurationToInfluence);

        return configurationToInfluence;
    }

    public static double calculateConfigurationInfluence(Set<String> longestConfiguration, Map<Set<String>, Double> configurationsToPerformance, Map<Set<String>, Double> configurationToInfluence) {
        if(!configurationToInfluence.containsKey(longestConfiguration)) {
            int currentLength = longestConfiguration.size();
            double influence = configurationsToPerformance.get(longestConfiguration);

            if(currentLength > 0) {
                influence = configurationsToPerformance.get(longestConfiguration);

                for(Map.Entry<Set<String>, Double> entry : configurationsToPerformance.entrySet()) {
                    Set<String> configuration = entry.getKey();
                    Set<String> intersectionWithLongestConfiguration = new HashSet<>(longestConfiguration);
                    intersectionWithLongestConfiguration.retainAll(configuration);

                    if(configuration.size() < currentLength && intersectionWithLongestConfiguration.equals(configuration)/*!intersectionWithLongestConfiguration.isEmpty()*/) {
                        influence -= PerformanceModel.calculateConfigurationInfluence(entry.getKey(), configurationsToPerformance, configurationToInfluence);
                    }
                }
            }

            configurationToInfluence.put(longestConfiguration, influence);
        }

        return configurationToInfluence.get(longestConfiguration);
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder performanceModel = new StringBuilder("T =");
        Set<String> allOptions = new HashSet<>();

        for(Set<String> configurations : this.configurationToPerformance.keySet()) {
            allOptions.addAll(configurations);
        }

        for(int i = 0; i <= allOptions.size(); i++) {
            for(Map.Entry<Set<String>, Double> entry : this.configurationToPerformance.entrySet()) {
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

                performanceModel.append(decimalFormat.format(Math.abs(entry.getValue())));

                for(String option : entry.getKey()) {
                    performanceModel.append(option);
                }
            }

        }

        return performanceModel.toString();
    }
}

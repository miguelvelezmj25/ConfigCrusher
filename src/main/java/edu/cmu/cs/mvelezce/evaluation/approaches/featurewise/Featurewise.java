package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Featurewise {

    public Set<PerformanceEntryStatistic> getFeaturewiseEntries(Set<PerformanceEntryStatistic> performanceEntries) {
        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
        Set<Set<String>> featurewiseConfigurations = this.getFeaturewiseConfigurations(configurations);

        Set<PerformanceEntryStatistic> featurewiseEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!featurewiseConfigurations.contains(configuration)) {
                continue;
            }

            featurewiseEntries.add(statistic);
        }

        return featurewiseEntries;
    }

    private Set<Set<String>> getFeaturewiseConfigurations(Set<Set<String>> configurations) {
        Set<String> options = this.getOptions(configurations);
        Set<Set<String>> featurewiseConfigurations = new HashSet<>();

        for(String option : options) {
            Set<String> configuration = new HashSet<>();
            configuration.add(option);

            featurewiseConfigurations.add(configuration);
        }

        return featurewiseConfigurations;
    }

    private Set<String> getOptions(Set<Set<String>> configurations) {
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        return options;
    }

    private Set<Set<String>> getConfigurations(Set<PerformanceEntryStatistic> performanceEntryStatistics) {
        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntryStatistics) {
            Set<String> configuration = statistic.getConfiguration();
            configurations.add(configuration);
        }

        return configurations;
    }

}

package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.model;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeaturewisePerformanceModel extends PerformanceModel {

    public FeaturewisePerformanceModel(double baseTime, Map<Region, Map<Set<String>, Double>> regionsToPerformanceHumanReadable) {
        this.setBaseTimeHumanReadable(baseTime);
        this.setRegionsToPerformanceTablesHumanReadable(regionsToPerformanceHumanReadable);

        this.setRegionsToPerformanceTables(FeaturewisePerformanceModel.toNanosecondPerformance(this.getRegionsToPerformanceTablesHumanReadable()));
    }

    private static Map<Region, Map<Set<String>, Long>> toNanosecondPerformance(Map<Region, Map<Set<String>, Double>> regionsToPerformanceTables) {
        Map<Region, Map<Set<String>, Long>> result = new HashMap<>();

        for(Map.Entry<Region, Map<Set<String>, Double>> regionToTable : regionsToPerformanceTables.entrySet()) {
            Map<Set<String>, Long> configToNewPerformance = new HashMap<>();

            for(Map.Entry<Set<String>, Double> configToPerformance : regionToTable.getValue().entrySet()) {
                long nanoseconds = DefaultPerformanceEntry.toNanoseconds(configToPerformance.getValue());
                configToNewPerformance.put(configToPerformance.getKey(), nanoseconds);
            }

            result.put(regionToTable.getKey(), configToNewPerformance);
        }

        return result;
    }

    @Override
    public double evaluate(Set<String> configuration) {
        double performance = this.getBaseTimeHumanReadable();

        for(Map.Entry<Region, Map<Set<String>, Double>> entry : this.getRegionsToPerformanceTablesHumanReadable().entrySet()) {
            for(Map.Entry<Set<String>, Double> optionsToPerformance : entry.getValue().entrySet()) {
                Set<String> options = optionsToPerformance.getKey();

                if(configuration.containsAll(options)) {
                    performance += optionsToPerformance.getValue();
                }
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String perfString = decimalFormat.format(performance);
        performance = Double.valueOf(perfString);

        return performance;
    }

}

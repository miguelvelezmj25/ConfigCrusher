package edu.cmu.cs.mvelezce.tool.performance.model.builder;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ConfigCrusherPerformanceModelBuilder extends BasePerformanceModelBuilder {

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs";

    public ConfigCrusherPerformanceModelBuilder(String programName, Set<DefaultPerformanceEntry> measuredPerformance, Map<Region, Set<Set<String>>> regionsToOptionSet) {
        super(programName, measuredPerformance, regionsToOptionSet);
    }

    public ConfigCrusherPerformanceModelBuilder(String programName) {
        this(programName, null, null);
    }

    @Override
    public PerformanceModel createModel() {
        Map<Region, Map<Set<String>, Set<Long>>> regionsToPerformanceTable = new HashMap<>();

        for(Map.Entry<Region, Set<Set<String>>> regionToOptionSet : this.getRegionsToOptionSet().entrySet()) {
            Region region = regionToOptionSet.getKey();
            // TODO this might change if we decide to not have a set of set of options for each region
            Set<String> optionsInRegion = new HashSet<>();

            for(Set<String> options : regionToOptionSet.getValue()) {
                optionsInRegion.addAll(options);
            }

            Map<Set<String>, Set<Long>> optionValuesToPerformances = new HashMap<>();

            for(DefaultPerformanceEntry performanceEntry : this.getMeasuredPerformance()) {
                Set<String> configuration = performanceEntry.getConfiguration();
                Set<String> optionValueInPerfEntry = new HashSet<>(optionsInRegion);
                optionValueInPerfEntry.retainAll(configuration);

                long regionTime = 0;

                for(Map.Entry<Region, Long> regionToProcessedPerformance : performanceEntry.getRegionsToProcessedPerformance().entrySet()) {
                    Region performanceRegion = regionToProcessedPerformance.getKey();

                    if(!performanceRegion.getRegionID().equals(region.getRegionID())) {
                        continue;
                    }

                    regionTime += regionToProcessedPerformance.getValue();
                }

                if(!optionValuesToPerformances.containsKey(optionValueInPerfEntry)) {
                    optionValuesToPerformances.put(optionValueInPerfEntry, new HashSet<>());
                }

                optionValuesToPerformances.get(optionValueInPerfEntry).add(regionTime);

            }

            regionsToPerformanceTable.put(region, optionValuesToPerformances);
        }

        long programTime = 0;

        for(DefaultPerformanceEntry performanceEntry : this.getMeasuredPerformance()) {
            for(Map.Entry<Region, Long> regionToProcessedPerformance : performanceEntry.getRegionsToProcessedPerformance().entrySet()) {
                Region region = regionToProcessedPerformance.getKey();

                if(!region.getRegionID().equals(Regions.PROGRAM_REGION_ID)) {
                    continue;
                }

                programTime += regionToProcessedPerformance.getValue();
            }

        }

        programTime /= this.getMeasuredPerformance().size();

        Map<Region, Map<Set<String>, Long>> regionsToAveragePerformanceTable = this.averagePerformance(regionsToPerformanceTable);

        PerformanceModel performanceModel = new PerformanceModel(programTime, regionsToAveragePerformanceTable);

        return performanceModel;
    }

    private Map<Region, Map<Set<String>, Long>> averagePerformance(Map<Region, Map<Set<String>, Set<Long>>> regionsToPeformanceTable) {
        Map<Region, Map<Set<String>, Long>> result = new HashMap<>();

        for(Map.Entry<Region, Map<Set<String>, Set<Long>>> regionToPerformanceTable : regionsToPeformanceTable.entrySet()) {
            Map<Set<String>, Set<Long>> table = regionToPerformanceTable.getValue();

            Map<Set<String>, Long> averagedTable = new HashMap<>();

            for(Map.Entry<Set<String>, Set<Long>> tableEntry : table.entrySet()) {
                long total = 0;

                for(Long performance : tableEntry.getValue()) {
                    total += performance;
                }

                total /= tableEntry.getValue().size();

                averagedTable.put(tableEntry.getKey(), total);
            }

            result.put(regionToPerformanceTable.getKey(), averagedTable);
        }

        return result;
    }

}

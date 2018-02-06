package edu.cmu.cs.mvelezce.tool.performance.model.builder;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ConfigCrusherPerformanceModelBuilder extends BasePerformanceModelBuilder {

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs/configcrusher";

    public ConfigCrusherPerformanceModelBuilder(String programName, Set<PerformanceEntryStatistic> measuredPerformance, Map<Region, Set<Set<String>>> regionsToOptionSet) {
        super(programName, measuredPerformance, regionsToOptionSet);
    }

    public ConfigCrusherPerformanceModelBuilder(String programName) {
        this(programName, null, null);
    }

    // TODO write code that will know when multiple regions are executed in separate
    @Override
    public PerformanceModel createModel() {
        // TODO add these method to the interface
        Map<Region, Map<Set<String>, Long>> regionsToPerformanceTable = this.processPerformance();
        long programTime = this.processProgramTime();
        PerformanceModel performanceModel = new PerformanceModel(programTime, regionsToPerformanceTable);

        return performanceModel;
    }

    @Override
    public String getOutputDir() {
        return ConfigCrusherPerformanceModelBuilder.DIRECTORY;
    }

    private Map<Region, Map<Set<String>, Long>> averageMultipleExecutions(Map<Region, Map<Set<String>, Set<Long>>> regionsToPerformanceTable) {
        Map<Region, Map<Set<String>, Long>> result = new HashMap<>();

        for(Map.Entry<Region, Map<Set<String>, Set<Long>>> regionToPerformanceTable : regionsToPerformanceTable.entrySet()) {
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

    // TODO make abstract and call from super method
    public Map<Region, Map<Set<String>, Long>> processPerformance() {
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


        for(Map.Entry<Region, Map<Set<String>, Set<Long>>> entry : regionsToPerformanceTable.entrySet()) {
            Region region = entry.getKey();

            for(Map.Entry<Set<String>, Set<Long>> entry2 : entry.getValue().entrySet()) {
                Set<String> conf = entry2.getKey();
                Set<Long> values = entry2.getValue();
                long min = Collections.min(values);
                long max = Collections.max(values);

                if((max - min) >= 1000000000) {
                    double minD = min / 1000000000.0;
                    double maxD = max / 1000000000.0;
                    System.out.println("Region: " + region.getRegionID() + " with conf " + conf + " might be incorrect (" + maxD + " : " + minD + ")");

                    for(Long value : values) {
                        System.out.println(value / 1000000000.0);
                    }
                }
            }
        }


        Map<Region, Map<Set<String>, Long>> regionsToAveragePerformanceTable = this.averageMultipleExecutions(regionsToPerformanceTable);
        return regionsToAveragePerformanceTable;
    }

    public Map<Region, Map<Set<String>, Double>> processStd() {
        Map<Region, Map<Set<String>, Set<Double>>> regionsToStdTable = new HashMap<>();

        for(Map.Entry<Region, Set<Set<String>>> regionToOptionSet : this.getRegionsToOptionSet().entrySet()) {
            Region region = regionToOptionSet.getKey();
            // TODO this might change if we decide to not have a set of set of options for each region
            Set<String> optionsInRegion = new HashSet<>();

            for(Set<String> options : regionToOptionSet.getValue()) {
                optionsInRegion.addAll(options);
            }

            Map<Set<String>, Set<Double>> optionValuesToStd = new HashMap<>();

            for(PerformanceEntryStatistic performanceEntry : this.getMeasuredPerformance()) {
                Set<String> configuration = performanceEntry.getConfiguration();
                Set<String> optionValueInPerfEntry = new HashSet<>(optionsInRegion);
                optionValueInPerfEntry.retainAll(configuration);

                double regionStd = 0;

                for(Map.Entry<Region, Double> regionToProcessedPerformance : performanceEntry.getRegionsToProcessedStd().entrySet()) {
                    Region performanceRegion = regionToProcessedPerformance.getKey();

                    if(!performanceRegion.getRegionID().equals(region.getRegionID())) {
                        continue;
                    }

                    regionStd += regionToProcessedPerformance.getValue();
                }

                if(!optionValuesToStd.containsKey(optionValueInPerfEntry)) {
                    optionValuesToStd.put(optionValueInPerfEntry, new HashSet<>());
                }

                optionValuesToStd.get(optionValueInPerfEntry).add(regionStd);
            }

            regionsToStdTable.put(region, optionValuesToStd);
        }

        Map<Region, Map<Set<String>, Double>> regionsToAveragePerformanceTableStd = this.averageMultipleExecutionsStd(regionsToStdTable);
        return regionsToAveragePerformanceTableStd;
    }

    // TODO this is dangerous when regions are executed multiple times but they had different times
    private Map<Region, Map<Set<String>, Double>> averageMultipleExecutionsStd(Map<Region, Map<Set<String>, Set<Double>>> regionsToStdTable) {
        Map<Region, Map<Set<String>, Double>> result = new HashMap<>();

        for(Map.Entry<Region, Map<Set<String>, Set<Double>>> regionToPerformanceTable : regionsToStdTable.entrySet()) {
            Map<Set<String>, Set<Double>> table = regionToPerformanceTable.getValue();
            Map<Set<String>, Double> averagedTable = new HashMap<>();

            for(Map.Entry<Set<String>, Set<Double>> tableEntry : table.entrySet()) {
                double total = 0;

                for(Double std : tableEntry.getValue()) {
                    total += std;
                }

                total /= tableEntry.getValue().size();
                averagedTable.put(tableEntry.getKey(), total);
            }

            result.put(regionToPerformanceTable.getKey(), averagedTable);
        }

        return result;
    }

    public long processProgramTime() {
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
        return programTime;
    }

    public double processProgramStd() {
        double programStd = 0;

        for(PerformanceEntryStatistic performanceEntry : this.getMeasuredPerformance()) {
            for(Map.Entry<Region, Double> regionToProcessedStd : performanceEntry.getRegionsToProcessedStd().entrySet()) {
                Region region = regionToProcessedStd.getKey();

                if(!region.getRegionID().equals(Regions.PROGRAM_REGION_ID)) {
                    continue;
                }

                programStd += regionToProcessedStd.getValue();
            }

        }

        programStd /= this.getMeasuredPerformance().size();
        return programStd;
    }

}

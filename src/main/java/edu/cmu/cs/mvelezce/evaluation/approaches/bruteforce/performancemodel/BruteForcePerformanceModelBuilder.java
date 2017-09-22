package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performancemodel.ConfigCrusherPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry2;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModel;

import java.util.Map;
import java.util.Set;

public class BruteForcePerformanceModelBuilder extends ConfigCrusherPerformanceModelBuilder {

    // TODO not sure that this will be used
    public BruteForcePerformanceModelBuilder(String programName, Set<PerformanceEntry2> measuredPerformance, Map<Region, Set<Set<String>>> regionsToOptionSet) {
        super(programName, measuredPerformance, regionsToOptionSet);
    }

    public BruteForcePerformanceModelBuilder(String programName, Set<PerformanceEntry2> measuredPerformance) {
        this(programName, measuredPerformance, null);

    }

    public BruteForcePerformanceModelBuilder(String programName) {
        super(programName);
    }

    // TODO we are not creating a performance model. Rather we are just saving the
    @Override
    public PerformanceModel createModel() {
        throw new RuntimeException();
//        Map<Region, Map<Set<String>, Set<Long>>> regionsToPerformanceTable = new HashMap<>();
//
//        for(Map.Entry<Region, Set<Set<String>>> regionToOptionSet : this.getRegionsToOptionSet().entrySet()) {
//            Region region = regionToOptionSet.getKey();
//            // TODO this might change if we decide to not have a set of set of options for each region
//            Set<String> optionsInRegion = new HashSet<>();
//
//            for(Set<String> options : regionToOptionSet.getValue()) {
//                optionsInRegion.addAll(options);
//            }
//
//            Map<Set<String>, Set<Long>> optionValuesToPerformances = new HashMap<>();
//
//            for(PerformanceEntry2 performanceEntry : this.getMeasuredPerformance()) {
//                Set<String> configuration = performanceEntry.getConfiguration();
//                Set<String> optionValueInPerfEntry = new HashSet<>(optionsInRegion);
//                optionValueInPerfEntry.retainAll(configuration);
//
//                long regionTime = 0;
//
//                for(Map.Entry<Region, Long> regionToProcessedPerformance : performanceEntry.getRegionsToProcessedPerformance().entrySet()) {
//                    Region performanceRegion = regionToProcessedPerformance.getKey();
//
//                    if(!performanceRegion.getRegionID().equals(region.getRegionID())) {
//                        continue;
//                    }
//
//                    regionTime += regionToProcessedPerformance.getValue();
//                }
//
//                if(!optionValuesToPerformances.containsKey(optionValueInPerfEntry)) {
//                    optionValuesToPerformances.put(optionValueInPerfEntry, new HashSet<>());
//                }
//
//                optionValuesToPerformances.get(optionValueInPerfEntry).add(regionTime);
//
//            }
//
//            regionsToPerformanceTable.put(region, optionValuesToPerformances);
//        }
//
//        long programTime = 0;
//
//        for(PerformanceEntry2 performanceEntry : this.getMeasuredPerformance()) {
//            for(Map.Entry<Region, Long> regionToProcessedPerformance : performanceEntry.getRegionsToProcessedPerformance().entrySet()) {
//                Region region = regionToProcessedPerformance.getKey();
//
//                if(!region.getRegionID().equals(Regions.PROGRAM_REGION_ID)) {
//                    continue;
//                }
//
//                programTime += regionToProcessedPerformance.getValue();
//            }
//
//        }
//
//        programTime /= this.getMeasuredPerformance().size();
//
//        Map<Region, Map<Set<String>, Long>> regionsToAveragePerformanceTable = this.averagePerformance(regionsToPerformanceTable);
//
//        PerformanceModel performanceModel = new PerformanceModel(programTime, regionsToAveragePerformanceTable);
//
//        return performanceModel;
//
//
//////        ConfigCrusherPerformanceModelBuilder.getOuterRegions(measuredPerformance, regionsToOptions);
//////        Map<Region, Set<String>> regionsToOptionsIncludingInnerRegions = ConfigCrusherPerformanceModelBuilder.getOptionsInRegionsWithInnerRegions(measuredPerformance, regionsToOptions);
////        for(PerformanceEntry entry : measuredPerformance) {
////            for(Map.Entry<Region, Set<Region>> region : entry.getRegionsToInnerRegions().entrySet()) {
////                System.out.println("############### " + region.getKey().getRegionID());
////
////                for(Region inner : region.getValue()) {
////                    System.out.println(inner.getRegionID());
////                }
////            }
////        }
////
////        Map<Region, Set<String>> regionsToOptionsIncludingInnerRegions = regionsToOptions;
////        Map<Region, Map<Set<String>, Double>> regionsToConfigurationPerformance = new HashMap<>();
////
////        // Building the bf table for each region
////        for(Map.Entry<Region, Set<String>> entry : regionsToOptionsIncludingInnerRegions.entrySet()) {
////            Map<Set<String>, Double> configurationsToPerformance = new HashMap<>();
////            Set<String> entryConfiguration = entry.getValue();
////
////            for(PerformanceEntry performanceEntry : measuredPerformance) {
////                Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
////                configurationValueInMeasuredConfiguration.retainAll(entryConfiguration);
////
////                Region region = entry.getKey();
////                long executionTime = Math.max(0, performanceEntry.getTimeOfRegionID(region.getRegionID()));
////                double time = Region.getSecondsExecutionTime(0, executionTime);
////
////                configurationsToPerformance.put(configurationValueInMeasuredConfiguration, time);
////            }
////
////            regionsToConfigurationPerformance.put(entry.getKey(), configurationsToPerformance);
////        }
////
////        // We do not have region information here
////        List<Map<Set<String>, Double>> bfTablePerRegion = new ArrayList<>(regionsToConfigurationPerformance.values());
////
////        return new PerformanceModel(bfTablePerRegion);
    }
}

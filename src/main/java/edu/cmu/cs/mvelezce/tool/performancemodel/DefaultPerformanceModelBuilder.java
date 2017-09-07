package edu.cmu.cs.mvelezce.tool.performancemodel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public class DefaultPerformanceModelBuilder implements PerformanceModelBuilder{

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs";

    private String programName;

    public DefaultPerformanceModelBuilder(String programName) {
        this.programName = programName;
    }

    @Override
    public PerformanceModel createModel(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputFile = DefaultPerformanceModelBuilder.DIRECTORY + "/" + this.programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            return this.readFromFile(file);
        }

        return null;
    }

    @Override
    public PerformanceModel createModel(String[] args, Set<PerformanceEntry2> measuredPerformance, Map<Region, Set<String>> regionsToOptions) throws IOException {
        PerformanceModel performanceModel = this.createModel(args);

        if(performanceModel != null) {
            return performanceModel;
        }

        performanceModel = this.createModel(measuredPerformance, regionsToOptions);

        if(Options.checkIfSave()) {
            this.writeToFile(performanceModel);
        }

        return performanceModel;
    }

    @Override
    public PerformanceModel createModel(Set<PerformanceEntry2> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
        // TODO
        throw new RuntimeException();

////        DefaultPerformanceModelBuilder.getOuterRegions(measuredPerformance, regionsToOptions);
////        Map<Region, Set<String>> regionsToOptionsIncludingInnerRegions = DefaultPerformanceModelBuilder.getOptionsInRegionsWithInnerRegions(measuredPerformance, regionsToOptions);
//        for(PerformanceEntry entry : measuredPerformance) {
//            for(Map.Entry<Region, Set<Region>> region : entry.getRegionsToInnerRegions().entrySet()) {
//                System.out.println("############### " + region.getKey().getRegionID());
//
//                for(Region inner : region.getValue()) {
//                    System.out.println(inner.getRegionID());
//                }
//            }
//        }
//
//        Map<Region, Set<String>> regionsToOptionsIncludingInnerRegions = regionsToOptions;
//        Map<Region, Map<Set<String>, Double>> regionsToConfigurationPerformance = new HashMap<>();
//
//        // Building the bf table for each region
//        for(Map.Entry<Region, Set<String>> entry : regionsToOptionsIncludingInnerRegions.entrySet()) {
//            Map<Set<String>, Double> configurationsToPerformance = new HashMap<>();
//            Set<String> entryConfiguration = entry.getValue();
//
//            for(PerformanceEntry performanceEntry : measuredPerformance) {
//                Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
//                configurationValueInMeasuredConfiguration.retainAll(entryConfiguration);
//
//                Region region = entry.getKey();
//                long executionTime = Math.max(0, performanceEntry.getTimeOfRegionID(region.getRegionID()));
//                double time = Region.getSecondsExecutionTime(0, executionTime);
//
//                configurationsToPerformance.put(configurationValueInMeasuredConfiguration, time);
//            }
//
//            regionsToConfigurationPerformance.put(entry.getKey(), configurationsToPerformance);
//        }
//
//        // We do not have region information here
//        List<Map<Set<String>, Double>> bfTablePerRegion = new ArrayList<>(regionsToConfigurationPerformance.values());
//
//        return new PerformanceModel(bfTablePerRegion);
    }

//    private static void getOuterRegions(Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
//        Map<Region, Set<Region>> regionsToOuterRegions = new HashMap<>();
//
//        for(PerformanceEntry perfEntry : measuredPerformance) {
//            for(Map.Entry<Region, Set<Region>> regionToInnerRegions : perfEntry.getRegionsToInnerRegions().entrySet()) {
//
//                for(Region innerRegion : regionToInnerRegions.getValue()) {
//                    if(regionsToOuterRegions.isEmpty()) {
//                        Set<Region> outerRegions = new HashSet<>();
//                        outerRegions.add(regionToInnerRegions.getKey());
//                        regionsToOuterRegions.put(innerRegion, outerRegions);
//
//                        continue;
//                    }
//
//                    boolean update = false;
//
//                    for(Map.Entry<Region, Set<Region>> regionToOuterRegions : regionsToOuterRegions.entrySet()) {
//                        if(regionToOuterRegions.getKey().getRegionID().equals(innerRegion.getRegionID())) {
//                            boolean newRegion = true;
//
//                            for(Region outerRegion : regionToOuterRegions.getValue()) {
//                                if(outerRegion.getRegionID().equals(regionToInnerRegions.getKey().getRegionID())) {
//                                    newRegion = false;
//                                    break;
//                                }
//                            }
//
//                            if(newRegion) {
//                                regionToOuterRegions.getValue().add(regionToInnerRegions.getKey());
//                            }
//
//                            update = true;
//                            break;
//                        }
//                    }
//
//                    if(!update) {
//                        Set<Region> outerRegions = new HashSet<>();
//                        outerRegions.add(regionToInnerRegions.getKey());
//                        regionsToOuterRegions.put(innerRegion, outerRegions);
//                    }
//                }
//            }
//        }
//
//        for(Map.Entry<Region, Set<Region>> regionToOuterRegions : regionsToOuterRegions.entrySet()) {
//            Set<Region> programRegion = new HashSet<>();
//
//            for(Region outerRegion : regionToOuterRegions.getValue()) {
//                if(outerRegion.getRegionID().equals("program")) {
//                    programRegion.add(outerRegion);
//                }
//            }
//
//            regionToOuterRegions.getValue().removeAll(programRegion);
//
//        }
//
//        for(Map.Entry<Region, Set<Region>> regionToOuterRegions : regionsToOuterRegions.entrySet()) {
//            Stack<Region> regionsToVisit = new Stack<>();
//            regionsToVisit.addAll(regionToOuterRegions.getValue());
//
//            while (!regionsToVisit.isEmpty()) {
//                Region visitingRegion = regionsToVisit.pop();
//
//                for(Map.Entry<Region, Set<Region>> visitingRegionToOuterRegions : regionsToOuterRegions.entrySet()) {
//                    if(visitingRegionToOuterRegions.getKey().getRegionID().equals(visitingRegion.getRegionID())) {
//                        regionToOuterRegions.getValue().addAll(visitingRegionToOuterRegions.getValue());
//                        regionsToVisit.addAll(visitingRegionToOuterRegions.getValue());
//
//                        break;
//                    }
//
//                }
//
//            }
//
//        }
//
//        for(Map.Entry<Region, Set<Region>> regionToOuterRegions : regionsToOuterRegions.entrySet()) {
//            System.out.print(regionToOuterRegions.getKey().getRegionID() + "->");
//            Set<String> options = new HashSet<>();
//
//            for(Region outerRegion : regionToOuterRegions.getValue()) {
//                for(Map.Entry<Region, Set<String>> regionToOptions : regionsToOptions.entrySet()) {
//                    if(regionToOptions.getKey().getRegionID().equals(outerRegion.getRegionID())) {
//                        options.addAll(regionToOptions.getValue());
//                    }
//                }
//            }
//
//            for(Map.Entry<Region, Set<String>> regionToOptions : regionsToOptions.entrySet()) {
//                if(regionToOptions.getKey().getRegionID().equals(regionToOuterRegions.getKey().getRegionID())) {
//                    options.addAll(regionToOptions.getValue());
//                    break;
//                }
//            }
//            System.out.println(options);
//        }
//
//    }
//
//    public static Map<Region, Set<String>> getOptionsInRegionsWithInnerRegions(Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
//        Map<Region, Set<String>> regionsToInvolvedOptions = new HashMap<>();
//
//        for(Region region : regionsToOptions.keySet()) {
//            regionsToInvolvedOptions.put(region, new HashSet<>());
//        }
//
//        Set<Region> regionsToAdd = new HashSet<>();
//
//        // Mostly to add the program
//        for(PerformanceEntry perfEntry : measuredPerformance) {
//            for(Region region : perfEntry.getRegionsToExecutionTime().keySet()) {
//                boolean added = false;
//
//                for(Region regionWithOptions : regionsToInvolvedOptions.keySet()) {
//                    if(region.getRegionID().equals(regionWithOptions.getRegionID())) {
//                        added = true;
//
//                        break;
//                    }
//                }
//
//                if(!added) {
//                    added = false;
//
//                    for(Region toAddRegion : regionsToAdd) {
//                        if(toAddRegion.getRegionID().equals(region.getRegionID())) {
//                            added = true;
//
//                            break;
//                        }
//                    }
//
//                    if(!added) {
//                        regionsToAdd.add(region);
//                    }
//                }
//            }
//        }
//
//        //TODO are there repeated regions?
//        for(Region region : regionsToAdd) {
//            regionsToInvolvedOptions.put(region, new HashSet<>());
//        }
//
//        for(PerformanceEntry performanceEntry : measuredPerformance) {
//            for(Map.Entry<Region, Set<Region>> regionToInnerRegions : performanceEntry.getRegionsToInnerRegions().entrySet()) {
//                Region region = regionToInnerRegions.getKey();
//                Set<String> options = new HashSet<>();
//
//                // Add options from inner regions
//                for(Region innerRegion : regionToInnerRegions.getValue()) {
//                    for(Map.Entry<Region, Set<String>> regionToOption : regionsToOptions.entrySet()) {
//                        if(regionToOption.getKey().getRegionID().equals(innerRegion.getRegionID())) {
//                            options.addAll(regionToOption.getValue());
//                        }
//                    }
//                }
//
//                // Add options from this region
//                for(Map.Entry<Region, Set<String>> regionToOption : regionsToOptions.entrySet()) {
//                    if(regionToOption.getKey().getRegionID().equals(region.getRegionID())) {
//                        options.addAll(regionToOption.getValue());
//
//                        break;
//                    }
//                }
//
//                for(Map.Entry<Region, Set<String>> regionToInvolvedOptions : regionsToInvolvedOptions.entrySet()) {
//                    if(regionToInvolvedOptions.getKey().getRegionID().equals(regionToInnerRegions.getKey().getRegionID())) {
//                        regionToInvolvedOptions.getValue().addAll(options);
//
//                        break;
//                    }
//                }
//            }
//        }
//
//        return regionsToInvolvedOptions;
//    }


//    private static void calculateRealPerformanceOfRegion(Region region, Map<Region, Map<Set<String>, Double>> regionsToRawPerformance, Map<Region, Map<Set<String>, Double>> regionsToRealPerformance) {
//        // Already have real performancemodel
//        if(regionsToRealPerformance.containsKey(region)) {
//            return;
//        }
//
//        Set<Region> possibleInnerRegions = Regions.getPossibleInnerRegions(region);
//
//        // Region does not have inner regions
//        if(possibleInnerRegions.isEmpty()) {
//            regionsToRealPerformance.put(region, regionsToRawPerformance.get(region));
//            return;
//        }
//
//        // Calculate real performancemodel by subtracting inner performances
//        Map<Set<String>, Double> configurationsToRealPerformance = regionsToRawPerformance.get(region);
//
//        for(Region innerRegion : possibleInnerRegions) {
//            DefaultPerformanceModelBuilder.calculateRealPerformanceOfRegion(innerRegion, regionsToRawPerformance, regionsToRealPerformance);
//        }
//
//        for(Map.Entry<Set<String>, Double> configurationsToRealPerformanceEntry : configurationsToRealPerformance.entrySet()) {
//            Set<String> parentConfiguration = configurationsToRealPerformanceEntry.getKey();
//
//            for(Region innerRegion : possibleInnerRegions) {
//                double time = configurationsToRealPerformanceEntry.getValue();
//                Map<Set<String>, Double> innerConfigurationsToRealPerformance = regionsToRealPerformance.get(innerRegion);
//
//                if(innerConfigurationsToRealPerformance.keySet().contains(configurationsToRealPerformanceEntry.getKey())) {
//                    time -= innerConfigurationsToRealPerformance.get(parentConfiguration);
//                }
//                else if(parentConfiguration.size() > 1){
//                    // If there is a region that executed a sub configuration of the current parent configuration
//                    for(Map.Entry<Set<String>, Double> innerConfigurationToRealPerformance : innerConfigurationsToRealPerformance.entrySet()) {
//                        Set<String> childConfiguration = innerConfigurationToRealPerformance.getKey();
//                        Set<String> childConfigurationValueOfParentConfiguration = new HashSet<>(parentConfiguration);
//                        childConfigurationValueOfParentConfiguration.retainAll(childConfiguration);
//
//                        if(!childConfigurationValueOfParentConfiguration.isEmpty() && childConfigurationValueOfParentConfiguration.equals(childConfiguration)) {
//                            time -= innerConfigurationToRealPerformance.getValue();
//                        }
//                    }
//                }
//
//                // Could have subtracted from calculateConfigurationInfluence that was not executed
//                configurationsToRealPerformance.put(configurationsToRealPerformanceEntry.getKey(), Math.max(0.0, time));
//            }
//        }
//
//        regionsToRealPerformance.put(region, configurationsToRealPerformance);
//    }

    @Override
    public void writeToFile(PerformanceModel performanceModel) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = DefaultPerformanceModelBuilder.DIRECTORY + "/" + this.programName + Options.DOT_JSON;
        File file = new File(outputFile);

        mapper.writeValue(file, performanceModel);
    }

    // TODO test
    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PerformanceModel performanceModel = mapper.readValue(file, new TypeReference<PerformanceModel>() {
        });

        return performanceModel;
    }
}

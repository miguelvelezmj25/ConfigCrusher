package edu.cmu.cs.mvelezce.tool.performancemodel;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public abstract class BasePerformanceModelBuilder implements PerformanceModelBuilder {

    private static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs";

    private String programName;
    private Set<PerformanceEntry2> measuredPerformance;
    private Map<Region, Set<Set<String>>> regionsToOptionSet;
    ;

    public BasePerformanceModelBuilder(String programName, Set<PerformanceEntry2> measuredPerformance, Map<Region, Set<Set<String>>> regionsToOptionSet) {
        this.programName = programName;
        this.measuredPerformance = measuredPerformance;
        this.regionsToOptionSet = regionsToOptionSet;
    }

    public String getProgramName() {
        return programName;
    }

    public Set<PerformanceEntry2> getMeasuredPerformance() {
        return measuredPerformance;
    }

    public Map<Region, Set<Set<String>>> getRegionsToOptionSet() {
        return regionsToOptionSet;
    }

    @Override
    public PerformanceModel createModel(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputDir = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName;
        File outputFile = new File(outputDir);

        Options.checkIfDeleteResult(outputFile);

        if(outputFile.exists()) {
            Collection<File> files = FileUtils.listFiles(outputFile, null, true);

            if(files.size() != 1) {
                throw new RuntimeException("We expected to find 1 file in the directory, but that is not the case "
                        + outputFile);
            }

            return this.readFromFile(files.iterator().next());
        }

        PerformanceModel performanceModel = this.createModel();

        if(Options.checkIfSave()) {
            this.writeToFile(performanceModel);
        }

        return performanceModel;
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
//            ConfigCrusherPerformanceModelBuilder.calculateRealPerformanceOfRegion(innerRegion, regionsToRawPerformance, regionsToRealPerformance);
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
        String outputFile = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName + "/" + this.programName
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        mapper.writeValue(file, performanceModel);
    }


    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        throw new UnsupportedOperationException("Have not figured out how to deserialize a performance model");
//        ObjectMapper mapper = new ObjectMapper();
//        PerformanceModel performanceModel = mapper.readValue(file, new TypeReference<PerformanceModel>() {
//        });
//
//        return performanceModel;
    }
}

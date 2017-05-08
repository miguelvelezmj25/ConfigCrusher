package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class PerformanceModelBuilder {

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs";

    // JSON strings
    public static final String MODEL = "model";
//    public static final String BASE_TIME = "baseTime";
    public static final String CONFIGURATION = "configuration";
    public static final String PERFORMANCE = "performance";
    public static final String CONFIGURATION_TO_PERFORMANCE = "configurationToPerformance";

    public static PerformanceModel createPerformanceModel(String programName, String[] args, Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) throws IOException {
        Options.getCommandLine(args);

        String outputFile = PerformanceModelBuilder.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            try {
                return PerformanceModelBuilder.readFromFile(file);
            }
            catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        PerformanceModel performanceModel = PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);

        if(Options.checkIfSave()) {
            PerformanceModelBuilder.writeToFile(programName, performanceModel);
        }

        return performanceModel;
    }

    public static PerformanceModel createPerformanceModel(Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
        Map<Region, Set<String>> regionsToOptionsIncludingInnerRegions = PerformanceModelBuilder.getOptionsInRegionsWithInnerRegions(measuredPerformance, regionsToOptions);
        Map<Region, Map<Set<String>, Double>> regionsToConfigurationPerformance = new HashMap<>();

        for(Map.Entry<Region, Set<String>> entry : regionsToOptionsIncludingInnerRegions.entrySet()) {
            Map<Set<String>, Double> configurationsToPerformance = new HashMap<>();
            Set<String> entryConfiguration = entry.getValue();

            for(PerformanceEntry performanceEntry : measuredPerformance) {
                Set<String> configurationValueInMeasuredConfiguration = new HashSet<>(performanceEntry.getConfiguration());
                configurationValueInMeasuredConfiguration.retainAll(entryConfiguration);

                Region region = entry.getKey();
                long executionTime = Math.max(0, performanceEntry.getTimeOfRegionID(region.getRegionID()));
                double time = Region.getSecondsExecutionTime(0, executionTime);

                configurationsToPerformance.put(configurationValueInMeasuredConfiguration, time);
            }

            regionsToConfigurationPerformance.put(entry.getKey(), configurationsToPerformance);
        }

//        // Calculate base time
//        PerformanceEntry performanceEntry = measuredPerformance.iterator().next();
//        Set<String> baseConfiguration =  performanceEntry.getConfiguration();
//        // TODO have a variable a not hard coded
//        long programTime = performanceEntry.getTimeOfRegionID("program");
//        double baseTime = Region.getSecondsExecutionTime(0, programTime);
//
//        for(Map.Entry<Region, Map<Set<String>, Double>> regionToConfigurationPerformance : regionsToConfigurationPerformance.entrySet()) {
//            Set<String> baseConfigurationValueOnRealPerformance = regionsToOptions.get(regionToConfigurationPerformance.getKey());
//            baseConfigurationValueOnRealPerformance.retainAll(baseConfiguration);
//
//            baseTime -= regionToConfigurationPerformance.getValue().get(baseConfigurationValueOnRealPerformance);
//        }

        List<Map<Set<String>, Double>> blockTimeList = new ArrayList<>(regionsToConfigurationPerformance.values());

        return new PerformanceModel(blockTimeList);
    }

    public static Map<Region, Set<String>> getOptionsInRegionsWithInnerRegions(Set<PerformanceEntry> measuredPerformance, Map<Region, Set<String>> regionsToOptions) {
        Map<Region, Set<String>> regionsToInvolvedOptions = new HashMap<>();

        for(PerformanceEntry performanceEntry : measuredPerformance) {
            for(Map.Entry<Region, Set<Region>> regionToInnerRegions : performanceEntry.getRegionToInnerRegions().entrySet()) {
                Set<String> options = new HashSet<>();

                for(Region innerRegion : regionToInnerRegions.getValue()) {
                    for(Map.Entry<Region, Set<String>> regionToOption : regionsToOptions.entrySet()) {
                        if(regionToOption.getKey().getRegionID().equals(innerRegion.getRegionID())) {
                            options.addAll(regionToOption.getValue());
                        }
                    }
                }

                for(Map.Entry<Region, Set<String>> regionToOption : regionsToOptions.entrySet()) {
                    if(regionToOption.getKey().getRegionID().equals(regionToInnerRegions.getKey().getRegionID())) {
                        options.addAll(regionToOption.getValue());
                        break;
                    }
                }

                if(regionsToInvolvedOptions.isEmpty()) {
                    regionsToInvolvedOptions.put(regionToInnerRegions.getKey(), options);
                }
                else {
                    boolean updated = false;

                    for(Map.Entry<Region, Set<String>> regionToInvolvedOptions : regionsToInvolvedOptions.entrySet()) {
                        if(regionToInvolvedOptions.getKey().getRegionID().equals(regionToInnerRegions.getKey().getRegionID())) {
                            regionToInvolvedOptions.getValue().addAll(options);
                            updated = true;
                            break;
                        }
                    }

                    if(!updated) {
                        regionsToInvolvedOptions.put(regionToInnerRegions.getKey(), options);
                    }
                }
            }
        }

        return regionsToInvolvedOptions;
    }


//    private static void calculateRealPerformanceOfRegion(Region region, Map<Region, Map<Set<String>, Double>> regionsToRawPerformance, Map<Region, Map<Set<String>, Double>> regionsToRealPerformance) {
//        // Already have real performance
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
//        // Calculate real performance by subtracting inner performances
//        Map<Set<String>, Double> configurationsToRealPerformance = regionsToRawPerformance.get(region);
//
//        for(Region innerRegion : possibleInnerRegions) {
//            PerformanceModelBuilder.calculateRealPerformanceOfRegion(innerRegion, regionsToRawPerformance, regionsToRealPerformance);
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

    private static void writeToFile(String programName, PerformanceModel performanceModel) throws IOException {
        JSONObject model = new JSONObject();

//        model.put(PerformanceModelBuilder.BASE_TIME, performanceModel.getBaseTime());

        JSONArray configurationToPerformance = new JSONArray();

        for(Map.Entry<Set<String>, Double> configurationToPerformanceEntry : performanceModel.getConfigurationToPerformance().entrySet()) {
            JSONObject configuration = new JSONObject();
            configuration.put(PerformanceModelBuilder.PERFORMANCE, configurationToPerformanceEntry.getValue());

            JSONArray values = new JSONArray();
            for(String option : configurationToPerformanceEntry.getKey()) {
                values.add(option);
            }

            configuration.put(PerformanceModelBuilder.CONFIGURATION, values);

            configurationToPerformance.add(configuration);
        }

        model.put(PerformanceModelBuilder.CONFIGURATION_TO_PERFORMANCE, configurationToPerformance);

        JSONObject result = new JSONObject();
        result.put(PerformanceModelBuilder.MODEL, model);

        File directory = new File(PerformanceModelBuilder.DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = PerformanceModelBuilder.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toJSONString());
        writer.flush();
        writer.close();
    }

    private static PerformanceModel readFromFile(File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(new FileReader(file));

        JSONObject model = (JSONObject) result.get(PerformanceModelBuilder.MODEL);

//        double baseTime = (double) model.get(PerformanceModelBuilder.BASE_TIME);
        Map<Set<String>, Double> configurationsToPerformances = new HashMap<>();

        JSONArray configurationsToPerformancesResult = (JSONArray) model.get(PerformanceModelBuilder.CONFIGURATION_TO_PERFORMANCE);

        for(Object entry : configurationsToPerformancesResult) {
            JSONObject configurationToPerformanceResult = (JSONObject) entry;

            double performance = (double) configurationToPerformanceResult.get(PerformanceModelBuilder.PERFORMANCE);

            Set<String> configuration = new HashSet<>();
            JSONArray optionsResult = (JSONArray) configurationToPerformanceResult.get(PerformanceModelBuilder.CONFIGURATION);

            for(Object optionResult : optionsResult) {
                configuration.add((String) optionResult);
            }

            configurationsToPerformances.put(configuration, performance);
        }

        PerformanceModel performanceModel = new PerformanceModel(/*baseTime,*/ new ArrayList<>());
        performanceModel.setConfigurationToPerformance(configurationsToPerformances);

        return performanceModel;
    }
}

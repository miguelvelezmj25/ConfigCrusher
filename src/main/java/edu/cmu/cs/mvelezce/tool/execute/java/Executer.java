package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Executer {
    public static final String DIRECTORY = Options.DIRECTORY + "/executer/java/programs";

    // JSON strings
    public static final String PERFORMANCE_ENTRY = "performanceEntry";
    public static final String CONFIGURATION = "configuration";
    public static final String PROGRAM = "program";
    public static final String ID = "ID";
    public static final String REGIONS = "regions";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String REGIONS_TO_INNER_REGIONS = "regionToInnerRegions";
    public static final String INNER_REGIONS = "innerRegions";

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args, String mainClass, String directory, Set<Set<String>> configurationsToExecute) throws IOException {
        Options.getCommandLine(args);

        String outputFile = Executer.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

//        if(file.exists()) {
//            try {
//                return Executer.readFromFile(file);
//            }
//            catch (ParseException pe) {
//                throw new RuntimeException("Could not parse the cached results");
//            }
//        }

        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(programName, mainClass, directory, configurationsToExecute);

//        if(Options.checkIfSave()) {
//            Executer.writeToFile(programName, measuredPerformance);
//        }

        return measuredPerformance;
    }

    public static void measureConfigurationsPerformance(String programName, String mainClass, String directory, Set<Set<String>> configurationsToExecute) {
        for(Set<String> configuration : configurationsToExecute) {
            // TODO factory pattern or switch statement to create the right adapter
            Adapter adapter = new SleepAdapter(programName, mainClass, directory);
            adapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }
        }
    }


    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String mainClass, String directory, Set<Set<String>> configurationsToExecute) {
        //        JavaRegion program = new JavaRegion(mainClass, mainClass);
//        Regions.addProgram(program);

        for(Set<String> configuration : configurationsToExecute) {
//            Regions.resetRegions();
            // TODO factory pattern or switch statement to create the right adapter
            Adapter adapter = new SleepAdapter(programName, mainClass, directory);
            adapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }

//             TODO parse output from file
//            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
            // TODO how to read it an build the set of performance entries before returning
        }

        return null;
    }

    public static void logExecutedRegion(String programName, Set<String> configuration, Set<Region> regions, Region programRegion, Map<Region, Set<Region>> regionsToAllPossibleInnerRegions) throws IOException {
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, regions, programRegion);


        Executer.writeToFile(programName, performanceEntry, regionsToAllPossibleInnerRegions);
    }

    private static void writeToFile(String programName, PerformanceEntry measuredPerformance, Map<Region, Set<Region>> regionsToInnerRegions) throws IOException {
        JSONObject performanceEntry = new JSONObject();
        JSONArray values = new JSONArray();

        for(String value : measuredPerformance.getConfiguration()) {
            values.add(value);
        }

        performanceEntry.put(Executer.CONFIGURATION, values);

        JSONObject program = new JSONObject();
        Region measuredProgram = measuredPerformance.getProgram();
        program.put(Executer.ID, measuredProgram.getRegionID());
        program.put(Executer.START_TIME, measuredProgram.getStartTime());
        program.put(Executer.END_TIME, measuredProgram.getEndTime());

        performanceEntry.put(Executer.PROGRAM, program);

        JSONArray regions = new JSONArray();

        for(Region measuredRegion : measuredPerformance.getRegions()) {
            JSONObject region = new JSONObject();
            region.put(Executer.ID, measuredRegion.getRegionID());
            region.put(Executer.START_TIME, measuredRegion.getStartTime());
            region.put(Executer.END_TIME, measuredRegion.getEndTime());

            regions.add(region);
        }

        performanceEntry.put(Executer.REGIONS, regions);

        JSONArray regionsToInnerRegion = new JSONArray();

        for(Map.Entry<Region, Set<Region>> regionToInnerRegion : regionsToInnerRegions.entrySet()) {
            JSONObject region = new JSONObject();
            region.put(Executer.ID, regionToInnerRegion.getKey().getRegionID());

            JSONArray innerRegions = new JSONArray();

            for(Region innerRegionEntry : regionToInnerRegion.getValue()) {
                JSONObject innerRegion = new JSONObject();
                innerRegion.put(Executer.ID, innerRegionEntry.getRegionID());

                innerRegions.add(innerRegion);
            }

            region.put(Executer.INNER_REGIONS, innerRegions);

            regionsToInnerRegion.add(region);
        }

        performanceEntry.put(Executer.REGIONS_TO_INNER_REGIONS, regionsToInnerRegion);

        JSONObject result = new JSONObject();
        result.put(Executer.PERFORMANCE_ENTRY, performanceEntry);

        File directory = new File(Executer.DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = Executer.DIRECTORY + "/" + programName + "_" + UUID.randomUUID().toString().substring(0, 8) + Options.DOT_JSON;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toJSONString());
        writer.flush();
        writer.close();
    }

}

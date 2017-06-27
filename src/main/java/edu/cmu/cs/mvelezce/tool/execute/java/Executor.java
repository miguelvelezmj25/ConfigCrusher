package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gpl.GPLAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Executor {
    public static final String DIRECTORY = Options.DIRECTORY + "/executor/java/programs";

    // JSON strings
    public static final String EXECUTIONS = "executions";
    public static final String CONFIGURATION = "configuration";
    public static final String EXECUTION_TRACE = "executionTrace";
    public static final String ID = "ID";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args) throws IOException, ParseException {
        Options.getCommandLine(args);

        String outputFile = Executor.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);
        Set<PerformanceEntry> measuredPerformance = null;

        if(file.exists()) {
            try {
                measuredPerformance = Executor.readFromFile(file);
            }
            catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        return measuredPerformance;
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args, String entryPoint, String directory, Set<Set<String>> configurationsToExecute) throws IOException, ParseException {
        Set<PerformanceEntry> results = Executor.measureConfigurationPerformance(programName, args);

        if(results != null) {
            return results;
        }

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, entryPoint, directory, configurationsToExecute);

        return measuredPerformance;
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String mainClass, String directory, Set<Set<String>> configurationsToExecute) throws IOException, ParseException {
        for(Set<String> configuration : configurationsToExecute) {
            // TODO factory pattern or switch statement to create the right adapter

            Adapter adapter = null;

            if(programName.contains("elevator")) {
                adapter = new ElevatorAdapter(programName, mainClass, directory);
            }
            else if(programName.contains("gpl")) {
                adapter = new GPLAdapter(programName, mainClass, directory);
            }
            else if(programName.contains("sleep")) {
                adapter = new SleepAdapter(programName, mainClass, directory);
            }
            else {
                throw new RuntimeException("Could not create an adapter for " + programName);
            }

            adapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }
        }

        String outputFile = Executor.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        return Executor.readFromFile(file);
    }

    public static void logExecutedRegions(String programName, Set<String> configuration, List<Region> executedRegions) throws IOException, ParseException {
        // TODO why not just call the writeToFile method?
        Executor.writeToFile(programName, configuration, executedRegions);
    }

    private static void writeToFile(String programName, Set<String> configuration, List<Region> executedRegions) throws IOException, ParseException {
        JSONArray executions = new JSONArray();
        JSONObject measuredExecution = new JSONObject();
        JSONArray values = new JSONArray();

        for(String value : configuration) {
            values.add(value);
        }

        measuredExecution.put(Executor.CONFIGURATION, values);

        JSONArray regions = new JSONArray();

        for(Region executedRegion : executedRegions) {
            JSONObject region = new JSONObject();
            region.put(Executor.ID, executedRegion.getRegionID());
            region.put(Executor.START_TIME, executedRegion.getStartTime());
            region.put(Executor.END_TIME, executedRegion.getEndTime());

            regions.add(region);
        }

        measuredExecution.put(Executor.EXECUTION_TRACE, regions);
        executions.add(measuredExecution);

        // TODO check if file exists and append. Otherwise create
        String outputFile = Executor.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        if(file.exists()) {
            JSONParser parser = new JSONParser();
            JSONObject cache = (JSONObject) parser.parse(new FileReader(file));
            JSONArray executionsResult = (JSONArray) cache.get(Executor.EXECUTIONS);

            executions.addAll(executionsResult);
        }

        JSONObject result = new JSONObject();
        result.put(Executor.EXECUTIONS, executions);

        File directory = new File(Executor.DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        FileWriter writer = new FileWriter(file);
        writer.write(result.toJSONString());
        writer.flush();
        writer.close();

    }

    private static Set<PerformanceEntry> readFromFile(File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject cache = (JSONObject) parser.parse(new FileReader(file));
        JSONArray result = (JSONArray) cache.get(Executor.EXECUTIONS);

        Set<PerformanceEntry> performanceEntries = new HashSet<>();

        for(Object resultEntry : result) {
            JSONObject execution = (JSONObject) resultEntry;
            Set<String> configuration = new HashSet<>();
            JSONArray configurationResult = (JSONArray) execution.get(Executor.CONFIGURATION);

            for(Object configurationResultEntry : configurationResult) {
                configuration.add((String) configurationResultEntry);
            }

            JSONArray executionTraceResult = (JSONArray) execution.get(Executor.EXECUTION_TRACE);
            List<Region> executionTrace = new LinkedList<>();

            for(Object executionTraceResultEntry : executionTraceResult) {
                JSONObject regionResult = (JSONObject) executionTraceResultEntry;
                String regionID = (String) regionResult.get(Executor.ID);
                long startTime  = (long) regionResult.get(Executor.START_TIME);
                long endTime = (long) regionResult.get(Executor.END_TIME);

                Region region = new Region(regionID, startTime, endTime);
                executionTrace.add(region);
            }


            PerformanceEntry performanceEntry = new PerformanceEntry(configuration, executionTrace);
            performanceEntries.add(performanceEntry);
        }

        return performanceEntries;
    }

}

package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gpl.GPLAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme.ZipmeAdapter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
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
 * Created by miguelvelez on 4/30/17.
 */
public class Executor {
    public static final String DIRECTORY = Options.DIRECTORY + "/executor/java/programs";
    public static final String UNDERSCORE = "_";

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
        Options.getCommandLine(args);
        Set<PerformanceEntry> measuredPerformance;
        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
        int iterations = Options.getIterations();

        for(int i = 0; i < iterations; i++) {
            Set<PerformanceEntry> results = Executor.measureConfigurationPerformance(programName + Executor.UNDERSCORE + i, args);

            if(results != null) {
//                return results;
                executionsPerformance.add(results);
                continue;
            }

            measuredPerformance = Executor.measureConfigurationPerformance(programName + Executor.UNDERSCORE + i, entryPoint, directory, configurationsToExecute);
            executionsPerformance.add(measuredPerformance);
        }

        return averageExecutions(executionsPerformance);
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
            else if(programName.contains("zipme")) {
                adapter = new ZipmeAdapter(programName, mainClass, directory);
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

    public static Set<PerformanceEntry> averageExecutions(List<Set<PerformanceEntry>> executionsPerformance) {
        if(executionsPerformance.size() == 1) {
            return executionsPerformance.get(0);
        }

        Set<PerformanceEntry> averagePerf = new HashSet<>(executionsPerformance.get(0));

        for(int i = 1; i < executionsPerformance.size(); i++) {
            Set<PerformanceEntry> measuredPerformance = executionsPerformance.get(i);

            for(PerformanceEntry averagePerfEntry : averagePerf) {
                for(PerformanceEntry perfEntry : measuredPerformance) {
                    if(!averagePerfEntry.getConfiguration().equals(perfEntry.getConfiguration())) {
                        continue;
                    }

                    if(averagePerfEntry.getRegionsToExecutionTime().size() != perfEntry.getRegionsToExecutionTime().size()) {
                        throw new RuntimeException("The number of executed regions do not match "
                                + averagePerfEntry.getRegionsToExecutionTime().size() + " vs "
                                + perfEntry.getRegionsToExecutionTime().size());
                    }

                    Iterator<Map.Entry<Region, Long>> averageRegionsToPerfIter = averagePerfEntry.getRegionsToExecutionTime().entrySet().iterator();
                    Iterator<Map.Entry<Region, Long>> regionsToPerfIter = perfEntry.getRegionsToExecutionTime().entrySet().iterator();

                    while(averageRegionsToPerfIter.hasNext() && regionsToPerfIter.hasNext()) {
                        Map.Entry<Region, Long> averageEntry = averageRegionsToPerfIter.next();
                        Map.Entry<Region, Long> newEntry = regionsToPerfIter.next();

                        if(!averageEntry.getKey().getRegionID().equals(newEntry.getKey().getRegionID())) {
                            throw new RuntimeException("The regions ID do not match " + averageEntry.getKey().getRegionID()
                                    + " vs " + newEntry.getKey().getRegionID());
                        }

                        long sum = averageEntry.getValue() + newEntry.getValue();
                        averageEntry.setValue(sum);
                    }
                }
            }
        }

        for(PerformanceEntry perfEntry : averagePerf) {
            for(Map.Entry<Region, Long> regionToPerf : perfEntry.getRegionsToExecutionTime().entrySet()) {
                Long average = regionToPerf.getValue() / executionsPerformance.size();
                regionToPerf.setValue(average);
            }
        }

        return averagePerf;
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

package edu.cmu.cs.mvelezce.tool.execute.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gpl.GPLAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.pngtastic.PngtasticAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme.ZipmeAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.pipeline.java.analysis.PerformanceStatistic;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
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

    private static Set<PerformanceEntry> returnIfExists(String programName) throws IOException, ParseException {
        String outputFile = Executor.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);
        Set<PerformanceEntry> measuredPerformance = null;

        if(file.exists()) {
            try {
                measuredPerformance = Executor.readFromFile(file);
            } catch (ParseException pe) {
                throw new RuntimeException("Could not parse the cached results");
            }
        }

        return measuredPerformance;
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args) throws IOException, ParseException {
        Options.getCommandLine(args);

        return Executor.returnIfExists(programName);
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args, String entryPoint, String directory, Set<Set<String>> configurationsToExecute) throws IOException, ParseException {
        Set<PerformanceEntry> results = Executor.measureConfigurationPerformance(programName, args);

        if(results != null) {
            return results;
        }

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, entryPoint, directory, configurationsToExecute);

        return measuredPerformance;
    }

    public static Set<PerformanceEntry> repeatMeasureConfigurationPerformance(String programName, String[] args) throws IOException, ParseException {
        Options.getCommandLine(args);
        int iterations = Options.getIterations();
        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();

        for(int i = 0; i < iterations; i++) {
            Set<PerformanceEntry> results = Executor.returnIfExists(programName + Executor.UNDERSCORE + i);

            if(results != null) {
                executionsPerformance.add(results);
            }

        }

        List<PerformanceStatistic> execStats = Executor.getExecutionsStats(executionsPerformance);
        Set<PerformanceEntry> processedRes = Executor.averageExecutions(execStats, executionsPerformance.get((0)));

        return processedRes;
    }

    public static Set<PerformanceEntry> repeatMeasureConfigurationPerformance(String programName, String[] args, String entryPoint, String directory, Set<Set<String>> configurationsToExecute) throws IOException, ParseException {
        Options.getCommandLine(args);
        Set<PerformanceEntry> measuredPerformance;
        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
        int iterations = Options.getIterations();

        for(int i = 0; i < iterations; i++) {
            Set<PerformanceEntry> results = Executor.returnIfExists(programName + Executor.UNDERSCORE + i);

            if(results != null) {
                executionsPerformance.add(results);
                continue;
            }

            measuredPerformance = Executor.measureConfigurationPerformance(programName + Executor.UNDERSCORE + i, entryPoint, directory, configurationsToExecute);
            executionsPerformance.add(measuredPerformance);
        }

        List<PerformanceStatistic> execStats = Executor.getExecutionsStats(executionsPerformance);
        Set<PerformanceEntry> processedRes = Executor.averageExecutions(execStats, executionsPerformance.get((0)));

        return processedRes;
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
            else if(programName.contains("pngtastic")) {
                adapter = new PngtasticAdapter(programName, mainClass, directory);
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
        System.out.println("Executed regions size: " + executedRegions.size());
        Executor.writeToFile(programName, configuration, executedRegions);
    }

    public static List<PerformanceStatistic> getExecutionsStats(List<Set<PerformanceEntry>> executionsPerformance) {
        Set<PerformanceEntry> entries = executionsPerformance.get(0);
        Map<Set<String>, PerformanceStatistic> regionsToPerfStat = new HashMap<>();

        for(PerformanceEntry entry : entries) {
            Map<Region, List<Long>> regionToValues = new LinkedHashMap<>();
            PerformanceStatistic perfStat = new PerformanceStatistic(entry.getConfiguration(), regionToValues);

            for(Map.Entry<Region, Long> regionsToTime : entry.getRegionsToExecutionTime().entrySet()) {
                List<Long> values = new ArrayList<>();
                values.add(regionsToTime.getValue());
                regionToValues.put(regionsToTime.getKey(), values);
            }

            regionsToPerfStat.put(entry.getConfiguration(), perfStat);
        }

        for(int i = 1; i < executionsPerformance.size(); i++) {
            entries = executionsPerformance.get(i);

            for(Map.Entry<Set<String>, PerformanceStatistic> regionToPerfStat : regionsToPerfStat.entrySet()) {
                for(PerformanceEntry entry : entries) {
                    if(!regionToPerfStat.getKey().equals(entry.getConfiguration())) {
                        continue;
                    }

                    if(regionToPerfStat.getValue().getRegionsToValues().size() != entry.getRegionsToExecutionTime().size()) {
                        throw new RuntimeException("The number of executed regions do not match "
                                + regionToPerfStat.getValue().getRegionsToValues().size() + " vs "
                                + entry.getRegionsToExecutionTime().size());
                    }

                    Iterator<Map.Entry<Region, List<Long>>> regionToPerfStatValuesIter = regionToPerfStat.getValue().getRegionsToValues().entrySet().iterator();
                    Iterator<Map.Entry<Region, Long>> entryRegionToValuesIter = entry.getRegionsToExecutionTime().entrySet().iterator();

                    while (regionToPerfStatValuesIter.hasNext() && entryRegionToValuesIter.hasNext()) {
                        Map.Entry<Region, List<Long>> regionToPerfStatValuesEntry = regionToPerfStatValuesIter.next();
                        Map.Entry<Region, Long> entryRegionToValuesEntry = entryRegionToValuesIter.next();

                        if(!regionToPerfStatValuesEntry.getKey().getRegionID().equals(entryRegionToValuesEntry.getKey().getRegionID())) {
                            throw new RuntimeException("The regions ID do not match "
                                    + regionToPerfStatValuesEntry.getKey().getRegionID()
                                    + " vs " + entryRegionToValuesEntry.getKey().getRegionID());
                        }

                        regionToPerfStatValuesEntry.getValue().add(entryRegionToValuesEntry.getValue());
                    }
                }
            }
        }

        List<PerformanceStatistic> perfStats = new ArrayList<>(regionsToPerfStat.values());

        for(PerformanceStatistic perfStat : perfStats) {
            perfStat.calculateMean();
            perfStat.calculateStd();
        }

        return perfStats;
    }

    public static Set<PerformanceEntry> averageExecutions(List<PerformanceStatistic> execStats, Set<PerformanceEntry> perfEntries) {
        Set<PerformanceEntry> processedRes = new HashSet<>();

        for(PerformanceStatistic perfStat : execStats) {
            for(PerformanceEntry perfEntry : perfEntries) {
                if(!perfStat.getConfiguration().equals(perfEntry.getConfiguration())) {
                    continue;
                }

                Map<Region, Long> regionsToTime = new LinkedHashMap<>();
                Iterator<Map.Entry<Region, Long>> regionIdsToMeanIter = perfStat.getRegionsToMean().entrySet().iterator();
                Iterator<Map.Entry<Region, Long>> regionsToTimeIter = perfEntry.getRegionsToExecutionTime().entrySet().iterator();

                // TODO check if this is correct

                while (regionIdsToMeanIter.hasNext() && regionsToTimeIter.hasNext()) {
                    regionsToTime.put(regionsToTimeIter.next().getKey(), regionIdsToMeanIter.next().getValue());
                }

                PerformanceEntry newPerfEntry = new PerformanceEntry(perfStat.getConfiguration(), regionsToTime, perfEntry.getRegionsToInnerRegions());
                processedRes.add(newPerfEntry);
            }
        }

        return processedRes;
    }

    public static Set<String> getOptions(String programName) throws IOException, ParseException {
        File file = new File("/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/src/main/java/edu/cmu/cs/mvelezce/tool/execute/java/options.json");
        JSONParser parser = new JSONParser();
        JSONObject cache = (JSONObject) parser.parse(new FileReader(file));
        JSONArray result = (JSONArray) cache.get(programName);

        Set<String> options = new HashSet<>();
        options.addAll(result.subList(0, result.size()));

        return options;
    }

    private static void writeToFile(String programName, Set<String> configuration, List<Region> executedRegions) throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = Executor.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        List<Execution> executions = new ArrayList<>();

        if(file.exists()) {
            executions = mapper.readValue(file, new TypeReference<List<Execution>>() {
            });
        }

        Execution execution = new Execution(configuration, executedRegions);
        executions.add(execution);

        mapper.writeValue(file, executions);
    }

    private static Set<PerformanceEntry> readFromFile(File file) throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        List<Execution> executions = mapper.readValue(file, new TypeReference<List<Execution>>() {
        });
        Set<PerformanceEntry> performanceEntries = new HashSet<>();

        for(Execution execution : executions) {
            PerformanceEntry perfEntry = new PerformanceEntry(execution.getConfiguration(), execution.getTrace());
            performanceEntries.add(perfEntry);
        }

        return performanceEntries;
    }

}

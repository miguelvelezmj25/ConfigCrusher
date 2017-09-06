package edu.cmu.cs.mvelezce.tool.execute.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.pipeline.java.analysis.PerformanceStatistic;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class BaseExecutor implements Executor {

    public static final String DIRECTORY = Options.DIRECTORY + "/executor/java/programs";

    private String programName;
    private String mainClass;
    private String dir;
    private Set<Set<String>> configurations;
    private int repetitions;

    public BaseExecutor(String programName, String mainClass, String dir, Set<Set<String>> configurations, int repetitions) {
        this.programName = programName;
        this.mainClass = mainClass;
        this.dir = dir;
        this.configurations = configurations;
        this.repetitions = repetitions;
    }

    @Override
    public Set<PerformanceEntry> execute(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputFile = BaseExecutor.DIRECTORY + "/" + programName;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            // TODO have to aggregate
            Execution execution = this.readFromFile(file);
            throw new RuntimeException();
        }

        Set<PerformanceEntry> performanceEntries = this.execute();

        // TODO
//        if(Options.checkIfSave()) {
//            this.writeToFile(performanceEntries);
//        }

        return performanceEntries;
    }

    @Override
    public Set<PerformanceEntry> execute() throws IOException {
        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();

        for(int i = 0; i < this.repetitions; i++) {
            Set<PerformanceEntry> results = this.execute(this.programName + "_" + i);

            executionsPerformance.add(results);
        }

        // TODO
//        List<PerformanceStatistic> execStats = BaseExecutor.getExecutionsStats(executionsPerformance);
//        Set<PerformanceEntry> processedRes = BaseExecutor.averageExecutions(execStats, executionsPerformance.get((0)));
        return null;
    }

    public String getProgramName() {
        return programName;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String getDir() {
        return dir;
    }

    public Set<Set<String>> getConfigurations() {
        return configurations;
    }

    public int getRepetitions() {
        return repetitions;
    }

    //    private static Set<PerformanceEntry> returnIfExists(String programName) throws IOException, ParseException {
//        String outputFile = BaseExecutor.DIRECTORY + "/" + programName + Options.DOT_JSON;
//        File file = new File(outputFile);
//
//        Options.checkIfDeleteResult(file);
//        Set<PerformanceEntry> measuredPerformance = null;
//
//        if(file.exists()) {
//            try {
//                measuredPerformance = BaseExecutor.readFromFile(file);
//            } catch (ParseException pe) {
//                throw new RuntimeException("Could not parse the cached results");
//            }
//        }
//
//        return measuredPerformance;
//    }

//    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args) throws IOException, ParseException {
//        Options.getCommandLine(args);
//
//        return BaseExecutor.returnIfExists(programName);
//    }

//    public static Set<PerformanceEntry> measureConfigurationPerformance(String programName, String[] args, String entryPoint, String directory, Set<Set<String>> configurationsToExecute) throws IOException, ParseException {
//        Set<PerformanceEntry> results = BaseExecutor.measureConfigurationPerformance(programName, args);
//
//        if(results != null) {
//            return results;
//        }
//
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, entryPoint, directory, configurationsToExecute);
//
//        return measuredPerformance;
//    }

//    public static Set<PerformanceEntry> repeatMeasureConfigurationPerformance(String programName, String[] args) throws IOException, ParseException {
//        Options.getCommandLine(args);
//        int iterations = Options.getIterations();
//        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
//
//        for(int i = 0; i < iterations; i++) {
//            Set<PerformanceEntry> results = BaseExecutor.returnIfExists(programName + BaseExecutor.UNDERSCORE + i);
//
//            if(results != null) {
//                executionsPerformance.add(results);
//            }
//
//        }
//
//        List<PerformanceStatistic> execStats = BaseExecutor.getExecutionsStats(executionsPerformance);
//        Set<PerformanceEntry> processedRes = BaseExecutor.averageExecutions(execStats, executionsPerformance.get((0)));
//
//        return processedRes;
//    }

//    public static Set<PerformanceEntry> repeatMeasureConfigurationPerformance(String programName, String[] args, String entryPoint, String directory, Set<Set<String>> configurationsToExecute) throws IOException, ParseException {
//        Options.getCommandLine(args);
//        Set<PerformanceEntry> measuredPerformance;
//        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
//        int iterations = Options.getIterations();
//
//        for(int i = 0; i < iterations; i++) {
//            Set<PerformanceEntry> results = BaseExecutor.returnIfExists(programName + BaseExecutor.UNDERSCORE + i);
//
//            if(results != null) {
//                executionsPerformance.add(results);
//                continue;
//            }
//
//            measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName + BaseExecutor.UNDERSCORE + i, entryPoint, directory, configurationsToExecute);
//            executionsPerformance.add(measuredPerformance);
//        }
//
//        List<PerformanceStatistic> execStats = BaseExecutor.getExecutionsStats(executionsPerformance);
//        Set<PerformanceEntry> processedRes = BaseExecutor.averageExecutions(execStats, executionsPerformance.get((0)));
//
//        return processedRes;
//    }

//    // TODO
//    public void logExecutedRegions(String programName, Set<String> configuration, List<Region> executedRegions) throws IOException, ParseException {
//        // TODO why not just call the writeToFile method?
//        System.out.println("Executed regions size: " + executedRegions.size());
//        this.writeToFile(programName, configuration, executedRegions);
//    }

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

    @Override
    public Set<String> getOptions(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Set<String> options = mapper.readValue(file, new TypeReference<Set<String>>() {
        });

        return options;
    }

    @Override
    public void writeToFile(String programName, Set<String> configuration, List<Region> executedRegions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BaseExecutor.DIRECTORY + "/" + programName + "/" + UUID.randomUUID() + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

//        List<Execution> executions = new ArrayList<>();
//
//        if(file.exists()) {
//            executions = mapper.readValue(file, new TypeReference<List<Execution>>() {
//            });
//        }

        Execution execution = new Execution(configuration, executedRegions);
        mapper.writeValue(file, execution);
    }

    @Override
    public Execution readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Execution execution = mapper.readValue(file, new TypeReference<Execution>() {
        });

        return execution;
    }

}

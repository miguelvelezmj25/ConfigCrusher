package edu.cmu.cs.mvelezce.tool.execute.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceStatistic;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class BaseExecutor implements Executor {

    public static String DIRECTORY = Options.DIRECTORY + "/executor/java/approach/programs";

    private String programName;
    private String entryPoint;
    private String classDir;
    private Set<Set<String>> configurations;
    private int repetitions = 0;

    public BaseExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        this.programName = programName;
        this.entryPoint = entryPoint;
        this.classDir = classDir;
        this.configurations = configurations;
    }

//    public static List<PerformanceStatistic> getExecutionsStats(List<Set<PerformanceEntry>> executionsPerformance) {
//        Set<PerformanceEntry> entries = executionsPerformance.get(0);
//        Map<Set<String>, PerformanceStatistic> regionsToPerfStat = new HashMap<>();
//
//        for(PerformanceEntry entry : entries) {
//            Map<Region, List<Long>> regionToValues = new LinkedHashMap<>();
//            PerformanceStatistic perfStat = new PerformanceStatistic(entry.getConfiguration(), regionToValues);
//
//            for(Map.Entry<Region, Long> regionsToTime : entry.getRegionsToExecutionTime().entrySet()) {
//                List<Long> values = new ArrayList<>();
//                values.add(regionsToTime.getValue());
//                regionToValues.put(regionsToTime.getKey(), values);
//            }
//
//            regionsToPerfStat.put(entry.getConfiguration(), perfStat);
//        }
//
//        for(int i = 1; i < executionsPerformance.size(); i++) {
//            entries = executionsPerformance.get(i);
//
//            for(Map.Entry<Set<String>, PerformanceStatistic> regionToPerfStat : regionsToPerfStat.entrySet()) {
//                for(PerformanceEntry entry : entries) {
//                    if(!regionToPerfStat.getKey().equals(entry.getConfiguration())) {
//                        continue;
//                    }
//
//                    if(regionToPerfStat.getValue().getRegionsToValues().size() != entry.getRegionsToExecutionTime().size()) {
//                        throw new RuntimeException("The number of executed regions do not match "
//                                + regionToPerfStat.getValue().getRegionsToValues().size() + " vs "
//                                + entry.getRegionsToExecutionTime().size());
//                    }
//
//                    Iterator<Map.Entry<Region, List<Long>>> regionToPerfStatValuesIter = regionToPerfStat.getValue().getRegionsToValues().entrySet().iterator();
//                    Iterator<Map.Entry<Region, Long>> entryRegionToValuesIter = entry.getRegionsToExecutionTime().entrySet().iterator();
//
//                    while (regionToPerfStatValuesIter.hasNext() && entryRegionToValuesIter.hasNext()) {
//                        Map.Entry<Region, List<Long>> regionToPerfStatValuesEntry = regionToPerfStatValuesIter.next();
//                        Map.Entry<Region, Long> entryRegionToValuesEntry = entryRegionToValuesIter.next();
//
//                        if(!regionToPerfStatValuesEntry.getKey().getRegionID().equals(entryRegionToValuesEntry.getKey().getRegionID())) {
//                            throw new RuntimeException("The regions ID do not match "
//                                    + regionToPerfStatValuesEntry.getKey().getRegionID()
//                                    + " vs " + entryRegionToValuesEntry.getKey().getRegionID());
//                        }
//
//                        regionToPerfStatValuesEntry.getValue().add(entryRegionToValuesEntry.getValue());
//                    }
//                }
//            }
//        }
//
//        List<PerformanceStatistic> perfStats = new ArrayList<>(regionsToPerfStat.values());
//
//        for(PerformanceStatistic perfStat : perfStats) {
//            perfStat.calculateMean();
//            perfStat.calculateStd();
//        }
//
//        return perfStats;
//    }
//
//    public static Set<PerformanceEntry> averageExecutions(List<PerformanceStatistic> execStats, Set<PerformanceEntry> perfEntries) {
//        Set<PerformanceEntry> processedRes = new HashSet<>();
//
//        for(PerformanceStatistic perfStat : execStats) {
//            for(PerformanceEntry perfEntry : perfEntries) {
//                if(!perfStat.getConfiguration().equals(perfEntry.getConfiguration())) {
//                    continue;
//                }
//
//                Map<Region, Long> regionsToTime = new LinkedHashMap<>();
//                Iterator<Map.Entry<Region, Long>> regionIdsToMeanIter = perfStat.getRegionsToMean().entrySet().iterator();
//                Iterator<Map.Entry<Region, Long>> regionsToTimeIter = perfEntry.getRegionsToExecutionTime().entrySet().iterator();
//
//                // TODO check if this is correct
//
//                while (regionIdsToMeanIter.hasNext() && regionsToTimeIter.hasNext()) {
//                    regionsToTime.put(regionsToTimeIter.next().getKey(), regionIdsToMeanIter.next().getValue());
//                }
//
//                PerformanceEntry newPerfEntry = new PerformanceEntry(perfStat.getConfiguration(), regionsToTime, perfEntry.getRegionsToInnerRegions());
//                processedRes.add(newPerfEntry);
//            }
//        }
//
//        return processedRes;
//    }

    private Set<DefaultPerformanceEntry> averageExecutions(List<Set<DefaultPerformanceEntry>> performanceEntriesList) {
        Set<DefaultPerformanceEntry> performanceEntrySet = performanceEntriesList.iterator().next();
        Set<Set<String>> configurations = new HashSet<>();

        for(DefaultPerformanceEntry performanceEntry : performanceEntrySet) {
            configurations.add(performanceEntry.getConfiguration());
        }

        Set<DefaultPerformanceEntry> result = new HashSet<>();

        for(Set<String> configuration : configurations) {
            List<DefaultPerformanceEntry> sameConfigEntries = new ArrayList<>();

            for(Set<DefaultPerformanceEntry> performanceEntries : performanceEntriesList) {
                for(DefaultPerformanceEntry performanceEntry : performanceEntries) {
                    if(performanceEntry.getConfiguration().equals(configuration)) {
                        sameConfigEntries.add(performanceEntry);
                        break;
                    }
                }
            }

            PerformanceStatistic performanceStatistic = new PerformanceStatistic(true, sameConfigEntries);

            DefaultPerformanceEntry performanceEntry = new DefaultPerformanceEntry(performanceStatistic.getConfiguration());
            performanceEntry.setRegionsToRawPerformance(performanceStatistic.getRegionsToRawMean());
            performanceEntry.setRegionsToRawPerformanceHumanReadable(performanceStatistic.getRegionsToRawMeanHumanReadReadable());
            performanceEntry.setRegionsToProcessedPerformance(performanceStatistic.getRegionsToProcessedMean());
            performanceEntry.setRegionsToProcessedPerformanceHumanReadable(performanceStatistic.getRegionsToProcessedMeanHumanReadable());

            result.add(performanceEntry);
        }

        return result;
    }

    @Override
    public Set<DefaultPerformanceEntry> execute(String[] args) throws IOException, InterruptedException {
        Options.getCommandLine(args);

        String outputDir = BaseExecutor.DIRECTORY + "/" + this.programName;
        File root = new File(outputDir);

        Options.checkIfDeleteResult(root);

        if(root.exists()) {
            List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();

            int i = 0;
            File outputFile = new File(root + "/" + i);

            while (outputFile.exists()) {
                Set<DefaultPerformanceEntry> results = this.aggregateExecutions(root);
                performanceEntriesList.add(results);

                i++;
                outputFile = new File(root + "/" + i);
            }

            Set<DefaultPerformanceEntry> averagedPerformanceEntries = this.averageExecutions(performanceEntriesList);
            return averagedPerformanceEntries;
        }

        this.repetitions = Options.getIterations();
        Set<DefaultPerformanceEntry> performanceEntries = this.execute();

        return performanceEntries;
    }

    @Override
    public Set<DefaultPerformanceEntry> execute() throws IOException, InterruptedException {
        List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();

        for(int i = 0; i < this.repetitions; i++) {
            Set<DefaultPerformanceEntry> results = this.execute(i);
            performanceEntriesList.add(results);
        }

        Set<DefaultPerformanceEntry> averagedPerformanceEntries = this.averageExecutions(performanceEntriesList);

        return averagedPerformanceEntries;
    }

    public String getProgramName() {
        return programName;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public String getClassDir() {
        return classDir;
    }

    public Set<Set<String>> getConfigurations() {
        return configurations;
    }

    public int getRepetitions() {
        return repetitions;
    }

    protected Set<DefaultPerformanceEntry> aggregateExecutions(File outputFile) throws IOException {
        Collection<File> files = FileUtils.listFiles(outputFile, null, true);
        Set<DefaultPerformanceEntry> performanceEntries = new HashSet<>();

        for(File file : files) {
            DefaultPerformanceEntry result = this.readFromFile(file);
            performanceEntries.add(result);
        }

        return performanceEntries;
    }

    @Override
    public Set<String> getOptions(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Set<String> options = mapper.readValue(file, new TypeReference<Set<String>>() {
        });

        return options;
    }

    @Override
    public void writeToFile(String iteration, Set<String> configuration, List<Region> executedRegions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BaseExecutor.DIRECTORY + "/" + this.programName + "/" + iteration + "/" + UUID.randomUUID()
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        Execution execution = new Execution(configuration, executedRegions);
        mapper.writeValue(file, execution);
        execution.checkTrace();
    }

    @Override
    public DefaultPerformanceEntry readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Execution execution = mapper.readValue(file, new TypeReference<Execution>() {
        });

        execution.checkTrace();
        DefaultPerformanceEntry performanceEntry = new DefaultPerformanceEntry(execution);

        return performanceEntry;
    }

}

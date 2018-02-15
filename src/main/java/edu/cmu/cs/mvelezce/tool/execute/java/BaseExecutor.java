package edu.cmu.cs.mvelezce.tool.execute.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by miguelvelez on 4/30/17.
 */
public abstract class BaseExecutor implements Executor {

    public static String DIRECTORY = Options.DIRECTORY + "/executor/java";

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

    protected Set<PerformanceEntryStatistic> averageExecutions(List<Set<DefaultPerformanceEntry>> performanceEntriesList) {
        Set<DefaultPerformanceEntry> performanceEntrySet = performanceEntriesList.iterator().next();
        Set<Set<String>> configurations = new HashSet<>();

        // Get configurations
        for(DefaultPerformanceEntry performanceEntry : performanceEntrySet) {
            configurations.add(performanceEntry.getConfiguration());
        }

        Set<PerformanceEntryStatistic> results = new HashSet<>();

        // Get results
        for(DefaultPerformanceEntry performanceEntry : performanceEntrySet) {
            PerformanceEntryStatistic entryStatistic = new PerformanceEntryStatistic(true, performanceEntry.getConfiguration());
//            entryStatistic.setRegionsToRawPerformance(performanceEntry.getRegionsToRawPerformance());
//            entryStatistic.setRegionsToRawPerformanceHumanReadable(performanceEntry.getRegionsToRawPerformanceHumanReadable());
//            entryStatistic.setRegionsToInnerRegions(performanceEntry.getRegionsToInnerRegions());
//            entryStatistic.setRegionsToProcessedPerformance(performanceEntry.getRegionsToProcessedPerformance());
//            entryStatistic.setRegionsToProcessedPerformanceHumanReadable(performanceEntry.getRegionsToProcessedPerformanceHumanReadable());
//
            results.add(entryStatistic);
        }

        // Calculate mean
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

            for(PerformanceEntryStatistic performanceEntryStatistic : results) {
                if(!performanceEntryStatistic.getConfiguration().equals(configuration)) {
                    continue;
                }

                performanceEntryStatistic.calculateMinMax(sameConfigEntries);
                performanceEntryStatistic.calculateMean(sameConfigEntries);
                performanceEntryStatistic.calculateStd(sameConfigEntries);
//                performanceEntryStatistic.calculateCI(sameConfigEntries);

                break;
            }

        }

        for(PerformanceEntryStatistic performanceEntryStatistic : results) {
            // Min
//            performanceEntryStatistic.setRegionsToRawPerformance(performanceEntryStatistic.getRegionsRawMin());
//            performanceEntryStatistic.setRegionsToRawPerformanceHumanReadable(performanceEntryStatistic.getRegionsRawMinHumanReadable());
//            performanceEntryStatistic.setRegionsToProcessedPerformance(performanceEntryStatistic.getRegionsProcessedMin());
//            performanceEntryStatistic.setRegionsToProcessedPerformanceHumanReadable(performanceEntryStatistic.getRegionsMinProcessedHumanReadable());

            // Mean
            performanceEntryStatistic.setRegionsToRawPerformance(performanceEntryStatistic.getRegionsRawMean());
            performanceEntryStatistic.setRegionsToRawPerformanceHumanReadable(performanceEntryStatistic.getRegionsRawMeanHumanReadable());
            performanceEntryStatistic.setRegionsToProcessedPerformance(performanceEntryStatistic.getRegionsProcessedMean());
            performanceEntryStatistic.setRegionsToProcessedPerformanceHumanReadable(performanceEntryStatistic.getRegionsMeanProcessedHumanReadable());
        }

        return results;
    }

    @Override
    public Set<PerformanceEntryStatistic> execute(String[] args) throws IOException, InterruptedException {
        Options.getCommandLine(args);

        String outputDir = this.getOutputDir() + "/" + this.programName;
        File root = new File(outputDir);

        Options.checkIfDeleteResult(root);

        if(root.exists()) {
            List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();

            int i = 0;
            File outputFile = new File(root + "/" + i);

            while(outputFile.exists()) {
                Set<DefaultPerformanceEntry> results = this.aggregateExecutions(outputFile);
                performanceEntriesList.add(results);

                i++;
                outputFile = new File(root + "/" + i);
            }

            Set<PerformanceEntryStatistic> averagedPerformanceEntries = this.averageExecutions(performanceEntriesList);
            return averagedPerformanceEntries;
        }

        this.repetitions = Options.getIterations();
        Set<PerformanceEntryStatistic> performanceEntries = this.execute();

        return performanceEntries;
    }

    @Override
    public Set<PerformanceEntryStatistic> execute() throws IOException, InterruptedException {
        List<Set<DefaultPerformanceEntry>> performanceEntriesList = new ArrayList<>();

        for(int i = 0; i < this.repetitions; i++) {
            Set<DefaultPerformanceEntry> results = this.execute(i);

            if(i < 0) {
                continue;
            }

            performanceEntriesList.add(results);

            System.gc();
            Thread.sleep(5000);
        }

        Set<PerformanceEntryStatistic> averagedPerformanceEntries = this.averageExecutions(performanceEntriesList);

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
        Collection<File> files = FileUtils.listFiles(outputFile, new String[]{"json"}, true);
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
    public void writeToFile(String iteration, Set<String> configuration, Map<String, Long> regionsToProcessedPerformance) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = this.getOutputDir() + "/" + this.programName + "/" + iteration + "/" + UUID.randomUUID()
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        Execution execution = new Execution(configuration, regionsToProcessedPerformance);
        mapper.writeValue(file, execution);
//        execution.checkTrace();
    }

//    @Override
//    public void writeToFile(String iteration, Set<String> configuration, List<Region> executedRegions) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        String outputFile = BaseExecutor.DIRECTORY + "/" + this.programName + "/" + iteration + "/" + UUID.randomUUID()
//                + Options.DOT_JSON;
//        File file = new File(outputFile);
//        file.getParentFile().mkdirs();
//
//        Execution execution = new Execution(configuration, executedRegions);
//        mapper.writeValue(file, execution);
////        execution.checkTrace();
//    }

    @Override
    public DefaultPerformanceEntry readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Execution execution = mapper.readValue(file, new TypeReference<Execution>() {
        });

//        execution.checkTrace();
        DefaultPerformanceEntry performanceEntry = new DefaultPerformanceEntry(execution);

        return performanceEntry;
    }

}

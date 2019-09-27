package edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.evaluation.approaches.Approach;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.SPLatDelayCompression;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.SPLatDelayMain;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SPLatDelay extends Approach {

    public SPLatDelay(String programName) {
        super(programName);
    }

    @Override
    public void generateCSVData(Set<PerformanceEntryStatistic> performanceEntries, List<String> options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Set<String>, Double> getLearnedModel(List<String> options) {
        throw new UnsupportedOperationException();
    }


    public List<Coverage> readFileCoverage() throws IOException {
        String coverageFile = SPLatDelayMain.DIRECTORY + "/" + this.getProgramName() + "/coverage" + Options.DOT_JSON;
        File file = new File(coverageFile);

        ObjectMapper mapper = new ObjectMapper();
        List<Coverage> results = mapper.readValue(file, new TypeReference<List<Coverage>>() {
        });

        return results;
    }



    public Set<PerformanceEntryStatistic> getSPLatEntries(Set<PerformanceEntryStatistic> performanceEntries) {
//        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
//        Set<Set<String>> splatConfigurations = this.getSPLatDelayConfigurations(configurations);
        Set<Set<String>> splatDelayConfigurations = this.getSPLatDelayConfigurations();

        Set<PerformanceEntryStatistic> splatDelayEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!splatDelayConfigurations.contains(configuration)) {
                continue;
            }

            splatDelayEntries.add(statistic);
        }

        return splatDelayEntries;
    }

    public Set<Set<String>> getSPLatDelayConfigurations() {
        String[] args = new String[0];
        Compression<Set<Set<String>>> compressor = new SPLatDelayCompression(this.getProgramName());
        Set<Set<String>> configurationsToExecute = new HashSet<>();

        try {
            configurationsToExecute = compressor.compressConfigurations(args);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return configurationsToExecute;
    }

    private Set<String> getOptions(Set<Set<String>> configurations) {
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        return options;
    }

    private Set<Set<String>> getConfigurations(Set<PerformanceEntryStatistic> performanceEntryStatistics) {
        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntryStatistics) {
            Set<String> configuration = statistic.getConfiguration();
            configurations.add(configuration);
        }

        return configurations;
    }

}

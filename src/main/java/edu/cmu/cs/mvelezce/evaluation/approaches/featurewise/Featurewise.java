package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Featurewise extends Evaluation {

    public static final String R_DIR = "/r";

    public Featurewise(String programName) {
        super(programName);
    }

    public void something(Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
        Set<String> optionsSet = this.getOptions(configurations);
        List<String> options = new ArrayList<>();
        options.addAll(optionsSet);

        StringBuilder result = new StringBuilder();

        for(String option : options) {
            result.append(option);
            result.append(",");
        }

        result.append("time");
        result.append("\n");

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            for(String option : options) {
                if(configuration.contains(option)) {
                    result.append("1");
                } else {
                    result.append("0");
                }

                result.append(",");
            }

            double performance = statistic.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
            result.append(decimalFormat.format(performance));
            result.append("\n");
        }

        String outputDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + "/" + Featurewise.R_DIR + "/"
                + Evaluation.FEATURE_WISE + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public Set<PerformanceEntryStatistic> getFeaturewiseEntries(Set<PerformanceEntryStatistic> performanceEntries) {
        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
        Set<Set<String>> featurewiseConfigurations = this.getFeaturewiseConfigurations(configurations);

        Set<PerformanceEntryStatistic> featurewiseEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!featurewiseConfigurations.contains(configuration)) {
                continue;
            }

            featurewiseEntries.add(statistic);
        }

        return featurewiseEntries;
    }

    private Set<Set<String>> getFeaturewiseConfigurations(Set<Set<String>> configurations) {
        Set<String> options = this.getOptions(configurations);
        Set<Set<String>> featurewiseConfigurations = new HashSet<>();

        for(String option : options) {
            Set<String> configuration = new HashSet<>();
            configuration.add(option);

            featurewiseConfigurations.add(configuration);
        }

        return featurewiseConfigurations;
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

package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.evaluation.approaches.Approach;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Featurewise extends Approach {

    public Featurewise(String programName) {
        super(programName);
    }

//    public PerformanceModel createModel(String output) {
//
//    }

//    public String execute(String file) throws IOException, InterruptedException {
//        List<String> commandList = new ArrayList<>();
//
//        commandList.add("Rscript");
//        commandList.add(file);
//
//        String[] command = new String[commandList.size()];
//        command = commandList.toArray(command);
//        System.out.println(Arrays.toString(command));
//        Process process = Runtime.getRuntime().exec(command);
//
//        System.out.println("Output: ");
//        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String string;
//
//        StringBuilder output = new StringBuilder();
//
//        while((string = inputReader.readLine()) != null) {
//            if(!string.isEmpty()) {
//                System.out.println(string);
//                output.append(string).append("\n");
//            }
//        }
//
//        System.out.println("Errors: ");
//        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//        while((string = errorReader.readLine()) != null) {
//            if(!string.isEmpty()) {
//                System.out.println(string);
//            }
//        }
//
//        process.waitFor();
//        System.out.println();
//
//        return output.toString();
//    }

//    public String generateRScript(Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
//        String file = this.generateRScriptData(performanceEntries);
//
//        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
//        Set<String> options = this.getOptions(configurations);
//
//        return this.generateRScript(file, options);
//    }

//    private String generateRScript(String file, Set<String> options) throws IOException {
//        StringBuilder script = new StringBuilder();
//        script.append("feature_wise <- read.csv(\"");
//        script.append(file);
//        script.append("\")");
//        script.append("\n");
//        script.append("model <- lm(time~");
//
//        Iterator<String> optionsIter = options.iterator();
//
//        while(optionsIter.hasNext()) {
//            String option = optionsIter.next();
//            script.append(option);
//
//            if(optionsIter.hasNext()) {
//                script.append("+");
//            }
//        }
//
//        script.append(", data = feature_wise)");
//        script.append("\n");
//        script.append("coef(model)");
//        script.append("\n");
//
//        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + Featurewise.DATA_DIR + "/"
//                + Evaluation.FEATURE_WISE + Featurewise.DOT_R;
//        File outputFile = new File(outputDir);
//
//        if(outputFile.exists()) {
//            FileUtils.forceDelete(outputFile);
//        }
//
//        outputFile.getParentFile().mkdirs();
//        FileWriter writer = new FileWriter(outputFile);
//        writer.write(script.toString());
//        writer.flush();
//        writer.close();
//
//        return outputDir;
//    }

    @Override
    public void generateCSVData(Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
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
                }
                else {
                    result.append("0");
                }

                result.append(",");
            }

            double performance = statistic.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
            result.append(decimalFormat.format(performance));
            result.append("\n");
        }

        String outputDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR + "/"
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

    @Override
    public Map<Set<String>, Double> getLearnedModel(List<String> options) throws IOException {
        return null;
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

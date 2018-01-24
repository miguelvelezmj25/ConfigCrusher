package edu.cmu.cs.mvelezce.evaluation.approaches.pairwise;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.util.Combinations;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Pairwise {

    public static final String R_DIR = "/r";
    public static final String DOT_R = ".R";
    public static final String INTERCEPT = "(Intercept)";
    public static final String NA = "NA";

    private String programName;

    public Pairwise(String programName) {
        this.programName = programName;
    }

//    public PerformanceModel createModel(String output) {
//
//    }

    public String execute(String file) throws IOException, InterruptedException {
        List<String> commandList = new ArrayList<>();

        commandList.add("Rscript");
        commandList.add(file);

        String[] command = new String[commandList.size()];
        command = commandList.toArray(command);
        System.out.println(Arrays.toString(command));
        Process process = Runtime.getRuntime().exec(command);

        System.out.println("Output: ");
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String string;

        StringBuilder output = new StringBuilder();

        while((string = inputReader.readLine()) != null) {
            if(!string.isEmpty()) {
                System.out.println(string);
                output.append(string).append("\n");
            }
        }

        System.out.println("Errors: ");
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        while((string = errorReader.readLine()) != null) {
            if(!string.isEmpty()) {
                System.out.println(string);
            }
        }

        process.waitFor();
        System.out.println();

        return output.toString();
    }

    public String generateRScript(Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
        String file = this.generateRScriptData(performanceEntries);

        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
        Set<String> options = this.getOptions(configurations);

        return this.generateRScript(file, options);
    }

    private String generateRScript(String file, Set<String> options) throws IOException {
        StringBuilder script = new StringBuilder();
        script.append("pair_wise <- read.csv(\"");
        script.append(file);
        script.append("\")");
        script.append("\n");
        script.append("model <- lm(time~");


        List<String> optionsList = new ArrayList<>(options);

        for(int i = 1; i <= options.size(); i++) {
            Combinations combinations = new Combinations(options.size(), i);
            Iterator<int[]> combinationsIter = combinations.iterator();

            while(combinationsIter.hasNext()) {
                int[] combination = combinationsIter.next();

                for(int j = 0; j < combination.length; j++) {
                    int index = combination[j];
                    String term = optionsList.get(index);
                    script.append(term);

                    if(j < (combination.length - 1)) {
                        script.append("*");
                    }
                }

                if(combinationsIter.hasNext()) {
                    script.append("+");
                }

            }

            if(i < options.size()) {
                script.append("+");
            }

        }

        script.append(", data = pair_wise)");
        script.append("\n");
        script.append("coef(model)");
        script.append("\n");

        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + Pairwise.R_DIR + "/"
                + Evaluation.PAIR_WISE + Pairwise.DOT_R;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(script.toString());
        writer.flush();
        writer.close();

        return outputDir;
    }


    private String generateRScriptData(Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
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

        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + Pairwise.R_DIR + "/"
                + Evaluation.PAIR_WISE + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();

        return outputDir;
    }

    public Set<PerformanceEntryStatistic> getPairwiseEntries(Set<PerformanceEntryStatistic> performanceEntries) {
        Set<Set<String>> allConfigurations = this.getConfigurations(performanceEntries);
        Set<Set<String>> pairwiseConfigurations = this.getPairwiseConfigurations(allConfigurations);

        Set<PerformanceEntryStatistic> pairwiseEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!pairwiseConfigurations.contains(configuration)) {
                continue;
            }

            pairwiseEntries.add(statistic);
        }

        return pairwiseEntries;
    }

    private Set<Set<String>> getPairwiseConfigurations(Set<Set<String>> allConfigurations) {
        Set<String> options = this.getOptions(allConfigurations);
        Set<List<String>> pairs = this.getPairs(options);

        Set<Set<String>> pairwiseConfigurations = new HashSet<>();

        for(List<String> pair : pairs) {
            Set<Set<String>> pairConfigurations = this.getConfigurations(pair);
            pairwiseConfigurations.addAll(pairConfigurations);
        }

        return pairwiseConfigurations;
    }

    private Set<Set<String>> getConfigurations(List<String> pair) {
        Set<Set<String>> configurations = new HashSet<>();

        Set<String> configuration = new HashSet<>();
        configurations.add(configuration);

        for(int i = 1; i <= pair.size(); i++) {
            Combinations combinations = new Combinations(pair.size(), i);

            for(int[] combination : combinations) {
                configuration = new HashSet<>();

                for(int j = 0; j < combination.length; j++) {
                    int index = combination[j];
                    configuration.add(pair.get(index));
                }

                configurations.add(configuration);
            }
        }

        return configurations;
    }

    private Set<List<String>> getPairs(Set<String> options) {
        List<String> optionsList = new ArrayList<>(options);

        Combinations combinations = new Combinations(options.size(), 2);
        Set<List<String>> pairs = new HashSet<>();

        for(int[] combination : combinations) {
            List<String> pair = new ArrayList<>();

            for(int i = 0; i < combination.length; i++) {
                int index = combination[i];
                pair.add(optionsList.get(index));
            }

            pairs.add(pair);
        }

        return pairs;
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

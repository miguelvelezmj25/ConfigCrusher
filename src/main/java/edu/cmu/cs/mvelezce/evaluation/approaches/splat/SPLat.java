package edu.cmu.cs.mvelezce.evaluation.approaches.splat;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.SPLatExecutor;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class SPLat {

    public static final String R_DIR = "/r";
    public static final String DOT_R = ".R";
    public static final String INTERCEPT = "(Intercept)";
    public static final String NA = "NA";

    private String programName;

    public SPLat(String programName) {
        this.programName = programName;
    }

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

//        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
//        Set<String> options = this.getOptions(configurations);

//        return this.generateRScript(file, options);
        return this.generateRScript(file, performanceEntries);
    }

//    private String generateRScript(String file, Set<String> options) throws IOException {
    private String generateRScript(String file, Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
        StringBuilder script = new StringBuilder();
        script.append("splat <- read.csv(\"");
        script.append(file);
        script.append("\")");
        script.append("\n");
        script.append("model <- lm(time~");

        Iterator<PerformanceEntryStatistic> entriesIt = performanceEntries.iterator();

        while(entriesIt.hasNext()) {
            Set<String> configuration = entriesIt.next().getConfiguration();

            if(configuration.isEmpty()) {
                continue;
            }

            Iterator<String> configurationIt = configuration.iterator();

            while(configurationIt.hasNext()) {
                String option = configurationIt.next();
                script.append(option);

                if(configurationIt.hasNext()) {
                    script.append("*");
                }
            }

            if(entriesIt.hasNext()) {
                script.append("+");
            }
        }

        script.append(", data = splat)");
        script.append("\n");
        script.append("coef(model)");
        script.append("\n");

        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + SPLat.R_DIR + "/"
                + Evaluation.SPLAT + SPLat.DOT_R;
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

        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + SPLat.R_DIR + "/"
                + Evaluation.SPLAT + Evaluation.DOT_CSV;
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

    public Set<PerformanceEntryStatistic> getSPLatEntries(Set<PerformanceEntryStatistic> performanceEntries) {
//        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
//        Set<Set<String>> splatConfigurations = this.getSPLatConfigurations(configurations);
        Set<Set<String>> splatConfigurations = this.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!splatConfigurations.contains(configuration)) {
                continue;
            }

            splatEntries.add(statistic);
        }

        return splatEntries;
    }

    public Set<Set<String>> getSPLatConfigurations(/*Set<Set<String>> configurations*/) {
        String[] args = new String[0];
        Compression compressor = new SPLatExecutor(programName);
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

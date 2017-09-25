package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Evaluation {

    public static final String DIRECTORY = Options.DIRECTORY + "/evaluation/programs/java";
    public static final String COMPARISON_DIR = "/comparison";
    public static final String FULL_DIR = "/full";
    public static final String DOT_CSV = ".csv";

    // TODO use a class or enum>
    public static final String CONFIG_CRUSHER = "config_crusher";
    public static final String BRUTE_FORCE = "brute_force";

    private String programName;

    public Evaluation(String programName) {
        this.programName = programName;
    }

    public void writeConfigurationToPerformance(String approach, Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance");//;,std");
        result.append("\n");

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        for(DefaultPerformanceEntry performanceEntry : performanceEntries) {
            if(performanceEntry.getRegionsToProcessedPerformanceHumanReadable().size() != 1) {
                throw new RuntimeException("This method can only handle approaches that measure 1 region" +
                        " (e.g. Brute force)");
            }

            result.append("true");
            result.append(",");
            result.append('"');
            result.append(performanceEntry.getConfiguration());
            result.append('"');
            result.append(",");
            double performance = performanceEntry.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
            result.append(decimalFormat.format(performance));
//            result.append(",");
//            result.append(perfStat.getRegionsToStd().values().iterator().next() / 1000000000.0);
            result.append("\n");
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public void writeConfigurationToPerformance(String approach, PerformanceModel performanceModel) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        Collection<Map<Set<String>, Long>> performanceTables = performanceModel.getRegionsToPerformanceTables().values();
        Set<Set<String>> options = new HashSet<>();

        for(Map<Set<String>, Long> entry : performanceTables) {
            options.addAll(entry.keySet());
        }

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurations(options);

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance,std");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            PerformanceEntryStatistic performanceStat = null;

//            for(PerformanceEntryStatistic performanceEntryStatistic : performanceEntryStatistics) {
//                if(performanceEntryStatistic.getConfiguration().equals(configuration)) {
//                    performanceStat = performanceEntryStatistic;
//                    break;
//                }
//            }

            if(performanceStat != null) {
                result.append(true);
            }
            else {
                result.append(false);
            }

            result.append(",");
            result.append('"');
            result.append(configuration);
            result.append('"');
            result.append(",");
            result.append(performanceModel.evaluate(configuration));
            result.append('"');
            result.append(",");

            if(performanceStat != null) {
//                result.append(performanceStat.getProcessedStd());
            }
            else {
                result.append("_");
            }

            result.append("\n");
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public void compareApproaches(String approach1, String approach2) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.FULL_DIR + "/"
                + approach1 + Evaluation.DOT_CSV;
        File outputFile1 = new File(outputDir);

        if(!outputFile1.exists()) {
            throw new IOException("Could not find a full file for " + approach1);
        }

        outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.FULL_DIR + "/"
                + approach2 + Evaluation.DOT_CSV;
        File outputFile2 = new File(outputDir);

        if(!outputFile2.exists()) {
            throw new IOException("Could not find a full file for " + approach2);
        }

        outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.COMPARISON_DIR + "/"
                + approach1 + "_" + approach2 + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        FileInputStream fstream = new FileInputStream(outputFile1);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int approach1LineCount = 0;

        while ((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                approach1LineCount++;
            }
        }

        in.close();

        fstream = new FileInputStream(outputFile2);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        int approach2LineCount = 0;

        while ((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                approach2LineCount++;
            }
        }

        in.close();

        if(approach1LineCount != approach2LineCount) {
            throw new RuntimeException("The approach files do not have the same length");
        }

        Set<Set<String>> configurations = new HashSet<>();

        fstream = new FileInputStream(outputFile1);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));

        while ((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                break;
            }
        }

        while ((strLine = br.readLine()) != null) {
            Set<String> options = new HashSet<>();
            String optionsString = strLine.substring("true,\"[".length(), strLine.lastIndexOf('"') - 1);
            String[] arrayOptions = optionsString.split(",");

            for(int i = 0; i < arrayOptions.length; i++) {
                options.add(arrayOptions[i].trim());
            }

            configurations.add(options);
        }

        in.close();

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        StringBuilder result = new StringBuilder();
        double se = 0;
        result.append("measured,configuration,ap1,ap2,absolute error,relative % error,squared error");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            result.append('"');
            result.append("TODO");
            result.append('"');
            result.append(",");
            result.append('"');
            result.append(configuration);
            result.append('"');
            result.append(",");

            fstream = new FileInputStream(outputFile1);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            br.readLine();

            while ((strLine = br.readLine()) != null) {
                Set<String> options = new HashSet<>();
                String optionsString = strLine.substring("true,\"[".length(), strLine.lastIndexOf('"') - 1);
                String[] arrayOptions = optionsString.split(",");

                for(int i = 0; i < arrayOptions.length; i++) {
                    options.add(arrayOptions[i].trim());
                }

                if(configuration.equals(options)) {
                    break;
                }
            }

            in.close();

            String[] entries = strLine.split(",");
            double performance1 = Double.valueOf(entries[entries.length - 1]);
            result.append(performance1);
            result.append(",");

            fstream = new FileInputStream(outputFile2);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            br.readLine();

            while ((strLine = br.readLine()) != null) {
                Set<String> options = new HashSet<>();
                String optionsString = strLine.substring("true,\"[".length(), strLine.lastIndexOf('"') - 1);
                String[] arrayOptions = optionsString.split(",");

                for(int i = 0; i < arrayOptions.length; i++) {
                    options.add(arrayOptions[i].trim());
                }

                if(configuration.equals(options)) {
                    break;
                }
            }

            in.close();

            entries = strLine.split(",");
            double performance2 = Double.valueOf(entries[entries.length - 1]);
            result.append(performance2);
            result.append(",");

            double absoluteError = Math.abs(performance2 - performance1);
            double relativeError = absoluteError / performance2 * 100.0;
            double squaredError = Math.pow(performance2 - performance1, 2);

            result.append(decimalFormat.format(absoluteError));
            result.append(",");
            result.append(decimalFormat.format(relativeError));
            result.append(",");
            result.append(decimalFormat.format(squaredError));
            result.append("\n");

            se = squaredError;
        }

        result.append("\n");
        result.append("MSE: ");
        double mse = se / configurations.size();
        result.append(decimalFormat.format(mse));
        result.append("\n");
        result.append("RMSE: ");
        result.append(decimalFormat.format(Math.sqrt(mse)));
        result.append("\n");

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

}

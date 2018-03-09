package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.Coverage;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Evaluation {

    public static final String DIRECTORY = Options.DIRECTORY + "/evaluation/programs/java";
    public static final String COMPARISON_DIR = "/comparison";
    public static final String FULL_DIR = "/full";
    public static final String DOT_CSV = ".csv";

    // TODO use a class or enum>
    public static final String CONFIG_CRUSHER = "config_crusher";
    public static final String GROUND_TRUTH = "ground_truth";
    public static final String FEATURE_WISE = "feature_wise";
    public static final String PAIR_WISE = "pair_wise";
    public static final String SPLAT = "splat";
    public static final String FAMILY = "family";
    public static final String BRUTE_FORCE = "brute_force";

    private String programName;

    public Evaluation(String programName) {
        this.programName = programName;
    }

    public double getTotalSamplingTime(String approach) throws IOException {
        double time = 0.0;

        String fileString = Evaluation.DIRECTORY + "/" + this.programName + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File file = new File(fileString);

        List<String> lines = this.parseFullFile(file);

        for(String line : lines) {
            if(!line.startsWith("true")) {
                continue;
            }

            String[] entries = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            Double exec = Double.valueOf(entries[2]);
            time += exec;
        }

        return time;
    }

    private List<String> parseFullFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = "";

        List<String> lines = new ArrayList<>();

        while((line = reader.readLine()) != null) {
            if(line.isEmpty()) {
                continue;
            }

            lines.add(line.trim());
        }

        return lines;
    }

    public void writeConfigurationToPerformance(String approach, Set<PerformanceEntryStatistic> performanceEntries) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance,std,minci,maxci");
        result.append("\n");

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        for(PerformanceEntryStatistic performanceEntry : performanceEntries) {
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
            result.append(",");
            double std = performanceEntry.getRegionsToProcessedStdHumanReadable().values().iterator().next();
            result.append(decimalFormat.format(std));
            result.append(",");
            List<Double> ci = performanceEntry.getRegionsToProcessedCIHumanReadable().values().iterator().next();
            double minCI = ci.get(0);
            double maxCI = ci.get(1);
            result.append(decimalFormat.format(minCI));
            result.append(",");
            result.append(decimalFormat.format(maxCI));
            result.append("\n");
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public void writeConfigurationToPerformance(String approach, List<Coverage> coverageList, Set<PerformanceEntryStatistic> performanceEntryStats) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        Set<Set<String>> bfConfigs = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntryStats) {
            bfConfigs.add(entry.getConfiguration());
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance,std");
        result.append("\n");

        for(Coverage coverage : coverageList) {
            Set<String> config = coverage.getConfig();

            for(PerformanceEntryStatistic entry : performanceEntryStats) {
                if(!entry.getConfiguration().equals(config)) {
                    continue;
                }

                result.append(true);
                result.append(",");
                result.append('"');
                result.append(config);
                result.append('"');
                result.append(",");
                double perf = entry.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
                result.append(decimalFormat.format(perf));
                result.append(",");
                double std = entry.getRegionsToProcessedStdHumanReadable().values().iterator().next();
                result.append(decimalFormat.format(std));
                result.append("\n");

                Set<Set<String>> covereds = coverage.getCovered();

                for(Set<String> covered : covereds) {
                    if(covered.equals(config)) {
                        continue;
                    }

                    if(!bfConfigs.contains(covered)) {
                        continue;
                    }

                    result.append(false);
                    result.append(",");
                    result.append('"');
                    result.append(covered);
                    result.append('"');
                    result.append(",");
                    result.append(decimalFormat.format(perf));
                    result.append(",");
                    result.append(decimalFormat.format(std));
                    result.append("\n");
                }
            }
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public void writeConfigurationToPerformance(String approach, PerformanceModel
            performanceModel, Set<PerformanceEntryStatistic> performanceEntryStats, Set<Set<String>> configurations) throws
            IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance,std");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            PerformanceEntryStatistic performanceStat = null;

            for(PerformanceEntryStatistic performanceEntryStatistic : performanceEntryStats) {
                if(performanceEntryStatistic.getConfiguration().equals(configuration)) {
                    performanceStat = performanceEntryStatistic;
                    break;
                }
            }

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

            double perf = performanceModel.evaluate(configuration);
            perf = Math.max(0.0, perf);

            result.append(perf);
            result.append(",");
            result.append(performanceModel.evaluateStd(configuration));
            result.append("\n");
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    private File checkFileExists(String approach) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.FULL_DIR + "/"
                + approach + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(!outputFile.exists()) {
            throw new IOException("Could not find a full file for " + approach);
        }

        return outputFile;
    }

    private File deleteOutputFile(String approach1, String approach2) throws IOException {
        String outputDir = Evaluation.DIRECTORY + "/" + this.programName + "/" + Evaluation.COMPARISON_DIR + "/"
                + approach1 + "_" + approach2 + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        return outputFile;
    }

    private void compareLengthsOfFiles(File file1, File file2) throws IOException {
        FileInputStream fstream = new FileInputStream(file1);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int approach1LineCount = 0;

        while((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                approach1LineCount++;
            }
        }

        in.close();

        fstream = new FileInputStream(file2);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        int approach2LineCount = 0;

        while((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                approach2LineCount++;
            }
        }

        in.close();

        if(approach1LineCount != approach2LineCount) {
            throw new RuntimeException("The approach files do not have the same length");
        }
    }

    private Set<Set<String>> getConfigurations(File file) throws IOException {
        Set<Set<String>> configurations = new HashSet<>();

        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;
        while((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                break;
            }
        }

        while((strLine = br.readLine()) != null) {
            Set<String> options = new HashSet<>();
            int startOptionIndex = strLine.indexOf("[") + 1;
            int endOptionIndex = strLine.lastIndexOf("]");

            String optionsString = strLine.substring(startOptionIndex, endOptionIndex);
            String[] arrayOptions = optionsString.split(",");

            for(int i = 0; i < arrayOptions.length; i++) {
                options.add(arrayOptions[i].trim());
            }

            configurations.add(options);
        }

        in.close();

        return configurations;
    }

    private Map<Set<String>, List<String>> getData(File file) throws IOException {
        Map<Set<String>, List<String>> confToData = new HashMap<>();
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.readLine();

        String strLine;
        while((strLine = br.readLine()) != null) {
            Set<String> conf = new HashSet<>();
            int startOptionIndex = strLine.indexOf("[") + 1;
            int endOptionIndex = strLine.lastIndexOf("]");

            String optionsString = strLine.substring(startOptionIndex, endOptionIndex);
            String[] arrayOptions = optionsString.split(",");

            for(int i = 0; i < arrayOptions.length; i++) {
                conf.add(arrayOptions[i].trim());
            }

            List<String> data = new ArrayList<>();

            String measured = strLine.substring(0, strLine.indexOf(","));
            data.add(measured);
            String dataString = strLine.substring(endOptionIndex + 3);
            String[] entries = dataString.split(",");
            data.addAll(Arrays.asList(entries));

            confToData.put(conf, data);
        }

        return confToData;
    }

    public void compareApproaches(String approach1, String approach2) throws IOException {
        File outputFile1 = this.checkFileExists(approach1);
        File outputFile2 = this.checkFileExists(approach2);
        File outputFile = this.deleteOutputFile(approach1, approach2);
        this.compareLengthsOfFiles(outputFile1, outputFile2);

        Map<Set<String>, List<String>> data1 = this.getData(outputFile1);
        Map<Set<String>, List<String>> data2 = this.getData(outputFile2);

        Set<Set<String>> configurations = this.getConfigurations(outputFile2);

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        StringBuilder result = new StringBuilder();
        double se = 0;
        double ape = 0;
        double testCount = 0;
        int measuredWithin = 0;
        int measuredOutside = 0;
        int predictedWithin = 0;
        int predictedOutside = 0;

        result.append("measured,configuration," + approach1 + "," + approach1 + "_std," + approach2 + "," + approach2
                + "_std," + approach2 + "_minci," + approach2 + "_maxci,1withinci,absolute error,relative error,squared error");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            List<String> perf1 = data1.get(configuration);
            List<String> perf2 = data2.get(configuration);

            boolean measured = Boolean.valueOf(perf1.get(0));

            result.append('"');
            result.append(measured);
            result.append('"');
            result.append(",");
            result.append('"');
            result.append(configuration);
            result.append('"');
            result.append(",");

            String time1s = perf1.get(1);
            result.append(time1s);
            result.append(",");
            result.append(perf1.get(2));
            result.append(",");

            String time2s = perf2.get(1);
            result.append(time2s);
            result.append(",");
            result.append(perf2.get(2));
            result.append(",");

            String minCI = perf2.get(3);
            String maxCI = perf2.get(4);
            result.append(minCI);
            result.append(",");
            result.append(maxCI);
            result.append(",");

            double time1 = Double.valueOf(time1s);
            double minTime2 = Double.valueOf(minCI);
            double maxTime2 = Double.valueOf(maxCI);
            boolean within = false;

            if(minTime2 <= time1 && time1 <= maxTime2) {
                within = true;
            }

            if(measured) {
                if(within) {
                    measuredWithin++;
                }
                else {
                    measuredOutside++;

                }
            }
            else {
                if(within) {
                    predictedWithin++;
                }
                else {
                    predictedOutside++;
                }
            }

            result.append(within);
            result.append(",");

            Double time2 = Double.valueOf(time2s);
            double absoluteError = Math.abs(time1 - time2);
            double relativeError = 0.0;

            if(time2 != 0) {
                relativeError = absoluteError / time2;
            }

            double squaredError = Math.pow(time2 - time1, 2);

            result.append(decimalFormat.format(absoluteError));
            result.append(",");
            result.append(decimalFormat.format(relativeError));
            result.append(",");
            result.append(decimalFormat.format(squaredError));
            result.append("\n");

            if(!measured && time2 >= 1.0) {
                se += squaredError;
                ape += relativeError;
                testCount++;
            }
        }

        result.append("\n");
        result.append("Total: ");
        result.append(configurations.size());
        result.append("\n");
        result.append("MSE: ");
        double mse = se / testCount;
        result.append(decimalFormat.format(mse));
        result.append("\n");
        result.append("RMSE: ");
        result.append(decimalFormat.format(Math.sqrt(mse)));
        result.append("\n");
        result.append("MAPE: ");
        double mape = ape / testCount * 100;
        result.append(decimalFormat.format(mape));
        result.append("\n");
        result.append("Measured within ci: ");
        result.append(measuredWithin);
        result.append("\n");
        result.append("Measured outside ci: ");
        result.append(measuredOutside);
        result.append("\n");
        result.append("Predicted within ci: ");
        result.append(predictedWithin);
        result.append("\n");
        result.append("Predicted outside ci: ");
        result.append(predictedOutside);
        result.append("\n");
        result.append("Valid configs: ");
        result.append(testCount);
        result.append("\n");

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public String getProgramName() {
        return this.programName;
    }

    public double getTotalSamplingTime(Set<PerformanceEntryStatistic> entries) {
        double time = 0.0;

        for(PerformanceEntryStatistic entry : entries) {
            Map<Region, Double> regionToTime = entry.getRegionsToProcessedPerformanceHumanReadable();

            for(double exec : regionToTime.values()) {
                time += exec;
            }
        }

        return time;
    }
}

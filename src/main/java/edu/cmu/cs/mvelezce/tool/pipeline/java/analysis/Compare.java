package edu.cmu.cs.mvelezce.tool.pipeline.java.analysis;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.execute.java.BruteForce;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaPipeline;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 6/30/17.
 */
public class Compare {

    public static final String COMPARE_DIR = Options.DIRECTORY + "/comparison/java/programs";

    public static void comparePMToBF(String programName) throws IOException {
//        FileInputStream fstream = new FileInputStream(BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);
//        DataInputStream in = new DataInputStream(fstream);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String strLine;
//        int bfLineCount = 0;
//
//        while((strLine = br.readLine()) != null) {
//            if(!strLine.isEmpty()) {
//                bfLineCount ++;
//            }
//        }
//
//        in.close();
//
//        fstream = new FileInputStream(JavaPipeline.PM_RES_DIR + "/" + programName + Options.DOT_CSV);
//        in = new DataInputStream(fstream);
//        br = new BufferedReader(new InputStreamReader(in));
//        int pfLineCount = 0;
//
//        while((strLine = br.readLine()) != null) {
//            if(!strLine.isEmpty()) {
//                pfLineCount ++;
//            }
//        }
//
//        in.close();
//
//        if(bfLineCount != pfLineCount) {
//            throw new RuntimeException("The pf and bf files do not have the same length");
//        }

        Set<String> allOptions = new HashSet<>();

        FileInputStream fstream = new FileInputStream(BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String header = "";
        String strLine = "";

        while((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                header = strLine;
                break;
            }
        }

        while((strLine = br.readLine()) != null && !strLine.equals(header)) {
            if(!strLine.isEmpty()) {
                Set<String> options = new HashSet<>();
                String optionsString = strLine.substring(2, strLine.lastIndexOf('"') - 1);
                String[] arrayOptions = optionsString.split(",");

                for(int i = 0; i < arrayOptions.length; i++) {
                    options.add(arrayOptions[i].trim());
                }

                allOptions.addAll(options);
            }
        }

        in.close();
        allOptions.remove("");

        Set<Set<String>> configurations = Helper.getConfigurations(allOptions);
        Set<PerformanceEntry> bfEntries = Compare.createEntries(programName, BruteForce.BF_RES_DIR, configurations);
        Set<PerformanceEntry> pmEntries = Compare.createEntries(programName, JavaPipeline.PM_RES_DIR, configurations);

        StringBuilder result = new StringBuilder();
        double se = 0;
        int entries = 0;
        result.append("configuration,pm mean,bf mean,absolute error,relative % error,squared error");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            result.append('"');
            result.append(configuration);
            result.append('"');
            result.append(",");

            double pmMean = Double.MIN_VALUE;
            double bfMean = Double.MIN_VALUE;

            for(PerformanceEntry entry : pmEntries) {
                if(entry.configuration.equals(configuration)) {
                    pmMean = entry.mean();
                    result.append(pmMean);
                    break;
                }
            }

            result.append(",");

            for(PerformanceEntry entry : bfEntries) {
                if(entry.configuration.equals(configuration)) {
                    bfMean = entry.mean();
                    result.append(bfMean);
                    break;
                }
            }

            result.append(",");
            double absoluteError = Math.abs(bfMean - pmMean);
            double relativeError = absoluteError / bfMean * 100.0;
            double squaredError = Math.pow(bfMean - pmMean, 2);
            result.append(absoluteError);
            result.append(",");
            result.append(relativeError);
            result.append(",");
            result.append(squaredError);
            result.append("\n");

            se += squaredError;
            entries += 1;
        }

        result.append("\n");
        result.append("MSE: ");
        double mse = se/entries;
        result.append(mse);
        result.append("\n");
        result.append("RMSE: ");
        result.append(Math.sqrt(mse));
        result.append("\n");

        File directory = new File(Compare.COMPARE_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = Compare.COMPARE_DIR + "/" + programName + Options.DOT_CSV;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

//    private static StringBuilder calculateStats(Set<PerformanceEntry> performanceEntries, Set<String> configuration) {
//        StringBuilder result = new StringBuilder();
//
//        for(PerformanceEntry entry : performanceEntries) {
//            if(entry.configuration.equals(configuration)) {
//                result.append(entry.mean());
//                result.append(",");
//                result.append(entry.std());
//                break;
//            }
//        }
//
//        return result;
//    }

    private static Set<PerformanceEntry> createEntries(String programName, String approachDir, Set<Set<String>> configurations) throws IOException {
        Set<PerformanceEntry> entries = new HashSet<>();

        for(Set<String> configuration : configurations) {
            List<Double> valuesList = new ArrayList<>();
            FileInputStream fstream = new FileInputStream(approachDir + "/" + programName + Options.DOT_CSV);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String header = "";

            while((strLine = br.readLine()) != null) {
                if(!strLine.isEmpty()) {
                    header = strLine;
                    break;
                }
            }

            while((strLine = br.readLine()) != null) {
                if(!strLine.isEmpty() && !strLine.equals(header)) {
                    String optionsString = strLine.substring(2, strLine.lastIndexOf('"') - 1);
                    String[] arrayOptions;

                    if(optionsString.isEmpty()) {
                        arrayOptions = new String[0];
                    }
                    else {
                        arrayOptions = optionsString.split(",");
                    }

                    boolean matchedConfiguration = true;

                    if(configuration.size() != arrayOptions.length) {
                        matchedConfiguration = false;
                    }

                    if(!matchedConfiguration) {
                        continue;
                    }

                    for(int i = 0; i < arrayOptions.length; i++) {
                        if(!configuration.contains(arrayOptions[i].trim())) {
                            matchedConfiguration = false;
                        }
                    }

                    if(!matchedConfiguration) {
                        continue;
                    }

                    String perfString = strLine.substring(strLine.lastIndexOf(",") + 1);
                    valuesList.add(Double.valueOf(perfString));
                }

            }

            Double[] valuesArray = valuesList.toArray(new Double[0]);
            double[] values = new double[valuesArray.length];

            for(int i = 0; i < valuesArray.length; i++) {
                values[i] = valuesArray[i];
            }

            PerformanceEntry performanceEntry = new PerformanceEntry(configuration, values);
            entries.add(performanceEntry);
        }

        return entries;
    }

    public static class PerformanceEntry {
        private Set<String> configuration;
        private double mean = Double.MIN_VALUE;
        private double std = Double.MIN_VALUE;
        private double[] values;

        public PerformanceEntry(Set<String> configuration, double[] values) {
            this.configuration = configuration;
            this.values = values;
        }

        public double mean() {
            if(this.mean == Double.MIN_VALUE) {
                Mean mathMean = new Mean();
                this.mean = mathMean.evaluate(this.values);
            }

            return this.mean;
        }

        public double std() {
            if(this.std == Double.MIN_VALUE) {
                StandardDeviation mathStd = new StandardDeviation();
                this.std = mathStd.evaluate(this.values);
            }

            return this.std;
        }

    }


}

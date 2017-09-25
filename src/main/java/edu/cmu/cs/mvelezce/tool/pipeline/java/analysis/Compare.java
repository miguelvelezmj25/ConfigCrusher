package edu.cmu.cs.mvelezce.tool.pipeline.java.analysis;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 6/30/17.
 */
public class Compare {

    public static final String COMPARE_DIR = Options.DIRECTORY + "/comparison/java/programs";

    public static void compare(String programName, String approach1Dir, String approach2Dir) throws IOException {
        FileInputStream fstream = new FileInputStream(approach1Dir + "/" + programName + Options.DOT_CSV);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        int bfLineCount = 0;

        while ((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                bfLineCount++;
            }
        }

        in.close();

        fstream = new FileInputStream(approach2Dir + "/" + programName + Options.DOT_CSV);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        int pfLineCount = 0;

        while ((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                pfLineCount++;
            }
        }

        in.close();

//        if(bfLineCount != pfLineCount) {
//            throw new RuntimeException("The approach files do not have the same length");
//        }

        Set<String> allOptions = new HashSet<>();

        fstream = new FileInputStream(approach1Dir + "/" + programName + Options.DOT_CSV);
        in = new DataInputStream(fstream);
        br = new BufferedReader(new InputStreamReader(in));
        String header = "";

        while ((strLine = br.readLine()) != null) {
            if(!strLine.isEmpty()) {
                header = strLine;
                break;
            }
        }

        while ((strLine = br.readLine()) != null && !strLine.equals(header)) {
            if(!strLine.isEmpty() && !strLine.contains("Average")) {
                Set<String> options = new HashSet<>();
                String optionsString = strLine.substring(7, strLine.lastIndexOf('"') - 1);
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
        Set<PerformanceEntryStatistic> app1Stats = Compare.createEntries(programName, approach1Dir, configurations);
        Set<PerformanceEntryStatistic> app2Stats = Compare.createEntries(programName, approach2Dir, configurations);

        StringBuilder result = new StringBuilder();
        double se = 0;
        int entries = 0;
        result.append("measured,configuration,ap1 mean,ap2 mean,absolute error,relative % error,squared error");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            PerformanceEntryStatistic app1Entry = null;
            PerformanceEntryStatistic app2Entry = null;

            for(PerformanceEntryStatistic entry : app1Stats) {
                if(!entry.getConfiguration().equals(configuration)) {
                    continue;
                }

                app1Entry = entry;
                break;
            }

            for(PerformanceEntryStatistic entry : app2Stats) {
                if(!entry.getConfiguration().equals(configuration)) {
                    continue;
                }

                app2Entry = entry;
                break;
            }

            result.append('"');
            result.append(Boolean.valueOf(app1Entry.isMeasured()) && Boolean.valueOf(app2Entry.isMeasured()));
            result.append('"');
            result.append(",");
            result.append('"');
            result.append(configuration);
            result.append('"');
            result.append(",");
//            result.append(app1Entry.getMean());
            result.append(",");

//            result.append(app2Entry.getMean());
            result.append(",");

//            double absoluteError = Math.abs(app2Entry.getMean() - app1Entry.getMean());
//            double relativeError = absoluteError / app2Entry.getMean() * 100.0;
//            double squaredError = Math.pow(app2Entry.getMean() - app1Entry.getMean(), 2);
//
//            result.append(absoluteError);
//            result.append(",");
//            result.append(relativeError);
//            result.append(",");
//            result.append(squaredError);
//            result.append("\n");
//
//            se += squaredError;
            entries += 1;
        }

        result.append("\n");
        result.append("MSE: ");
        double mse = se / entries;
        result.append(mse);
        result.append("\n");
        result.append("RMSE: ");
        result.append(Math.sqrt(mse));
        result.append("\n");

        File directory = new File(Compare.COMPARE_DIR);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String approach1 = approach1Dir.replace(Options.DIRECTORY, "");
        approach1 = approach1.substring(1, approach1.indexOf("_"));
        String approach2 = approach2Dir.replace(Options.DIRECTORY, "");
        approach2 = approach2.substring(1, approach2.indexOf("_"));

        String outputFile = Compare.COMPARE_DIR + "/" + programName + "_" + approach1 + "_" + approach2 + Options.DOT_CSV;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    private static Set<PerformanceEntryStatistic> createEntries(String programName, String approachDir, Set<Set<String>> configurations) throws IOException {
        Set<PerformanceEntryStatistic> entries = new HashSet<>();

        for(Set<String> configuration : configurations) {
            FileInputStream fstream = new FileInputStream(approachDir + "/" + programName + Options.DOT_CSV);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String header = "";

            while ((strLine = br.readLine()) != null) {
                if(!strLine.isEmpty()) {
                    header = strLine;
                    break;
                }
            }

            while ((strLine = br.readLine()) != null) {
                if(strLine.isEmpty() || strLine.equals(header) || strLine.contains("Average")) {
                    continue;
                }

                String optionsString = strLine.substring(7, strLine.lastIndexOf('"') - 1);
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

                PerformanceEntryStatistic perfStat;
                int commaOffset = Math.max(0, configuration.size() - 1);
                String[] strEntries = strLine.split(",");
                String measuredString = strEntries[0].trim();
                boolean measured = Boolean.valueOf(measuredString);
                String perfString = strEntries[2 + commaOffset].trim();

//                if((strEntries.length - commaOffset) == 3) {
//                    perfStat = new PerformanceEntryStatistic(measured + "", configuration, Double.valueOf(perfString), 0.0);
//                }
//                else if((strEntries.length - commaOffset) == 4) {
//                    String stdString = strEntries[3 + commaOffset].trim();
//                    perfStat = new PerformanceEntryStatistic(measured + "", configuration, Double.valueOf(perfString), Double.valueOf(stdString));
//                }
//                else {
//                    throw new RuntimeException("Could not parse the file");
//                }
//
//                entries.add(perfStat);
            }

        }

        return entries;
    }

}

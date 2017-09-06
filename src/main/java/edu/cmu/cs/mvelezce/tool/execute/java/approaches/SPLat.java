package edu.cmu.cs.mvelezce.tool.execute.java.approaches;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.ProgramAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
import edu.cmu.cs.mvelezce.tool.pipeline.java.analysis.PerformanceStatistic;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Created by mvelezce on 7/9/17.
 */
public class SPLat {

//    public static final String SPLAT_RES_DIR = Options.DIRECTORY + "/splat_res/java/programs";
//
//    public static void measure(String programName) throws IOException, ParseException {
//        // Get regions to options
//        String[] analysisArgs = new String[0];
//        Map<JavaRegion, Set<Set<String>>> regionsToOptions = ProgramAnalysis.analyze(programName, analysisArgs);
//
//        // Get a file with the execution trace of our approach
//        File file = new File(BaseExecutor.DIRECTORY + "/" + programName + "_" + 0 + Options.DOT_JSON);
//
//        if(!file.exists()) {
//            throw new RuntimeException("The file " + file + " does not exist");
//        }
//
//        Set<String> splatOptions = SPLat.getOptions(file, regionsToOptions);
//        Set<Set<String>> splatConfigurations = Helper.getConfigurations(splatOptions);
//        List<PerformanceStatistic> perfStats = SPLat.getTimesFromBF(programName, splatConfigurations);
//        SPLat.saveSPLAtPerformance(programName, perfStats);
//    }
//
//    public static Set<String> getOptions(File file, Map<JavaRegion, Set<Set<String>>> regionsToOptions) throws IOException, ParseException {
////        Set<String> splatOptions = new HashSet<>();
////        JSONParser parser = new JSONParser();
////        JSONObject cache = (JSONObject) parser.parse(new FileReader(file));
////        JSONArray result = (JSONArray) cache.get(BaseExecutor.EXECUTIONS);
////
////        for(Object resultEntry : result) {
////            JSONObject execution = (JSONObject) resultEntry;
////            JSONArray executionTraceResult = (JSONArray) execution.get(BaseExecutor.EXECUTION_TRACE);
////
////            for(Object executionTraceResultEntry : executionTraceResult) {
////                JSONObject regionResult = (JSONObject) executionTraceResultEntry;
////                String regionID = (String) regionResult.get(BaseExecutor.ID);
////
////                for(Map.Entry<JavaRegion, Set<String>> regionToOptions : regionsToOptions.entrySet()) {
////                    if(regionToOptions.getKey().getRegionID().equals(regionID)) {
////                        splatOptions.addAll(regionToOptions.getValue());
////                        break;
////                    }
////                }
////            }
////        }
////
////        return splatOptions;
//        return null; // TODO make change since interface changed
//    }
//
//    public static List<PerformanceStatistic> getTimesFromBF(String programName, Set<Set<String>> configurations) throws IOException, ParseException {
//        File file = new File(BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);
//
//        if(!file.exists()) {
//            throw new RuntimeException("The file " + file.getName() + " does not exist");
//        }
//
//        List<PerformanceStatistic> perfStats = new ArrayList<>();
//        FileInputStream fstream = new FileInputStream(file);
//        DataInputStream in = new DataInputStream(fstream);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String strLine;
//
//        while ((strLine = br.readLine()) != null) {
//            if(!strLine.isEmpty()) {
//                break;
//            }
//        }
//
//        while ((strLine = br.readLine()) != null) {
//            if(strLine.isEmpty()) {
//                continue;
//            }
//
//            String configurationString = strLine.substring(strLine.indexOf("[") + 1);
//            configurationString = configurationString.substring(0, configurationString.indexOf("]"));
//            String[] configurationArray = configurationString.split(",");
//
//            Set<String> configuration = new HashSet<>();
//
//            for(String option : configurationArray) {
//                configuration.add(option.trim());
//            }
//
//            configuration.remove("");
//
//            for(Set<String> splatConfiguration : configurations) {
//                if(splatConfiguration.equals(configuration)) {
//                    String[] values = strLine.split(",");
//                    double std = Double.parseDouble(values[values.length - 1].trim());
//                    double mean = Double.parseDouble(values[values.length - 2].trim());
//
//                    PerformanceStatistic perfStat = new PerformanceStatistic("true", splatConfiguration, mean, std);
//                    perfStats.add(perfStat);
//
//                    break;
//                }
//            }
//        }
//
//        Set<String> splatOptions = new HashSet<>();
//
//        for(Set<String> splatConfiguration : configurations) {
//            splatOptions.addAll(splatConfiguration);
//        }
//
//        Set<String> allOptions = BaseExecutor.getOptions(programName);
//        Set<String> optionsNotExecutedBySPLat = new HashSet<>(allOptions);
//        optionsNotExecutedBySPLat.removeAll(splatOptions);
//
//        if(optionsNotExecutedBySPLat.isEmpty()) {
//            return perfStats;
//        }
//
//        List<PerformanceStatistic> perfStatsToAdd = new ArrayList<>();
//        fstream = new FileInputStream(file);
//        in = new DataInputStream(fstream);
//        br = new BufferedReader(new InputStreamReader(in));
//
//        while ((strLine = br.readLine()) != null) {
//            if(strLine.isEmpty()) {
//                continue;
//            }
//
//            String configurationString = strLine.substring(strLine.indexOf("[") + 1);
//            configurationString = configurationString.substring(0, strLine.indexOf("]"));
//            String[] configurationArray = configurationString.split(",");
//
//            Set<String> fullConfig = new HashSet<>();
//
//            for(String option : configurationArray) {
//                fullConfig.add(option.trim());
//            }
//
//            Set<String> splatConfig = new HashSet<>(fullConfig);
//            splatConfig.removeAll(optionsNotExecutedBySPLat);
//
//            if(splatConfig.isEmpty()) {
//                continue;
//            }
//
//            for(PerformanceStatistic perfStat : perfStats) {
//                if(perfStat.getConfiguration().equals(splatConfig)) {
//                    PerformanceStatistic newPerfStat = new PerformanceStatistic("false", fullConfig, perfStat.getMean(), perfStat.getStd());
//                    perfStatsToAdd.add(newPerfStat);
//                }
//            }
//        }
//
//        perfStats.addAll(perfStatsToAdd);
//
//        return perfStats;
//    }
//
//    public static void saveSPLAtPerformance(String programName, List<PerformanceStatistic> perfStats) throws IOException {
//        File file = new File(SPLat.SPLAT_RES_DIR + "/" + programName + Options.DOT_CSV);
//
//        if(file.exists()) {
//            if(!file.delete()) {
//                throw new RuntimeException("Could not delete " + file);
//            }
//        }
//
//        StringBuilder result = new StringBuilder();
//        result.append("measured,configuration,performance,std");
//        result.append("\n");
//
//        for(PerformanceStatistic perfStat : perfStats) {
//            result.append(perfStat.getMeasured());
//            result.append(",");
//            result.append('"');
//            result.append(perfStat.getConfiguration());
//            result.append('"');
//            result.append(",");
//            result.append(perfStat.getMean());
//            result.append(",");
//            result.append(perfStat.getStd());
//            result.append("\n");
//        }
//
//        File directory = new File(SPLat.SPLAT_RES_DIR);
//
//        if(!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String outputFile = directory + "/" + programName + Options.DOT_CSV;
//        file = new File(outputFile);
//        FileWriter writer = new FileWriter(file, true);
//        writer.write(result.toString());
//        writer.flush();
//        writer.close();
//    }
}

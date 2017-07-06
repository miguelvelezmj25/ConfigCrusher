package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Formatter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.pipeline.java.analysis.PerformanceStatistic;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mvelezce on 6/30/17.
 */
public class BruteForce {

    public static final String BF_RES_DIR = Options.DIRECTORY + "/bf_res/java/programs";



    public static Set<PerformanceEntry> repeatProcessMeasure(String programName, int iterations) throws IOException, ParseException, InterruptedException {
        programName += "-bf";
        String[] args = new String[1];
        args[0] = "-i" + iterations;
        Options.getCommandLine(args);

        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();

        for(int i = 0; i < Options.getIterations(); i++) {
            executionsPerformance.add(Executor.measureConfigurationPerformance(programName + Executor.UNDERSCORE + i, args));
        }

        List<PerformanceStatistic> perfStats = Executor.getExecutionsStats(executionsPerformance);
        Set<PerformanceEntry> measuredPerformance = Executor.averageExecutions(perfStats, executionsPerformance.get(0));
        programName = programName.substring(0, programName.indexOf("-"));
        BruteForce.saveBFPerformance(programName, perfStats);

        return measuredPerformance;
    }

    public static Set<PerformanceEntry> repeatProcessMeasure(String programName, int iterations, String srcDir, String classDir, String entryPoint) throws IOException, ParseException, InterruptedException {
        Formatter.compile(srcDir, classDir);
        Formatter.formatReturnWithMethod(classDir);

        String[] args = new String[0];
        Options.getCommandLine(args);

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        programName += "-bf";
        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i" + iterations;
        Options.getCommandLine(args);

        configurations = Helper.getConfigurations(options);
        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();

        for(int i = 0; i < Options.getIterations(); i++) {
            executionsPerformance.add(Executor.measureConfigurationPerformance(programName + Executor.UNDERSCORE + i, args, entryPoint, classDir, configurations));
        }

        List<PerformanceStatistic> perfStats = Executor.getExecutionsStats(executionsPerformance);
        Set<PerformanceEntry> measuredPerformance = Executor.averageExecutions(perfStats, executionsPerformance.get(0));
        programName = programName.substring(0, programName.indexOf("-"));
        BruteForce.saveBFPerformance(programName, perfStats);

        return measuredPerformance;
    }

    public static void saveBFPerformance(String programName, List<PerformanceStatistic> perfStats) throws IOException {
        File file = new File(BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);

        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("Could not delete " + file);
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance,std");
        result.append("\n");

        for(PerformanceStatistic perfStat : perfStats) {
            if(perfStat.getRegionsToMean().size() != 1) {
                throw new RuntimeException("The performance entry should only have measured the entire program " + perfStat.getRegionsToMean().keySet());
            }

            perfStat.setMeasured("true");
            result.append("true");
            result.append(",");
            result.append('"');
            result.append(perfStat.getConfiguration());
            result.append('"');
            result.append(",");
            result.append(perfStat.getRegionsToMean().values().iterator().next() / 1000000000.0);
            result.append(",");
            result.append(perfStat.getRegionsToStd().values().iterator().next() / 1000000000.0);
            result.append("\n");
        }

        File directory = new File(BruteForce.BF_RES_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = directory + "/" + programName + Options.DOT_CSV;
        file = new File(outputFile);
        FileWriter writer = new FileWriter(file, true);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

}

package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Formatter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mvelezce on 6/30/17.
 */
public class BruteForce {

    public static final String BF_RES_DIR = Options.DIRECTORY + "/bf_res/java/programs";

    public static Set<PerformanceEntry> measure(String programName, int iterations) throws IOException, ParseException, InterruptedException {
        programName += "-bf";
        String[] args = new String[1];
        args[0] = "-i" + iterations;

        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args);
        return measuredPerformance;
    }

    public static Set<PerformanceEntry> measure(String programName, int iterations, String srcDir, String classDir, String entryPoint) throws IOException, ParseException, InterruptedException {
        Formatter.compile(srcDir, classDir);
        Formatter.formatReturnWithMethod(classDir);

        String[] args = new String[0];

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

        configurations = Helper.getConfigurations(options);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDir, configurations);
        return measuredPerformance;
    }

}

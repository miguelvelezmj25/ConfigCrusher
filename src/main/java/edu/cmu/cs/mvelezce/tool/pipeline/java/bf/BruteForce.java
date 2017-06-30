package edu.cmu.cs.mvelezce.tool.pipeline.java.bf;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
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

    public static Set<PerformanceEntry> measure(String programName, String srcDir, String classDir, String entryPoint) throws IOException, ParseException, InterruptedException {
        Formatter.compile(srcDir, classDir);
        Formatter.formatReturnWithMethod(classDir);

        String[] args = new String[0];

        Set<Set<String>> configurations = Simple.getConfigurationsToExecute(programName, args);
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        programName += "-bf";
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        configurations = Helper.getConfigurations(options);
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDir, configurations);
        return measuredPerformance;
    }

}

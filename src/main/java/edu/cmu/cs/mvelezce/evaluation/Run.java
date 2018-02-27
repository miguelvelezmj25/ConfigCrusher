package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.Featurewise;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.Pairwise;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.density.DensityAdapter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static edu.cmu.cs.mvelezce.tool.Options.USER_HOME;

public class Run {

    public static void main(String[] args) throws IOException, InterruptedException {
        String programName = "density";
//        String classDirectory = USER_HOME + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/density/target/classes";
        String classDirectory = USER_HOME + "/Documents/performance-mapper-evaluation/instrumented/density/target/classes";
        String entryPoint = "at.favre.tools.dconvert.Main";

        // Program arguments
        args = new String[0];


        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> ccConfigs = compression.compressConfigurations(args);

        List<String> options = DensityAdapter.getDensityOptions();
        Set<Set<String>> fwConfigs = Featurewise.getFeaturewiseConfigurations(options);
        Set<Set<String>> pwConfigs = Pairwise.getPairwiseConfigurations(options);

        Set<Set<String>> configs = Helper.mergeConfigs(ccConfigs, fwConfigs);
        configs = Helper.mergeConfigs(configs, pwConfigs);

        Set<Set<String>> randomConfigs = Helper.getRandomConfigs(options, 1000, configs);

        configs = Helper.mergeConfigs(randomConfigs, configs);

        System.out.println("Configurations: " + configs.size());

//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i1";
//
//        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
//        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
//        measuredPerformance.size();
    }

    public static void removeSampledConfigurations(String name, Set<Set<String>> configurations) throws IOException, InterruptedException {
        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(name);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        for(PerformanceEntryStatistic entry : performanceEntries) {
            configurations.remove(entry.getConfiguration());
        }
    }

}
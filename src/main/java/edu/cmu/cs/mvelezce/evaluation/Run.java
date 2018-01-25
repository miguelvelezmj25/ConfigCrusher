package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Run {

    public static void main(String[] args) throws IOException, InterruptedException {
        String programName = "find";
//        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/find/target/classes";
        String classDirectory = "/home/mvelezce/Documents/performance-mapper-evaluation/original/find/target/classes";

        String entryPoint = "org.unix4j.find.Main";

//        Set<String> options = new HashSet<>(FindAdapter.getFindOptions());
//        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        Set<String> empty = new HashSet<>();
        Set<Set<String>> configurations = new HashSet<>();
        configurations.add(empty);

        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

}



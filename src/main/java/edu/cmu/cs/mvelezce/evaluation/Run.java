package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sort.SortAdapter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Run {

    public static void main(String[] args) throws IOException, InterruptedException {
        String programName = "sort";
//        String classDirectory = System.getProperty("user.home") + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/sort/target/classes";
        String classDirectory = System.getProperty("user.home") + "/Documents/performance-mapper-evaluation/original/sort/target/classes";

        String entryPoint = "org.unix4j.sort.Main";

        Set<String> options = new HashSet<>(SortAdapter.getSortOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        removeSampledConfigurations(programName, configurations);

//        Set<String> empty = new HashSet<>();
//        empty.add("MONTHSORT");
//        empty.add("VERSIONSORT");
//        empty.add("MERGE");
//        empty.add("REVERSE");
//        empty.add("GENERALNUMERICSORT");

//        configurations = new HashSet<>();
//        configurations.add(empty);

        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
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


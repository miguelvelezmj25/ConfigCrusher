package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ConfigCrusherExecutorTest {

    @Test
    public void sleep1() throws Exception {
        String programName = "sleep1";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void runningExample() throws Exception {
        String programName = "running-example";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";
        String entryPoint = "edu.cmu.cs.mvelezce.Example";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

//        configurations.clear();
//        Set<String> n = new HashSet<>();
//        n.add("A");
//        configurations.add(n);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

    @Test
    public void colorCounter() throws Exception {
        String programName = "pngtasticColorCounter";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-counter/out/production/pngtastic-counter";
        String entryPoint = "counter.com.googlecode.pngtastic.Run";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);


        configurations.clear();
        Set<String> n = new HashSet<>();
//        n.add("A");
        configurations.add(n);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

    @Test
    public void optimizer() throws Exception {
        String programName = "pngtasticOptimizer";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-optimizer/out/production/pngtastic-optimizer";
        String entryPoint = "optimizer.com.googlecode.pngtastic.Run";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

//        configurations.clear();
//        Set<String> n = new HashSet<>();
//        n.add("COMPRESSOR");
//        n.add("LOGLEVEL");
//        n.add("REMOVEGAMMA");
//        configurations.add(n);


        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

    @Test
    public void regions12() throws Exception {
        String programName = "regions12";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";
        String entryPoint = "edu.cmu.cs.mvelezce.Regions12";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

        configurations.clear();
        Set<String> n = new HashSet<>();
        n.add("A");
        n.add("B");
        n.add("C");
        configurations.add(n);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

    @Test
    public void regions13() throws Exception {
        String programName = "regions13";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";
        String entryPoint = "edu.cmu.cs.mvelezce.Regions13";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

        configurations.clear();
        Set<String> n = new HashSet<>();
        n.add("A");
        n.add("B");
        configurations.add(n);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

    @Test
    public void regions14() throws Exception {
        String programName = "regions14";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";
        String entryPoint = "edu.cmu.cs.mvelezce.Regions14";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

        configurations.clear();
        Set<String> n = new HashSet<>();
        n.add("A");
        n.add("B");
        configurations.add(n);

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

}
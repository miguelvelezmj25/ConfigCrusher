package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute;

import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BruteForceExecutorTest {

//    @Test
//    public void testElevator() throws IOException, ParseException, InterruptedException {
//        String programName = "elevator";
//        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
//        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
//        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";
//
//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5, srcDir, classDir, entryPoint);
////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
//    }
//
//    @Test
//    public void testPngtastic() throws IOException, ParseException, InterruptedException {
//        String programName = "pngtastic";
//        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/";
//        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/target/classes/";
//        String entryPoint = "com.googlecode.pngtastic.PngtasticColorCounter";
//
//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1, srcDir, classDir, entryPoint);
////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
//    }
//
//    @Test
//    public void testSleep1() throws IOException, ParseException, InterruptedException {
//        String programName = "sleep1";
//        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
//        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";
//
//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5, srcDir, classDir, entryPoint);
////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
//    }
//
//    @Test
//    public void testSleep26() throws IOException, ParseException, InterruptedException {
//        String programName = "sleep26";
//        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
//        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep26";
//
//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1, srcDir, classDir, entryPoint);
////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
//    }
//
//    @Test
//    public void testSleep29() throws IOException, ParseException, InterruptedException {
//        String programName = "sleep29";
//        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
//        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep29";
//
//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1, srcDir, classDir, entryPoint);
////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
//    }
//
//    @Test
//    public void testZipme() throws IOException, ParseException, InterruptedException {
//        String programName = "zipme";
//        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/";
//        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
//        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";
//
//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1, srcDir, classDir, entryPoint);
//    }

    @Test
    public void runningExample() throws IOException, InterruptedException {
        String programName = "running-example";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes";
        String entryPoint = "edu.cmu.cs.mvelezce.Example";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void colorCounter() throws IOException, InterruptedException {
        String programName = "pngtasticColorCounter";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/out/production/pngtastic-counter";
        String entryPoint = "counter.com.googlecode.pngtastic.Run";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void optimizer() throws IOException, InterruptedException {
        String programName = "pngtasticOptimizer";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/out/production/pngtastic-optimizer";
        String entryPoint = "optimizer.com.googlecode.pngtastic.Run";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void prevayler() throws IOException, InterruptedException {
        String programName = "prevayler";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/target/classes";
        String entryPoint = "org.prevayler.demos.demo1.PrimeNumbers";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void kanzi() throws IOException, InterruptedException {
        String programName = "kanzi";
        String entryPoint = "kanzi.Run";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/kanzi/target/classes";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i3";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void regions12() throws IOException, InterruptedException {
        String programName = "regions12";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ;
        String entryPoint = "edu.cmu.cs.mvelezce.Regions12";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void regions16() throws IOException, InterruptedException {
        String programName = "regions16";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
        ;
        String entryPoint = "edu.cmu.cs.mvelezce.Regions16";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

    @Test
    public void grep() throws IOException, InterruptedException {
        String programName = "grep";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/grep/target/classes";

        String entryPoint = "org.unix4j.grep.Main";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
        System.out.println("Configurations to sample: " + configurations.size());

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        Executor executor = new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    }

}
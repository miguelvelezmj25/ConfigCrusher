package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry2;
import org.junit.Test;

import java.util.Set;

public class DefaultExecutorTest {

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

        Executor executor = new DefaultExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntry2> measuredPerformance = executor.execute(args);
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

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        Executor executor = new DefaultExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntry2> measuredPerformance = executor.execute(args);
    }

    @Test
    public void colorCounter() throws Exception {
        String programName = "pngtasticColorCounter";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-counter/out/production/pngtastic-counter";
        String entryPoint = "com.googlecode.pngtastic.Run";

        // Program arguments
        String[] args = new String[0];

        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);

//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i1";

        Executor executor = new DefaultExecutor(programName, entryPoint, classDirectory, configurations);
        Set<PerformanceEntry2> measuredPerformance = executor.execute(args);
        measuredPerformance.size();
    }

}
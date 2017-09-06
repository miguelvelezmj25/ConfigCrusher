package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class DefaultExecutorTest {
    @Test
    public void execute() throws Exception {

        String programName = "sleep1";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
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
        Set<PerformanceEntry> measuredPerformance = executor.execute(args);
    }

}